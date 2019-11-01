package com.totvs.tj.tcc;

import java.util.LinkedHashMap;
import java.util.Map;

import com.totvs.tj.tcc.domain.empresa.EmpresaId;
import com.totvs.tj.tcc.domain.movimentacao.Movimentacao;
import com.totvs.tj.tcc.domain.movimentacao.MovimentacaoId;
import com.totvs.tj.tcc.domain.movimentacao.MovimentacaoRepository;

public class MovimentacaoRepositoryMock implements MovimentacaoRepository {

    private final Map<MovimentacaoId, Movimentacao> movimentacoes = new LinkedHashMap<>();

    @Override
    public void save(Movimentacao movimentacao) {
        movimentacoes.put(movimentacao.getId(), movimentacao);
    }

    @Override
    public Movimentacao getOne(MovimentacaoId id) {
        return movimentacoes.get(id);
    }

    @Override
    public Map<MovimentacaoId, Movimentacao> getMovimentacaoPorEmpresa(EmpresaId empresaId) {
        Map<MovimentacaoId, Movimentacao> movimentosPorEmpresa = new LinkedHashMap<>();
        for(Map.Entry<MovimentacaoId, Movimentacao> entry : movimentacoes.entrySet()) {
            Movimentacao movimentacao = entry.getValue();
            if (movimentacao.getEmpresaId().equals(empresaId)) {
                movimentosPorEmpresa.put(entry.getKey(), entry.getValue());                    
            }
        }
        return movimentosPorEmpresa;
    }
}