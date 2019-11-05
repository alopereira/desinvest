package com.totvs.tj.tcc.app.empresa;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "from")
public class SuspenderEmpresaCommand {

    private String empresa;
    
}
