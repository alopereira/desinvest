package com.totvs.tj.tcc.domain.responsavel;

public interface ResponsavelRepository {

    void save(Responsavel conta);
    
    Responsavel getOne(ResponsavelId id);
    
}
