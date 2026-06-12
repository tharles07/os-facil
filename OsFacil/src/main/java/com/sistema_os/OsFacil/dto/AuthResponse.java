package com.sistema_os.OsFacil.dto;

public class AuthResponse {

    private String token;
    private String perfil;
    private Long empresaId;

    public AuthResponse(String token, String perfil, Long empresaId) {
        this.token = token;
        this.perfil = perfil;
        this.empresaId = empresaId;
    }

    public String getToken() {
        return token;
    }

    public String getPerfil() {
        return perfil;
    }

    public Long getEmpresaId() {
        return empresaId;
    }

}
