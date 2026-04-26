package com.sistema_os.OsFacil.repository;

import com.sistema_os.OsFacil.model.Chamado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChamadoRepository extends JpaRepository<Chamado, Long> {
}