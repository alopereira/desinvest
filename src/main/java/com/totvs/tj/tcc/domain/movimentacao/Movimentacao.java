package com.totvs.tj.tcc.domain.movimentacao;

import static lombok.AccessLevel.PRIVATE;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

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
    
    @Id
    private String id;
    
    private String empresaId;
    
    private double valor;
    
    private String contaId;
    
    private String responsavelId;
    
    private TipoMovimentacao tipo;
    
    private LocalDateTime dataHora;
    
    public static String generate() {
        return UUID.randomUUID().toString();
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
