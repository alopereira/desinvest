package com.totvs.tj.tcc.app.movimentacao;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConsultaMovimentacaoPorEmpresalCommand {
    
    private String empresaId;
    
}
