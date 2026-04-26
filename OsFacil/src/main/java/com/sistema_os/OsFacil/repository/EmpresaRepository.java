package com.sistema_os.OsFacil.repository;

import com.sistema_os.OsFacil.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;


public interface  EmpresaRepository extends JpaRepository <Empresa, Long> {
}
