package com.totvs.tj.tcc.app.conta;

import org.springframework.stereotype.Service;

import com.totvs.tj.tcc.domain.conta.Conta;
import com.totvs.tj.tcc.domain.conta.ContaId;
import com.totvs.tj.tcc.domain.conta.ContaRepository;
import com.totvs.tj.tcc.domain.empresa.Empresa;
import com.totvs.tj.tcc.domain.empresa.EmpresaId;
import com.totvs.tj.tcc.domain.empresa.EmpresaRepository;
import com.totvs.tj.tcc.domain.responsavel.Responsavel;
import com.totvs.tj.tcc.domain.responsavel.ResponsavelId;
import com.totvs.tj.tcc.domain.responsavel.ResponsavelRepository;

@Service
public class ContaApplicationService {
    
    private ContaRepository repository;
    private EmpresaRepository empresaRepository;
    private ResponsavelRepository responsavelRepository;
    
    public ContaApplicationService(ContaRepository repository) {
        this.repository = repository;
    }
    
    public ContaId handle(AbrirContaCommand cmd) {
        
        ContaId idConta = ContaId.generate();
        
        Conta conta = Conta.builder()
                .id(idConta)
                .empresa(getEmpresaById(cmd.getEmpresaId()))
                .responsavel(getResponsavelById(cmd.getResponsavelId()))
            .build();
        
        repository.save(conta);
        
        return idConta; 
    }
    
    protected Empresa getEmpresaById(EmpresaId empresaId) {
        return empresaRepository.getOne(empresaId);
    }
    
    protected Responsavel getResponsavelById(ResponsavelId responsavelId) {
        return responsavelRepository.getOne(responsavelId);
    }
    
    public void handle(SuspenderContaCommand cmd) {
        
        Conta conta = repository.getOne(cmd.getConta());
        
        conta.suspender();
        
        repository.save(conta);
    }
    
}
