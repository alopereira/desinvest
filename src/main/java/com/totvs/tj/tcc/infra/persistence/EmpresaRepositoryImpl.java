package com.totvs.tj.tcc.infra.persistence;

import org.springframework.stereotype.Service;

import com.totvs.tj.tcc.domain.empresa.Empresa;
import com.totvs.tj.tcc.domain.empresa.EmpresaRepository;

@Service
public class EmpresaRepositoryImpl implements EmpresaRepository {

   
    @Override
    public void save(Empresa empresa) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Empresa getOne(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    
}
