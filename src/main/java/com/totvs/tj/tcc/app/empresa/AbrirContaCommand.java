package com.totvs.tj.tcc.app.empresa;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AbrirContaCommand {

    private String empresaId;
}
