package com.totvs.tj.tcc.app.movimentacao;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.totvs.tj.tcc.domain.movimentacao.Movimentacao;
import com.totvs.tj.tcc.domain.movimentacao.MovimentacaoRepository;

import lombok.Builder;

@Service
@Builder
public class MovimentacaoApplicationService {
    
    private MovimentacaoRepository movimentacaoRepository;
    
    public MovimentacaoApplicationService(MovimentacaoRepository movimentacaoRepository) {
        this.movimentacaoRepository = movimentacaoRepository;
    }
    
    public Map<String, Movimentacao> handle(ConsultaMovimentacaoPorEmpresalCommand cmd) {
        return this.movimentacaoRepository.getMovimentacaoPorEmpresa(cmd.getEmpresaId());
    }
    
}
