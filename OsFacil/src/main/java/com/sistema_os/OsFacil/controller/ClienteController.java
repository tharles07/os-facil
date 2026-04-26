package com.sistema_os.OsFacil.controller;


import com.sistema_os.OsFacil.model.Cliente;
import com.sistema_os.OsFacil.service.ClienteService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private  final ClienteService service;

    public ClienteController(ClienteService service){
        this.service = service;

    }
    @PostMapping
    public Cliente criar (@RequestBody Cliente cliente ){
        return  service.salvar(cliente);
    }

    @GetMapping
    public List<Cliente> listar(){
        return  service.listar();
    }

}
