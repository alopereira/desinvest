package com.totvs.tj.tcc.domain.emprestimo;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.totvs.tj.tcc.domain.empresa.EmpresaId;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@Entity
public class Emprestimo {

    private EmprestimoId id;

    private EmpresaId empresaId;

    private double valor;

    private EmprestimoSituacao situacao;

    private EmprestimoMotivoRecusa motivoRecusa;
    
    @Id
    public String getIdValue() {
        return this.id.getValue();
    }

    public void devolver(double valorDevolvido) {
        this.valor = this.valor - valorDevolvido;

        if (this.valor == 0) {
            this.situacao = EmprestimoSituacao.QUITADO;
        }
    }

    public void aprovar() {
        this.situacao = EmprestimoSituacao.APROVADO;
    }

    public void reprovar() {
        this.situacao = EmprestimoSituacao.REPROVADO;
    }

    public void aguardarLimiteEmergencial() {
        this.situacao = EmprestimoSituacao.AGUARDANDO_LIMITE_EMERGENCIAL;
    }
    
    

}
