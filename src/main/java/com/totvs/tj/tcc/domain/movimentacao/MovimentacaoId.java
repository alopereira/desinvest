package com.totvs.tj.tcc.domain.movimentacao;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(staticName = "from")
public class MovimentacaoId {

    private String value;

    /**
     * Retorna o valor raiz do códito.
     */
    @Override
    public String toString() {
        return value;
    }

    public static MovimentacaoId generate() {
        return MovimentacaoId.from(UUID.randomUUID().toString());
    }

}
