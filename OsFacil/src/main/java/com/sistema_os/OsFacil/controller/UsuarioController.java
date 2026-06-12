package com.sistema_os.OsFacil.controller;

import org.springframework.web.bind.annotation.*;

import com.sistema_os.OsFacil.model.Usuario;
import com.sistema_os.OsFacil.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Locale;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public Usuario salvar(@RequestBody Usuario usuario) {
        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            throw new RuntimeException("Senha obrigatória");
        }

        if (!usuario.getPassword().startsWith("$2")) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        if (usuario.getPerfil() == null || usuario.getPerfil().isBlank()) {
            usuario.setPerfil("USER");
        } else {
            usuario.setPerfil(usuario.getPerfil().toUpperCase(Locale.ROOT));
        }

        return repository.save(usuario);
    }
}
