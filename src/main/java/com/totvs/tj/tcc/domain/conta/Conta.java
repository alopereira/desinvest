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
    
    private int solicitacaoAumentoCredito;

    private Conta(ContaId id, Empresa empresa, Responsavel responsavel, Situacao situacao, double saldo, double limite, int solicitacaoAumentoCredito) {
        super();
        this.id = id;
        this.empresa = empresa;
        this.responsavel = responsavel;
        this.situacao = situacao;
        this.saldo = saldo;
        this.solicitacaoAumentoCredito = solicitacaoAumentoCredito;
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

        ABERTO,
        SUSPENSO;

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

    public Movimentacao solicitarCreditoEmergencial(double valor) {

        Movimentacao movimentacao = null;
        double limiteMaximo = this.calculaLimiteMaximoParaCreditoEmergencial();
        
        if (this.permiteSolicitarCreditoEmergencial() == false) {
            movimentacao = Movimentacao.builder()
                    .id(MovimentacaoId.generate())
                    .contaId(this.id)
                    .situacao(MovimentacaoSituacao.REPROVADO)
                    .motivoRecusa(MovimentacaoMotivoRecusa.LIMITE_JA_SOLICITADO)
                    .build();
            
            return movimentacao;
        } 
        
        if (valor > limiteMaximo) {
            movimentacao = Movimentacao.builder()
                    .id(MovimentacaoId.generate())
                    .contaId(this.id)
                    .situacao(MovimentacaoSituacao.AGUARDANDO_APROVACAO)
                    .motivoRecusa(MovimentacaoMotivoRecusa.LIMITE_ACIMA_MAXIMO)
                    .build();
            
            return movimentacao;
        }
        
        movimentacao = Movimentacao.builder()
                .id(MovimentacaoId.generate())
                .contaId(this.id)
                .situacao(MovimentacaoSituacao.APROVADO)
                .build();
        
        this.limite = valor;
        this.solicitacaoAumentoCredito = this.solicitacaoAumentoCredito + 1;
               
        return movimentacao;
    }

    private double calculaLimiteMaximoParaCreditoEmergencial() {

        double limiteMaximo = this.limite + (this.limite * 0.5);

        return limiteMaximo;
    }
    
    private boolean permiteSolicitarCreditoEmergencial() {
        
        return this.solicitacaoAumentoCredito == 0;
        
    }
    
}
