package com.totvs.tj.tcc.domain.empresa;

public interface EmpresaRepository {

    void save(Empresa conta);
    
    Empresa getOne(EmpresaId id);
    
}
