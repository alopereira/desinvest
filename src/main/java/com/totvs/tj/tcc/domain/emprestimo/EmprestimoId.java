package com.totvs.tj.tcc.domain.emprestimo;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.Value;

@Value
@AllArgsConstructor(staticName = "from")
@ToString
public class EmprestimoId {
    
    private String value;
    
    public static EmprestimoId generate() {
        return EmprestimoId.from(UUID.randomUUID().toString());
    }

}
