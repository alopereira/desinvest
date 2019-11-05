package com.totvs.tj.tcc.domain.empresa;

public interface EmpresaRepository {

    void save(Empresa empresa);
    
    Empresa getOne(String id);
    
}
