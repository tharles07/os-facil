package com.sistema_os.OsFacil.controller;


import com.sistema_os.OsFacil.model.Empresa;
import com.sistema_os.OsFacil.repository.EmpresaRepository;
import com.sistema_os.OsFacil.service.EmpresaService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {


    private final EmpresaService service;

    public  EmpresaController(EmpresaService service){
        this.service =service;

    }

    @PostMapping
    public Empresa criar(@RequestBody @Valid Empresa empresa){
        return service.salvar(empresa);
    }

    @GetMapping
    public List<Empresa> listar(){
        return  service.listar();
    }

}
