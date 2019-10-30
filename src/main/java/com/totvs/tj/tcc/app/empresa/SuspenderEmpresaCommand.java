package com.totvs.tj.tcc.app.empresa;

import com.totvs.tj.tcc.domain.empresa.EmpresaId;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "from")
public class SuspenderEmpresaCommand {

    private EmpresaId empresa;
    
}
