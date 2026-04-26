package com.sistema_os.OsFacil.service;


import com.sistema_os.OsFacil.controller.ClienteController;
import com.sistema_os.OsFacil.model.Cliente;
import com.sistema_os.OsFacil.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    public final ClienteRepository repository;


    public ClienteService(ClienteRepository repository){
        this.repository = repository;
    }

    public Cliente salvar (Cliente cliente){
        return repository.save(cliente);
    }
    public List<Cliente> listar(){
        return repository.findAll();
    }
}
