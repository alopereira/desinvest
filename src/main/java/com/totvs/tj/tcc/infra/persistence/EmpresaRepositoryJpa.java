package com.totvs.tj.tcc.infra.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.totvs.tj.tcc.domain.empresa.Empresa;
import com.totvs.tj.tcc.domain.empresa.EmpresaId;
import com.totvs.tj.tcc.domain.empresa.EmpresaRepository;

@Repository
public interface EmpresaRepositoryJpa extends EmpresaRepository, JpaRepository<Empresa, EmpresaId> {

}
