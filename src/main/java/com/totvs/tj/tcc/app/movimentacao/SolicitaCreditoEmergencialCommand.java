package com.totvs.tj.tcc.app.movimentacao;

import com.totvs.tj.tcc.domain.conta.ContaId;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SolicitaCreditoEmergencialCommand {
    
    private ContaId contaId;
    private double valor;
    
}
