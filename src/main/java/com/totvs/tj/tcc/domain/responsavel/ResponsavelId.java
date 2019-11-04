package com.totvs.tj.tcc.domain.responsavel;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.Value;

@Value
@AllArgsConstructor(staticName = "from")
@ToString
public class ResponsavelId {
    
    
    private String value;

    public static ResponsavelId generate() {
        return ResponsavelId.from(UUID.randomUUID().toString());
    }

}
