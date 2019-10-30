package com.totvs.tj.tcc.domain.emprestimo;

import com.totvs.tj.tcc.domain.conta.ContaId;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class Emprestimo {
    
    private EmprestimoId id;
    
    private ContaId contaId;
    
    private EmprestimoSituacao situacao;
    
    private EmprestimoMotivoRecusa motivoRecusa;
    
}
