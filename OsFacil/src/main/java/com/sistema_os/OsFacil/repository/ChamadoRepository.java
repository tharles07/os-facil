package com.sistema_os.OsFacil.repository;

import com.sistema_os.OsFacil.model.Chamado;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;



public interface ChamadoRepository extends JpaRepository<Chamado, Long> {
List<Chamado> findByEmpresaId(Long empresaId);
}