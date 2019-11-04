package com.totvs.tj.tcc.domain.movimentacao;

import static lombok.AccessLevel.PRIVATE;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.totvs.tj.tcc.domain.conta.ContaId;
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
@Entity
public class Movimentacao {
    
    private MovimentacaoId id;
    
    private EmpresaId empresaId;
    
    private double valor;
    
    private ContaId contaId;
    
    private ResponsavelId responsavelId;
    
    private TipoMovimentacao tipo;
    
    private LocalDateTime dataHora;
    
    @Id
    public String getIdValue() {
        return this.id.getValue();
    }
    
    
    public enum TipoMovimentacao {
        CADASTRO_EMPRESA,
        ABRIR_CONTA,
        SOLICITAR_EMPRESTIMO,
        COMPRAR_DIVIDA,
        DEVOLUCAO,
        SUSPENDER_CONTA,
        SOLICITA_LIMITE_EMERGENCIAL,
        APROVACAO_LIMITE_EMERGENCIAL,
        RECUSA_AUMENTO_LIMITE,
        APROVAR_SOLICITACAO_EMPRESTIMO,
        REPROVAR_EMPRESTIMO
    }
    
    
}
