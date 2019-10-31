package com.totvs.tj.tcc.app.empresa;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.totvs.tj.tcc.app.emprestimo.SolicitaCreditoEmergencialCommand;
import com.totvs.tj.tcc.domain.empresa.Empresa;
import com.totvs.tj.tcc.domain.empresa.EmpresaId;
import com.totvs.tj.tcc.domain.empresa.EmpresaRepository;

import lombok.Builder;

@Service
@Builder
public class EmpresaApplicationService {
    
    private EmpresaRepository empresaRepository;
    
    public EmpresaApplicationService(EmpresaRepository repository) {
        this.empresaRepository = repository;
    }
    
    public EmpresaId handle(SalvaEmpresaCommand cmd) {
        
        EmpresaId idEmpresa = EmpresaId.generate();
        
        Empresa empresa = Empresa.builder()
                .id(idEmpresa)
                .valor(cmd.getValor())
                .qtdFuncionarios(cmd.getQtdFuncionarios())
            .build();
        
        empresaRepository.save(empresa);
        
        return idEmpresa; 
    }
    
    public void handle(SuspenderEmpresaCommand cmd) {

        Empresa empresa = empresaRepository.getOne(cmd.getEmpresa());

        empresa.suspender();

        empresaRepository.save(empresa);
    }
    
    @Transactional
    public double handle(SolicitaCreditoEmergencialCommand cmd) {
        Empresa empresa = this.empresaRepository.getOne(cmd.getEmpresaId());
        empresa.solicitaLimiteEmergencial(cmd.getValor());
        
        empresaRepository.save(empresa);
        
        return empresa.getLimiteConta();
    }
    
}
