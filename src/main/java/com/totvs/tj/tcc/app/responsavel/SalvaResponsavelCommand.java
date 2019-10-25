package com.totvs.tj.tcc.app.responsavel;

import com.totvs.tj.tcc.domain.responsavel.ResponsavelId;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SalvaResponsavelCommand {
    
    private ResponsavelId responsavel;
    private String supervisor;
    
}
