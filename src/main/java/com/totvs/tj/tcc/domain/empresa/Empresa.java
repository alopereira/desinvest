package com.totvs.tj.tcc.domain.empresa;

import static lombok.AccessLevel.PRIVATE;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.totvs.tj.tcc.domain.conta.Conta;
import com.totvs.tj.tcc.domain.movimentacao.Movimentacao;
import com.totvs.tj.tcc.domain.movimentacao.MovimentacaoId;
import com.totvs.tj.tcc.domain.movimentacao.MovimentacaoSituacao;
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

    @NotBlank
    @NotNull
    private EmpresaId id;

    @NotBlank
    @NotNull
    private ResponsavelId responsaveId;

    @NotNull
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

    public Movimentacao solicitaCreditoEmergencial(double novoLimite) {

        MovimentacaoSituacao situacao = MovimentacaoSituacao.APROVADO;
        //MovimentacaoMotivoRecusa motivoRecusa = null;

        MovimentacaoId idMovimentacao = MovimentacaoId.generate();

        if (this.permiteSolicitarCreditoEmergencial() == false) {
            situacao = MovimentacaoSituacao.REPROVADO;
            //motivoRecusa = MovimentacaoMotivoRecusa.LIMITE_JA_SOLICITADO;
        } else if (novoLimite > this.calculaLimiteMaximoParaCreditoEmergencial()) {
            situacao = MovimentacaoSituacao.AGUARDANDO_APROVACAO;
            //motivoRecusa = MovimentacaoMotivoRecusa.LIMITE_ACIMA_MAXIMO;
        } else {
            this.conta.ajustaLimite(novoLimite);
        }

        this.incrementaSolicitacaoAumentoCredito();

        return Movimentacao.builder()
                .id(idMovimentacao)
                .contaId(conta.getId())
                .situacao(situacao)
                .build();
    }
    
    public double calculaLimiteMaximoParaCreditoEmergencial() {
        double limiteMaximo = this.conta.getLimite() + (this.conta.getLimite() * 0.5);
        return limiteMaximo;
    }
    
    

}
