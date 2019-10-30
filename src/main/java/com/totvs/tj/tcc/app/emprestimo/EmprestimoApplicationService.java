package com.totvs.tj.tcc.app.emprestimo;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.totvs.tj.tcc.domain.empresa.Empresa;
import com.totvs.tj.tcc.domain.empresa.EmpresaRepository;
import com.totvs.tj.tcc.domain.emprestimo.Emprestimo;
import com.totvs.tj.tcc.domain.emprestimo.EmprestimoId;
import com.totvs.tj.tcc.domain.emprestimo.EmprestimoRepository;

@Service
public class EmprestimoApplicationService {
    
    private EmprestimoRepository emprestimoRepository;
    private EmpresaRepository empresaRepository;
    
    public EmprestimoApplicationService(EmprestimoRepository emprestimoRepository,
            EmpresaRepository empresaRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.empresaRepository = empresaRepository;
    }
    
    @Transactional
    public double handle(SolicitaCreditoEmergencialCommand cmd) {
        Empresa empresa = this.empresaRepository.getOne(cmd.getEmpresaId());
        empresa.solicitaLimiteEmergencial(cmd.getValor());
        
        empresaRepository.save(empresa);
        
        return emprestimo.getId();
    }
    
    @Transactional
    public EmprestimoId handle(SolicitaEmprestimoCommand cmd) {
        Empresa empresa = this.empresaRepository.getOne(cmd.getEmpresaId());
        
        Emprestimo emprestimo = empresa.criaSolicitacaoEmprestimo(cmd.getValor());
        
        emprestimoRepository.save(emprestimo);
        
        return emprestimo.getId();
    }
    
    
    
}
