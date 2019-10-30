package com.totvs.tj.tcc.domain.empresa;

import static lombok.AccessLevel.PRIVATE;

import com.totvs.tj.tcc.domain.conta.Conta;
import com.totvs.tj.tcc.domain.conta.ContaId;
import com.totvs.tj.tcc.domain.emprestimo.Emprestimo;
import com.totvs.tj.tcc.domain.emprestimo.EmprestimoId;
import com.totvs.tj.tcc.domain.emprestimo.EmprestimoSituacao;
import com.totvs.tj.tcc.domain.responsavel.ResponsavelId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor(access = PRIVATE)
public class Empresa {

    private EmpresaId id;

    private ResponsavelId responsaveId;

    private Conta conta;

    private double valor;

    private double qtdFuncionarios;

    private String cnpj;

    private int solicitacaoAumentoCredito;

    private Situacao situacao;

    protected double calculaLimiteParaAberturaConta() {

        double valorDaEmpresaPerc = this.getValor() * 0.1;
        double qtdFuncionariosEmpresa = this.getQtdFuncionarios() * 10;

        double valorLimiteConta = valorDaEmpresaPerc + qtdFuncionariosEmpresa;

        if (valorLimiteConta > 15000) {
            valorLimiteConta = 15000;
        }

        return valorLimiteConta;
    }

    public boolean permiteSolicitarCreditoEmergencial() {
        return this.solicitacaoAumentoCredito == 0;
    }

    public void incrementaSolicitacaoAumentoCredito() {
        this.solicitacaoAumentoCredito = this.solicitacaoAumentoCredito + 1;
    }

    public void suspender() {
        situacao = Situacao.SUSPENSO;
    }

    public boolean isDisponivel() {
        return Situacao.ABERTO.equals(situacao);
    }

    static enum Situacao {
        ABERTO,
        SUSPENSO;
    }

    public Emprestimo solicitaCreditoEmergencial(double novoLimite) {

        EmprestimoSituacao situacao = EmprestimoSituacao.APROVADO;
        //MovimentacaoMotivoRecusa motivoRecusa = null;

        EmprestimoId idMovimentacao = EmprestimoId.generate();

        if (this.permiteSolicitarCreditoEmergencial() == false) {
            situacao = EmprestimoSituacao.REPROVADO;
            //motivoRecusa = MovimentacaoMotivoRecusa.LIMITE_JA_SOLICITADO;
        } else if (novoLimite > this.calculaLimiteMaximoParaCreditoEmergencial()) {
            situacao = EmprestimoSituacao.AGUARDANDO_APROVACAO;
            //motivoRecusa = MovimentacaoMotivoRecusa.LIMITE_ACIMA_MAXIMO;
        } else {
            this.conta.ajustaLimite(novoLimite);
        }

        this.incrementaSolicitacaoAumentoCredito();

        return Emprestimo.builder()
                .id(idMovimentacao)
                .contaId(conta.getId())
                .situacao(situacao)
                .build();
    }

    public double calculaLimiteMaximoParaCreditoEmergencial() {
        double limiteMaximo = this.conta.getLimite() + (this.conta.getLimite() * 0.5);
        return limiteMaximo;
    }

    public void abrirConta() {

        Conta conta = Conta.builder()
                .id(ContaId.generate())
                .saldo(0)
                .limite(this.calculaLimiteParaAberturaConta())
                .build();

        this.conta = conta;
    }

    public ContaId getContaId() {
        return this.conta.getId();
    }

}
