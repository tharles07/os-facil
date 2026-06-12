package com.sistema_os.OsFacil.service;

import com.sistema_os.OsFacil.model.Chamado;
import com.sistema_os.OsFacil.model.Cliente;
import com.sistema_os.OsFacil.model.Empresa;
import com.sistema_os.OsFacil.repository.ChamadoRepository;
import com.sistema_os.OsFacil.repository.ClienteRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ChamadoService {

    private final ChamadoRepository repository;
    private final ClienteRepository clienteRepository;

    public ChamadoService(ChamadoRepository repository, ClienteRepository clienteRepository) {
        this.repository = repository;
        this.clienteRepository = clienteRepository;
    }

    /* ==========================
       PEGAR EMPRESA DO TOKEN
    ========================== */
    private Long getEmpresaId() {
        Object details = SecurityContextHolder.getContext()
                .getAuthentication()
                .getDetails();

        if (details == null) {
            throw new RuntimeException("Empresa não identificada");
        }

        return ((Number) details).longValue();
    }

    /* ==========================
       CRIAR
    ========================== */
    public Chamado salvar(Chamado chamado) {

        Long empresaId = getEmpresaId();

        if (chamado.getCliente() == null || chamado.getCliente().getId() == null) {
            throw new RuntimeException("Cliente obrigatório");
        }

        Cliente cliente = clienteRepository.findById(chamado.getCliente().getId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        if (cliente.getEmpresa() == null || !Long.valueOf(cliente.getEmpresa().getId()).equals(empresaId)) {
            throw new RuntimeException("Cliente não pertence à empresa autenticada");
        }

        Empresa empresa = new Empresa();
        empresa.setId(empresaId);

        if (chamado.getData() == null) {
            chamado.setData(LocalDate.now());
        }

        if (chamado.getStatus() == null || chamado.getStatus().isBlank()) {
            chamado.setStatus("ABERTO");
        }

        if (chamado.getPago() == null) {
            chamado.setPago(false);
        }

        chamado.setCliente(cliente);
        chamado.setEmpresa(empresa);
        return repository.save(chamado);

    }

    /* ==========================
       LISTAR POR EMPRESA
    ========================== */
    public List<Chamado> listarPorEmpresa() {

        Long empresaId = getEmpresaId();

        return repository.findByEmpresaId(empresaId);
    }

    /* ==========================
       BUSCAR SEGURO
    ========================== */
    public Chamado buscarPorIdSeguro(Long id) {

        Long empresaId = getEmpresaId();

        Chamado chamado = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chamado não encontrado"));

        if (chamado.getEmpresa() == null) {
            throw new RuntimeException("Chamado sem empresa");
        }

        if (!Long.valueOf(chamado.getEmpresa().getId()).equals(empresaId)) {
            throw new RuntimeException("Acesso negado");
        }

        return chamado;
    }

    /* ==========================
       FINALIZAR
    ========================== */
    public Chamado finalizar(Long id) {

        Chamado chamado = buscarPorIdSeguro(id);

        chamado.setStatus("FINALIZADO");

        return repository.save(chamado);
    }

    /* ==========================
       ATUALIZAR
    ========================== */
    public Chamado atualizar(Long id, Chamado novo) {

        Chamado chamado = buscarPorIdSeguro(id);

        chamado.setDescricao(novo.getDescricao());
        chamado.setValor(novo.getValor());
        chamado.setPago(novo.getPago());

        return repository.save(chamado);
    }

    /* ==========================
       EXCLUIR (ADMIN ONLY depois você pode melhorar)
    ========================== */
    public void excluir(Long id) {
        repository.deleteById(id);
    }
}
