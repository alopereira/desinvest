package com.totvs.tj.tcc.domain.empresa;

import static lombok.AccessLevel.PRIVATE;

import com.totvs.tj.tcc.domain.responsavel.ResponsavelId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor(access = PRIVATE)
public class Empresa {
    
    private EmpresaId id;
    
    private ResponsavelId responsavel;
    
    private double valor;
    
    private double qtdFuncionarios;
    
}
