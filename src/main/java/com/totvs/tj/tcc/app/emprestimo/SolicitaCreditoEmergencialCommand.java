package com.totvs.tj.tcc.app.emprestimo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SolicitaCreditoEmergencialCommand {
    
    private String empresaId;
    private double valor;
    
}
