package com.totvs.tj.tcc.app.emprestimo;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.totvs.tj.tcc.domain.conta.Conta;
import com.totvs.tj.tcc.domain.empresa.Empresa;
import com.totvs.tj.tcc.domain.empresa.EmpresaRepository;
import com.totvs.tj.tcc.domain.emprestimo.Emprestimo;
import com.totvs.tj.tcc.domain.emprestimo.EmprestimoRepository;
import com.totvs.tj.tcc.domain.movimentacao.Movimentacao;
import com.totvs.tj.tcc.domain.movimentacao.Movimentacao.TipoMovimentacao;
import com.totvs.tj.tcc.domain.movimentacao.MovimentacaoRepository;

import lombok.Builder;

@Service
@Builder
public class EmprestimoApplicationService {
    
    private EmprestimoRepository emprestimoRepository;
    private EmpresaRepository empresaRepository;
    private MovimentacaoRepository movimentacaoRepository;
    
    public EmprestimoApplicationService(EmprestimoRepository emprestimoRepository,
            EmpresaRepository empresaRepository,
            MovimentacaoRepository movimentacaoRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.empresaRepository = empresaRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }
    
    @Transactional
    public String handle(SolicitaEmprestimoCommand cmd) {
        Empresa empresa = this.empresaRepository.getOne(cmd.getEmpresaId());
        
        Emprestimo emprestimo = empresa.criaSolicitacaoEmprestimo(cmd.getValor());
        
        emprestimoRepository.save(emprestimo);
        this.movimentacaoRepository.save(Movimentacao.builder()
                .id(Movimentacao.generate())
                .contaId(empresa.getContaId())
                .empresaId(empresa.getId())
                .dataHora(LocalDateTime.now())
                .valor(cmd.getValor())
                .tipo(TipoMovimentacao.SOLICITA_LIMITE_EMERGENCIAL)
                .build());
        
        return emprestimo.getId();
    }
    
    @Transactional
    public Movimentacao handle(DevolverEmprestimoCommand cmd) {
        
        Emprestimo emprestimo = this.emprestimoRepository.getOne(cmd.getEmprestimoId());        
        emprestimo.devolver(cmd.getValor());        
        emprestimoRepository.save(emprestimo);
        
        String empresaId = emprestimo.getEmpresaId();
        Empresa empresa = empresaRepository.getOne(empresaId);
        Conta conta = empresa.getConta();
        conta.reduzSaldoDevedor(cmd.getValor());
        empresaRepository.save(empresa);
        
        Movimentacao movimentacao = Movimentacao.builder()
                .id(Movimentacao.generate())
                .contaId(empresa.getContaId())
                .empresaId(empresa.getId())
                .dataHora(LocalDateTime.now())
                .valor(cmd.getValor())
                .tipo(TipoMovimentacao.DEVOLUCAO)
                .build();  
        
        this.movimentacaoRepository.save(movimentacao);
        
        return movimentacao;        
    }

    public Movimentacao handle(AprovarSolicitacaoEmprestimoCommand cmdAprovar) {

        Emprestimo emprestimo = this.emprestimoRepository.getOne(cmdAprovar.getEmprestimoId());        
        emprestimo.aprovar();        
        emprestimoRepository.save(emprestimo);
        
        String empresaId = emprestimo.getEmpresaId();
        Empresa empresa = empresaRepository.getOne(empresaId);
        
        Movimentacao movimentacao = Movimentacao.builder()
                .id(Movimentacao.generate())
                .contaId(empresa.getContaId())
                .empresaId(empresa.getId())
                .dataHora(LocalDateTime.now())
                .valor(emprestimo.getValor())
                .tipo(TipoMovimentacao.APROVAR_SOLICITACAO_EMPRESTIMO)
                .build();  
        
        this.movimentacaoRepository.save(movimentacao);
        
        return movimentacao;          
    }

    public Movimentacao handle(ReprovarSolicitacaoEmprestimoCommand cmdReprovar) {
        
        Emprestimo emprestimo = this.emprestimoRepository.getOne(cmdReprovar.getEmprestimoId());     
        
        String empresaId = emprestimo.getEmpresaId();
        Empresa empresa = empresaRepository.getOne(empresaId);
        
        double quantidadeSolicitacoesLimite = empresa.getSolicitacaoAumentoCredito();
        TipoMovimentacao tipo;
        
        if (quantidadeSolicitacoesLimite > 0) {
            emprestimo.reprovar(); 
            tipo = TipoMovimentacao.REPROVAR_EMPRESTIMO;
        } else {
            emprestimo.aguardarLimiteEmergencial();
            tipo = TipoMovimentacao.SOLICITA_LIMITE_EMERGENCIAL;
        }        
               
        emprestimoRepository.save(emprestimo);
        
        Movimentacao movimentacao = Movimentacao.builder()
                .id(Movimentacao.generate())
                .contaId(empresa.getContaId())
                .empresaId(empresa.getId())
                .dataHora(LocalDateTime.now())
                .valor(emprestimo.getValor())
                .tipo(tipo)
                .build();  
        
        this.movimentacaoRepository.save(movimentacao);
        
        return movimentacao;          
    }
    
    
}
