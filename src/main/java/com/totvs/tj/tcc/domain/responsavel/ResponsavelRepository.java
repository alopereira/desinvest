package com.totvs.tj.tcc.domain.responsavel;

public interface ResponsavelRepository {

    void save(Responsavel responsavel);
    
    Responsavel getOne(String id);
    
}
