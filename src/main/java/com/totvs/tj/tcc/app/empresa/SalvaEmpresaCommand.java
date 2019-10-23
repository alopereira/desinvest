package com.totvs.tj.tcc.app.empresa;

import com.totvs.tj.tcc.domain.conta.ResponsavelId;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SalvaEmpresaCommand {
    
    private ResponsavelId responsavel;
    private double valor;
    private double qtdFuncionarios;
    
}
