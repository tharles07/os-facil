package com.sistema_os.OsFacil.service;

import com.sistema_os.OsFacil.model.Usuario;
import com.sistema_os.OsFacil.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private  final UsuarioRepository repository;

    public  UsuarioService(UsuarioRepository repository){
        this.repository =repository;
    }

    public Usuario buscarPorUsername(String username){
        return  repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario não emcontrado"));

    }

}
