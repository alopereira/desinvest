package com.totvs.tj.tcc.app.conta;

import com.totvs.tj.tcc.domain.empresa.Empresa;
import com.totvs.tj.tcc.domain.responsavel.Responsavel;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AbrirContaCommand {

    private Empresa empresa;
    
    private Responsavel responsavel;
}
