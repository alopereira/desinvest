package com.totvs.tj.tcc.domain.conta;

import static com.totvs.tj.tcc.domain.conta.Conta.Situacao.ABERTO;
import static com.totvs.tj.tcc.domain.conta.Conta.Situacao.SUSPENSO;

import com.totvs.tj.tcc.domain.empresa.Empresa;
import com.totvs.tj.tcc.domain.empresa.EmpresaId;
import com.totvs.tj.tcc.domain.responsavel.Responsavel;
import com.totvs.tj.tcc.domain.responsavel.ResponsavelId;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class Conta {

    private ContaId id;
    
    private Empresa empresa;
    
    private Responsavel responsavel;

    private Situacao situacao;
    
    private double saldo;
    
    private double limite;
    

    private Conta(ContaId id, Empresa empresa, Responsavel responsavel, Situacao situacao, double saldo, double limite) {
        super();
        this.id = id;
        this.empresa = empresa;
        this.responsavel = responsavel;
        this.situacao = situacao;
        this.saldo = saldo;
        this.limite = this.calculaLimiteAbertura();
        
    }
    
    public EmpresaId getEmpresaId() {
        return this.empresa.getId();
    }
    
    public ResponsavelId getResponsavelId() {
        return this.responsavel.getId();
    }
    
    
    public void suspender() {
        situacao = SUSPENSO;
    }

    public boolean isDisponivel() {
        return ABERTO.equals(situacao);
    }
    
    static enum Situacao {

        ABERTO, SUSPENSO;
        
    }
    
    protected double calculaLimiteAbertura() {
        
        double xx = this.empresa.getValor() * 0.1;
        double yy = this.empresa.getQtdFuncionarios() * 10;
        
        double zz = xx + yy;
        
        if (zz > 15000) {
            zz = 15000;
        }
        
        return zz;
    }
    
}
