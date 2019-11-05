package com.totvs.tj.tcc.domain.conta;

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
public class Conta {
    
    @Id
    private String id;
    
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
    
    public static String generate() {
        return UUID.randomUUID().toString();
    }

}
