package com.sistema_os.OsFacil.repository;

import com.sistema_os.OsFacil.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByEmpresaId(Long empresaId);
}
