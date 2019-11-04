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
    
    public void ajustaLimite(double limite) {
        this.limite = limite;
    }
    
    public void aumentaSaldoDevedor(double valor) {
        this.saldo = this.saldo + valor;
    }
    
    public void reduzSaldoDevedor(double valor) {
        this.saldo = this.saldo - valor;
    }

}
