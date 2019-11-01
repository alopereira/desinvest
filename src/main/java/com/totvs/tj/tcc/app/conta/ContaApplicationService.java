package com.totvs.tj.tcc.app.conta;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.totvs.tj.tcc.domain.conta.ContaId;
import com.totvs.tj.tcc.domain.conta.ContaRepository;
import com.totvs.tj.tcc.domain.empresa.Empresa;
import com.totvs.tj.tcc.domain.empresa.EmpresaId;
import com.totvs.tj.tcc.domain.empresa.EmpresaRepository;
import com.totvs.tj.tcc.domain.movimentacao.Movimentacao;
import com.totvs.tj.tcc.domain.movimentacao.Movimentacao.TipoMovimentacao;
import com.totvs.tj.tcc.domain.movimentacao.MovimentacaoId;
import com.totvs.tj.tcc.domain.movimentacao.MovimentacaoRepository;
import com.totvs.tj.tcc.domain.responsavel.Responsavel;
import com.totvs.tj.tcc.domain.responsavel.ResponsavelId;
import com.totvs.tj.tcc.domain.responsavel.ResponsavelRepository;

import lombok.Builder;

@Service
@Builder
public class ContaApplicationService {

    private ContaRepository contaRepository;
    private EmpresaRepository empresaRepository;
    private ResponsavelRepository responsavelRepository;
    private MovimentacaoRepository movimentacaoRepository;

    public ContaApplicationService(ContaRepository contaRepository,
            EmpresaRepository empresaRepository,
            ResponsavelRepository responsavelRepository,
            MovimentacaoRepository movimentacaoRepository) {
        this.contaRepository = contaRepository;
        this.empresaRepository = empresaRepository;
        this.responsavelRepository = responsavelRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    public ContaId handle(AbrirContaCommand cmd) {
        
        Empresa empresa = this.empresaRepository.getOne(cmd.getEmpresaId());

        empresa.abrirConta();

        this.empresaRepository.save(empresa);
        this.contaRepository.save(empresa.getConta());
        this.movimentacaoRepository.save(Movimentacao.builder()
                .id(MovimentacaoId.generate())
                .contaId(empresa.getContaId())
                .empresaId(empresa.getId())
                .dataHora(LocalDateTime.now())
                .valor(0)
                .tipo(TipoMovimentacao.ABRIR_CONTA)
                .build());
        
        return empresa.getContaId();
    }

    protected Empresa getEmpresaById(EmpresaId empresaId) {
        return empresaRepository.getOne(empresaId);
    }

    protected Responsavel getResponsavelById(ResponsavelId responsavelId) {
        return responsavelRepository.getOne(responsavelId);
    }

}
