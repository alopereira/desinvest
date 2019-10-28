package com.totvs.tj.tcc.domain.conta;

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
    
    private MovimentacaoMotivo motivo;
    
}
