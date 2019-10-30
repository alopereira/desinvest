package com.totvs.tj.tcc.domain.conta;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class Conta {

    private ContaId id;
    
    private double saldo;

    private double limite;

    private Conta(ContaId id, double saldo, double limite) {
        super();
        this.id = id;
        this.saldo = saldo;
        //this.limite = this.calculaLimiteAbertura();
    }
    
    
    public void ajustaLimite(double limite) {
        this.limite = limite;
    }
    
    

}
