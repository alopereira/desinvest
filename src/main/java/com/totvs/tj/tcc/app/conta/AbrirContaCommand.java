package com.totvs.tj.tcc.app.conta;

import com.totvs.tj.tcc.domain.conta.ResponsavelId;
import com.totvs.tj.tcc.domain.empresa.EmpresaId;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AbrirContaCommand {

    private EmpresaId empresa;
    
    private ResponsavelId responsavel;
    
}
