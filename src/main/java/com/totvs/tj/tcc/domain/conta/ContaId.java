package com.totvs.tj.tcc.domain.conta;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.Value;

@Value
@AllArgsConstructor(staticName = "from")
@ToString
public class ContaId {

    private String value;
    
    public static ContaId generate() {
        return ContaId.from(UUID.randomUUID().toString());
    }

}
