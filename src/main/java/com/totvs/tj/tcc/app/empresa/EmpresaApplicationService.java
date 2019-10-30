package com.totvs.tj.tcc.app.empresa;

import org.springframework.stereotype.Service;

import com.totvs.tj.tcc.domain.empresa.Empresa;
import com.totvs.tj.tcc.domain.empresa.EmpresaId;
import com.totvs.tj.tcc.domain.empresa.EmpresaRepository;

@Service
public class EmpresaApplicationService {
    
    private EmpresaRepository repository;
    
    public EmpresaApplicationService(EmpresaRepository repository) {
        this.repository = repository;
    }
    
    public EmpresaId handle(SalvaEmpresaCommand cmd) {
        
        EmpresaId idEmpresa = EmpresaId.generate();
        
        Empresa empresa = Empresa.builder()
                .id(idEmpresa)
                .valor(cmd.getValor())
                .qtdFuncionarios(cmd.getQtdFuncionarios())
            .build();
        
        repository.save(empresa);
        
        return idEmpresa; 
    }
    
    public void handle(SuspenderEmpresaCommand cmd) {

        Empresa empresa = repository.getOne(cmd.getEmpresa());

        empresa.suspender();

        repository.save(empresa);
    }
    
}
