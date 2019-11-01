package com.totvs.tj.tcc.app.movimentacao;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.totvs.tj.tcc.domain.movimentacao.Movimentacao;
import com.totvs.tj.tcc.domain.movimentacao.MovimentacaoId;
import com.totvs.tj.tcc.domain.movimentacao.MovimentacaoRepository;

@Service
public class MovimentacaoApplicationService {
    
    private MovimentacaoRepository movimentacaoRepository;
    
    public MovimentacaoApplicationService(MovimentacaoRepository movimentacaoRepository) {
        this.movimentacaoRepository = movimentacaoRepository;
    }
    
    public Map<MovimentacaoId, Movimentacao> handle(ConsultaMovimentacaoPorEmpresalCommand cmd) {
        return this.movimentacaoRepository.getMovimentacaoPorEmpresa(cmd.getEmpresaId());
    }
    
}
