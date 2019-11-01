package com.totvs.tj.tcc.domain.movimentacao;

import java.util.Map;

import com.totvs.tj.tcc.domain.empresa.EmpresaId;

public interface MovimentacaoRepository {

    void save(Movimentacao movimentacao);
    
    Movimentacao getOne(MovimentacaoId id);
    
    Map<MovimentacaoId, Movimentacao> getMovimentacaoPorEmpresa(EmpresaId empresaId);
    
}
