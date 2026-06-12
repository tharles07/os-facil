package com.sistema_os.OsFacil.controller;

import com.sistema_os.OsFacil.dto.AuthRequest;
import com.sistema_os.OsFacil.model.Usuario;
import com.sistema_os.OsFacil.dto.AuthResponse;
import com.sistema_os.OsFacil.repository.UsuarioRepository;
import com.sistema_os.OsFacil.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(JwtUtil jwtUtil, UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;     
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {

        Usuario user = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (request.getPassword() == null || user.getPassword() == null) {
            throw new RuntimeException("Senha inválida");
        }

        if (user.getEmpresa() == null) {
            throw new RuntimeException("Usuário sem empresa vinculada");
        }

        boolean senhaCriptografada = user.getPassword().startsWith("$2");
        boolean senhaValida = senhaCriptografada
                ? passwordEncoder.matches(request.getPassword(), user.getPassword())
                : user.getPassword().equals(request.getPassword());

        if (!senhaValida) {
            throw new RuntimeException("Senha inválida");
        }

        if (!senhaCriptografada) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            repository.save(user);
        }

        String token = jwtUtil.gerarToken(user);

        return new AuthResponse(
                token,
                user.getPerfil(),
                user.getEmpresa().getId()
        );
    }
}
