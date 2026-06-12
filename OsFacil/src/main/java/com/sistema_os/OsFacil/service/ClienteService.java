package com.sistema_os.OsFacil.service;


import com.sistema_os.OsFacil.model.Cliente;
import com.sistema_os.OsFacil.model.Empresa;
import com.sistema_os.OsFacil.repository.ClienteRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    private Long getEmpresaId() {
        Object details = SecurityContextHolder.getContext()
                .getAuthentication()
                .getDetails();

        if (details == null) {
            throw new RuntimeException("Empresa não identificada");
        }

        return ((Number) details).longValue();
    }

    public Cliente salvar(Cliente cliente) {

        if (cliente.getNome() == null || cliente.getNome().isBlank()) {
            throw new RuntimeException("Nome do cliente obrigatório");
        }

        if (cliente.getTelefone() == null || cliente.getTelefone().isBlank()) {
            throw new RuntimeException("Telefone obrigatório");
        }

        Empresa empresa = new Empresa();
        empresa.setId(getEmpresaId());
        cliente.setEmpresa(empresa);

        return repository.save(cliente);
    }

    public List<Cliente> listar() {
        return repository.findByEmpresaId(getEmpresaId());
    }
}
