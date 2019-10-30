package com.totvs.tj.tcc.domain.emprestimo;

public enum EmprestimoMotivoRecusa {
    
    LIMITE_JA_SOLICITADO("Limite de crédito já foi solicitado"),
    LIMITE_ACIMA_MAXIMO("Limite solicitado é superior a 50%. Aguardando aprovação do superior.");
    
    private final String descricaoMotivo;
    
    private EmprestimoMotivoRecusa(String descricaoMotivo) {
        this.descricaoMotivo = descricaoMotivo;
    }
    
    public String getDescricaoMotivo() {        
        return this.descricaoMotivo;        
    }

}
