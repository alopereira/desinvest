package com.totvs.tj.tcc.domain.movimentacao;

public interface MovimentacaoRepository {

    void save(Movimentacao movimentacao);
    
    Movimentacao getOne(MovimentacaoId id);
    
}
