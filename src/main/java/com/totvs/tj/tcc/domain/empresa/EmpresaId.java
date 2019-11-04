package com.totvs.tj.tcc.domain.empresa;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.Value;

@Value
@AllArgsConstructor(staticName = "from")
@ToString
public class EmpresaId {

    private String value;

    public static EmpresaId generate() {
        return EmpresaId.from(UUID.randomUUID().toString());
    }

}
