package com.totvs.tj.tcc.domain.conta;

import static com.totvs.tj.tcc.domain.conta.Conta.Situacao.ABERTO;
import static com.totvs.tj.tcc.domain.conta.Conta.Situacao.SUSPENSO;
import static lombok.AccessLevel.PRIVATE;

import com.totvs.tj.tcc.domain.empresa.EmpresaId;
import com.totvs.tj.tcc.domain.responsavel.ResponsavelId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor(access = PRIVATE)
public class Conta {

    private ContaId id;
    
    private EmpresaId empresa;
    
    private ResponsavelId responsavel;

    private Situacao situacao;
    
    private double saldo;
    
    private double limite;
    
    public void suspender() {
        situacao = SUSPENSO;
    }

    public boolean isDisponivel() {
        return ABERTO.equals(situacao);
    }
    
    static enum Situacao {

        ABERTO, SUSPENSO;
        
    }
}
