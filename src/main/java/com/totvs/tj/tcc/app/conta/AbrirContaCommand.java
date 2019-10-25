package com.totvs.tj.tcc.app.conta;

import com.totvs.tj.tcc.domain.empresa.EmpresaId;
import com.totvs.tj.tcc.domain.responsavel.ResponsavelId;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AbrirContaCommand {

    private EmpresaId empresa;
    
    private ResponsavelId responsavel;
    // teste vitor git
}
