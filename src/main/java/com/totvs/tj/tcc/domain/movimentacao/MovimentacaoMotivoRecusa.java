package com.totvs.tj.tcc.domain.movimentacao;

public enum MovimentacaoMotivoRecusa {
    
    LIMITE_JA_SOLICITADO("Limite de crédito já foi solicitado"),
    LIMITE_ACIMA_MAXIMO("Limite solicitado é superior a 50%. Aguardando aprovação do superior.");
    
    private final String descricaoMotivo;
    
    private MovimentacaoMotivoRecusa(String descricaoMotivo) {
        this.descricaoMotivo = descricaoMotivo;
    }
    
    public String getDescricaoMotivo() {        
        return this.descricaoMotivo;        
    }

}
