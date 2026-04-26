package com.sistema_os.OsFacil.service;


import com.sistema_os.OsFacil.model.Empresa;
import com.sistema_os.OsFacil.repository.EmpresaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresaService {

    private final EmpresaRepository repository;

    public EmpresaService(EmpresaRepository repository){
        this.repository = repository;

    }

    public Empresa  salvar(Empresa empresa){
        return  repository.save(empresa);
    }

    public List<Empresa> listar(){
        return repository.findAll();
    }
}
