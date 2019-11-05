package com.totvs.tj.tcc.domain.movimentacao;

import java.util.Map;

public interface MovimentacaoRepository {

    void save(Movimentacao movimentacao);
    
    Movimentacao getOne(String id);
    
    Map<String, Movimentacao> getMovimentacaoPorEmpresa(String empresaId);
    
}
