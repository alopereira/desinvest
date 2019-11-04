package com.totvs.tj.tcc.domain.conta;

import static com.totvs.tj.tcc.domain.conta.Conta.Situacao.ABERTO;
import static com.totvs.tj.tcc.domain.conta.Conta.Situacao.SUSPENSO;

import com.totvs.tj.tcc.domain.empresa.Empresa;
import com.totvs.tj.tcc.domain.empresa.EmpresaId;
import com.totvs.tj.tcc.domain.movimentacao.Movimentacao;
import com.totvs.tj.tcc.domain.movimentacao.MovimentacaoId;
import com.totvs.tj.tcc.domain.movimentacao.MovimentacaoMotivoRecusa;
import com.totvs.tj.tcc.domain.movimentacao.MovimentacaoSituacao;
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
