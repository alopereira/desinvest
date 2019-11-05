package com.totvs.tj.tcc.domain.empresa;

import static lombok.AccessLevel.PRIVATE;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.totvs.tj.tcc.domain.conta.Conta;
import com.totvs.tj.tcc.domain.emprestimo.Emprestimo;
import com.totvs.tj.tcc.domain.emprestimo.EmprestimoSituacao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor(access = PRIVATE)
@Entity
@Table(name = "empresa")
public class Empresa {

    @Id
    private String id;

    private String responsaveId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "conta_id", referencedColumnName = "id")
    private Conta conta;

    private double valor;

    private double qtdFuncionarios;

    private String cnpj;

    private int solicitacaoAumentoCredito;

    private Situacao situacao;
    
    public static String generate() {
        return UUID.randomUUID().toString();
    }

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

    public Emprestimo criaSolicitacaoEmprestimo(double valor) {

        EmprestimoSituacao situacao;

        if (valor > (this.conta.getLimite() * 1.25) - this.conta.getSaldo()) {
            situacao = EmprestimoSituacao.AGUARDANDO_APROVACAO;
        } else {
            situacao = EmprestimoSituacao.APROVADO;
            this.conta.aumentaSaldoDevedor(valor);
        }

        return Emprestimo.builder()
                .id(Emprestimo.generate())
                .empresaId(this.id)
                .situacao(situacao)
                .valor(valor)
                .build();
    }

    public void solicitaLimiteEmergencial(double novoLimite) {
        if (this.permiteSolicitarCreditoEmergencial() == true
                && novoLimite <= this.calculaLimiteMaximoParaCreditoEmergencial()) {
            this.conta.ajustaLimite(novoLimite);
        }

        this.incrementaSolicitacaoAumentoCredito();
    }

    public double calculaLimiteMaximoParaCreditoEmergencial() {
        double limiteMaximo = this.conta.getLimite() * 1.5;
        return limiteMaximo;
    }

    public void abrirConta() {

        Conta conta = Conta.builder()
                .id(Conta.generate())
                .saldo(0)
                .limite(this.calculaLimiteParaAberturaConta())
                .build();

        this.conta = conta;
    }

    public String getContaId() {
        if (this.conta != null) {
            return this.conta.getId();
        }
        return null;
    }
    
    public double getLimiteConta() {
        if (this.conta != null) {
            return this.conta.getLimite();
        }
        
        return 0;
    }
    
}
