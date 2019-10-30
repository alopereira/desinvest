package com.totvs.tj.tcc.infra.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.totvs.tj.tcc.domain.emprestimo.Emprestimo;
import com.totvs.tj.tcc.domain.emprestimo.EmprestimoId;
import com.totvs.tj.tcc.domain.emprestimo.EmprestimoRepository;

@Repository
public interface EmprestimoRepositoryJpa extends EmprestimoRepository, JpaRepository<Emprestimo, EmprestimoId> {

}
