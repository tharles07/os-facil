package com.sistema_os.OsFacil.repository;

import com.sistema_os.OsFacil.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}