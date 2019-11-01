package com.totvs.tj.tcc.app.empresa;

import com.totvs.tj.tcc.domain.empresa.EmpresaId;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AbrirContaCommand {

    private EmpresaId empresaId;
}
