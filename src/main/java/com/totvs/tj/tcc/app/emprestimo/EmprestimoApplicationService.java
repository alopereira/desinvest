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
    
    private EmprestimoRepository repository;
    private EmpresaRepository empresaRepository;
    
    public EmprestimoApplicationService(EmprestimoRepository repository,
            EmpresaRepository empresaRepository) {
        this.repository = repository;
        this.empresaRepository = empresaRepository;
    }
    
    @Transactional
    public EmprestimoId handle(SolicitaCreditoEmergencialCommand cmd) {
        Empresa empresa = this.empresaRepository.getOne(cmd.getEmpresaId());
        Emprestimo emprestimo = empresa.solicitaCreditoEmergencial(cmd.getValor());
        
        repository.save(emprestimo);
        
        return emprestimo.getId();
    }
    
    
    
}
