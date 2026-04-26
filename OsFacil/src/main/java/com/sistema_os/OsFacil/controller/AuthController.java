package com.sistema_os.OsFacil.controller;

import com.sistema_os.OsFacil.dto.AuthRequest;
import com.sistema_os.OsFacil.model.Usuario;
import com.sistema_os.OsFacil.repository.UsuarioRepository;
import com.sistema_os.OsFacil.security.JwtUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UsuarioRepository repository;

    public AuthController(JwtUtil jwtUtil, UsuarioRepository repository) {
        this.jwtUtil = jwtUtil;
        this.repository = repository;
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest request) {

        // 🔍 busca usuário no banco
        Usuario user = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // 🔐 valida senha
        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Senha inválida");
        }

        // 🎟 gera token
        return jwtUtil.gerarToken(user.getUsername());
    }
}