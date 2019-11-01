package com.totvs.tj.tcc.app.emprestimo;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.totvs.tj.tcc.domain.empresa.Empresa;
import com.totvs.tj.tcc.domain.empresa.EmpresaRepository;
import com.totvs.tj.tcc.domain.emprestimo.Emprestimo;
import com.totvs.tj.tcc.domain.emprestimo.EmprestimoId;
import com.totvs.tj.tcc.domain.emprestimo.EmprestimoRepository;
import com.totvs.tj.tcc.domain.movimentacao.Movimentacao;
import com.totvs.tj.tcc.domain.movimentacao.Movimentacao.TipoMovimentacao;
import com.totvs.tj.tcc.domain.movimentacao.MovimentacaoId;
import com.totvs.tj.tcc.domain.movimentacao.MovimentacaoRepository;

import lombok.Builder;

@Service
@Builder
public class EmprestimoApplicationService {
    
    private EmprestimoRepository emprestimoRepository;
    private EmpresaRepository empresaRepository;
    private MovimentacaoRepository movimentacaoRepository;
    
    public EmprestimoApplicationService(EmprestimoRepository emprestimoRepository,
            EmpresaRepository empresaRepository,
            MovimentacaoRepository movimentacaoRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.empresaRepository = empresaRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }
    
    @Transactional
    public EmprestimoId handle(SolicitaEmprestimoCommand cmd) {
        Empresa empresa = this.empresaRepository.getOne(cmd.getEmpresaId());
        
        Emprestimo emprestimo = empresa.criaSolicitacaoEmprestimo(cmd.getValor());
        
        emprestimoRepository.save(emprestimo);
        this.movimentacaoRepository.save(Movimentacao.builder()
                .id(MovimentacaoId.generate())
                .contaId(empresa.getContaId())
                .empresaId(empresa.getId())
                .dataHora(LocalDateTime.now())
                .valor(cmd.getValor())
                .tipo(TipoMovimentacao.SOLICITA_CREDITO_EMERGENCIAL)
                .build());
        
        return emprestimo.getId();
    }
    
    
    
}
