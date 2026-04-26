package com.sistema_os.OsFacil.service;

import com.sistema_os.OsFacil.model.Chamado;
import com.sistema_os.OsFacil.model.Cliente;
import com.sistema_os.OsFacil.repository.ChamadoRepository;
import com.sistema_os.OsFacil.repository.ClienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
@Service
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;
    private final ClienteRepository clienteRepository;

    public ChamadoService(ChamadoRepository chamadoRepository,
                          ClienteRepository clienteRepository) {
        this.chamadoRepository = chamadoRepository;
        this.clienteRepository = clienteRepository;
    }

    public Chamado salvar(Chamado chamado){

        if (chamado.getCliente() == null){
            throw new RuntimeException("Cliente obrigatorio");
        }

        Long clienteId = chamado.getCliente().getId();

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        chamado.setCliente(cliente);

        chamado.setData(LocalDate.now());
        chamado.setStatus("ABERTO");

        return chamadoRepository.save(chamado);
    }

    public Chamado finalizar(Long id){

        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chamado não encontrado"));

        // 🔥 REGRA ANTI-ERRO (evita 409)
        if ("FINALIZADO".equals(chamado.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Chamado já está finalizado"
            );
        }

        chamado.setStatus("FINALIZADO");

        return chamadoRepository.save(chamado);
    }

    public List<Chamado> listar(){
        return chamadoRepository.findAll();
    }
}