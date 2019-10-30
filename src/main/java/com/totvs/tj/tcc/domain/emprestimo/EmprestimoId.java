package com.totvs.tj.tcc.domain.emprestimo;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(staticName = "from")
public class EmprestimoId {
    
    private String value;

    @Override
    public String toString() {
        return value;
    }

    public static EmprestimoId generate() {
        return EmprestimoId.from(UUID.randomUUID().toString());
    }

}
