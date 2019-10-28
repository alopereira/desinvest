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

import lombok.Builder;

@Service
@Builder
public class ContaApplicationService {
    
    private ContaRepository contaRepository;
    private EmpresaRepository empresaRepository;
    private ResponsavelRepository responsavelRepository;
    
    public ContaApplicationService(ContaRepository contaRepository,
            EmpresaRepository empresaRepository,
            ResponsavelRepository responsavelRepository) {
        this.contaRepository = contaRepository;
        this.empresaRepository = empresaRepository;
        this.responsavelRepository = responsavelRepository;
    }
    
    public ContaId handle(AbrirContaCommand cmd) {
        
        ContaId idConta = ContaId.generate();
        
        Conta conta = Conta.builder()
                .id(idConta)
                .empresa(getEmpresaById(cmd.getEmpresaId()))
                .responsavel(getResponsavelById(cmd.getResponsavelId()))
            .build();
        
        contaRepository.save(conta);
        
        return idConta; 
    }
    
    protected Empresa getEmpresaById(EmpresaId empresaId) {
        return empresaRepository.getOne(empresaId);
    }
    
    protected Responsavel getResponsavelById(ResponsavelId responsavelId) {
        return responsavelRepository.getOne(responsavelId);
    }
    
    public void handle(SuspenderContaCommand cmd) {
        
        Conta conta = contaRepository.getOne(cmd.getConta());
        
        conta.suspender();
        
        contaRepository.save(conta);
    }
    
}
