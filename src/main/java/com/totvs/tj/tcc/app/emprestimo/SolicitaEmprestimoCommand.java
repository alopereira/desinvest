package com.totvs.tj.tcc.app.emprestimo;

import com.totvs.tj.tcc.domain.empresa.EmpresaId;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SolicitaEmprestimoCommand {
    
    private EmpresaId empresaId;
    
    private double valor;
    
}
