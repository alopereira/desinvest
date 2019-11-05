package com.totvs.tj.tcc.domain.emprestimo;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@Entity
public class Emprestimo {

    @Id
    private String id;

    private String empresaId;

    private double valor;

    private EmprestimoSituacao situacao;

    private EmprestimoMotivoRecusa motivoRecusa;
    
    public static String generate() {
        return UUID.randomUUID().toString();
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
