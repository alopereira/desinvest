package com.totvs.tj.tcc.app.movimentacao;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.totvs.tj.tcc.domain.conta.Conta;
import com.totvs.tj.tcc.domain.conta.ContaRepository;
import com.totvs.tj.tcc.domain.movimentacao.Movimentacao;
import com.totvs.tj.tcc.domain.movimentacao.MovimentacaoId;
import com.totvs.tj.tcc.domain.movimentacao.MovimentacaoRepository;

@Service
public class MovimentacaoApplicationService {
    
    private MovimentacaoRepository repository;
    private ContaRepository contaRepository;
    
    public MovimentacaoApplicationService(MovimentacaoRepository repository,
            ContaRepository contaRepository) {
        this.repository = repository;
        this.contaRepository = contaRepository;
    }
    
    @Transactional
    public MovimentacaoId handle(SolicitaCreditoEmergencialCommand cmd) {
        Conta conta = contaRepository.getOne(cmd.getContaId());
        Movimentacao movimentacao = conta.solicitaCreditoEmergencial(cmd.getValor());
        
        repository.save(movimentacao);
        contaRepository.save(conta);
        
        return movimentacao.getId();
    }
    
    
    
}
