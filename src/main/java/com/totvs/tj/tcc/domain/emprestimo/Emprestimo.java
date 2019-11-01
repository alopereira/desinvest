package com.totvs.tj.tcc.domain.emprestimo;

import com.totvs.tj.tcc.domain.empresa.EmpresaId;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class Emprestimo {
    
    private EmprestimoId id;
    
    private EmpresaId empresaId;
    
    private double valor;
    
    private EmprestimoSituacao situacao;
    
    private EmprestimoMotivoRecusa motivoRecusa;

    public void devolver(double valorDevolvido) {
        
        this.valor = this.valor - valorDevolvido;
        
        if (this.valor == 0) {
            this.situacao = EmprestimoSituacao.QUITADO;
        }
        
    }

    public void aprovar() {
        this.situacao = EmprestimoSituacao.APROVADO;
    }
        
}
