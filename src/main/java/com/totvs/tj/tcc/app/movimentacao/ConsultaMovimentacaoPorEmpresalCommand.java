package com.totvs.tj.tcc.app.movimentacao;

import com.totvs.tj.tcc.domain.empresa.EmpresaId;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConsultaMovimentacaoPorEmpresalCommand {
    
    private EmpresaId empresaId;
    
}
