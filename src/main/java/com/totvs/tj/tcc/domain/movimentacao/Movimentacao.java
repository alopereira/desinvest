package com.totvs.tj.tcc.domain.movimentacao;

import com.totvs.tj.tcc.domain.conta.ContaId;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class Movimentacao {
    
    private MovimentacaoId id;
    
    private ContaId contaId;
    
    private MovimentacaoSituacao situacao;
    
    private MovimentacaoMotivoRecusa motivoRecusa;
    
}
