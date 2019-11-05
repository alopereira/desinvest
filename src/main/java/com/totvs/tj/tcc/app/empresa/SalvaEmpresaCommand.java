package com.totvs.tj.tcc.app.empresa;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SalvaEmpresaCommand {
    
    private String responsavelId;
    private double valor;
    private double qtdFuncionarios;
    
}
