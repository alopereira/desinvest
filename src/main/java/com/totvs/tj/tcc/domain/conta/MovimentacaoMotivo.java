package com.totvs.tj.tcc.domain.conta;

public enum MovimentacaoMotivo {
    
    LIMITE_JA_SOLICITADO("Limite de crédito já foi solicitado"),
    LIMITE_ACIMA_MAXIMO("Limite solicitado é superior a 50%. Aguardando aprovação do superior.");
    
    private final String descricaoMotivo;
    
    private MovimentacaoMotivo(String descricaoMotivo) {
        this.descricaoMotivo = descricaoMotivo;
    }
    
    public String getDescricaoMotivo() {        
        return this.descricaoMotivo;        
    }

}
