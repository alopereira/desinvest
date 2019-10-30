package com.totvs.tj.tcc.infra.persistence;

import com.totvs.tj.tcc.domain.responsavel.Responsavel;
import com.totvs.tj.tcc.domain.responsavel.ResponsavelId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.totvs.tj.tcc.domain.responsavel.ResponsavelRepository;

@Repository
public interface ResponsavelRepositoryJpa extends ResponsavelRepository, JpaRepository<Responsavel, ResponsavelId> {

}
