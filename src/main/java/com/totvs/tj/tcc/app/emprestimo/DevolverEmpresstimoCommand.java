package com.totvs.tj.tcc.app.emprestimo;

import com.totvs.tj.tcc.domain.emprestimo.EmprestimoId;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DevolverEmpresstimoCommand {
    
    private EmprestimoId emprestimoId;
    
    private double valor;

}
