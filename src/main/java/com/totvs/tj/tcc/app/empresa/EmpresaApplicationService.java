package com.totvs.tj.tcc.app.empresa;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.totvs.tj.tcc.app.emprestimo.SolicitaCreditoEmergencialCommand;
import com.totvs.tj.tcc.domain.conta.ContaId;
import com.totvs.tj.tcc.domain.conta.ContaRepository;
import com.totvs.tj.tcc.domain.empresa.Empresa;
import com.totvs.tj.tcc.domain.empresa.EmpresaId;
import com.totvs.tj.tcc.domain.empresa.EmpresaRepository;
import com.totvs.tj.tcc.domain.movimentacao.Movimentacao;
import com.totvs.tj.tcc.domain.movimentacao.Movimentacao.TipoMovimentacao;
import com.totvs.tj.tcc.domain.movimentacao.MovimentacaoId;
import com.totvs.tj.tcc.domain.movimentacao.MovimentacaoRepository;

import lombok.Builder;

@Service
@Builder
public class EmpresaApplicationService {

    private EmpresaRepository empresaRepository;
    private MovimentacaoRepository movimentacaoRepository;
    private ContaRepository contaRepository;

    public EmpresaApplicationService(EmpresaRepository empresaRepository,
            MovimentacaoRepository movimentacaoRepository,
            ContaRepository contaRepository) {
        this.empresaRepository = empresaRepository;
        this.movimentacaoRepository = movimentacaoRepository;
        this.contaRepository = contaRepository;
    }

    @Transactional
    public EmpresaId handle(SalvaEmpresaCommand cmd) {

        EmpresaId idEmpresa = EmpresaId.generate();

        Empresa empresa = Empresa.builder()
                .id(idEmpresa)
                .valor(cmd.getValor())
                .qtdFuncionarios(cmd.getQtdFuncionarios())
                .build();

        this.empresaRepository.save(empresa);

        this.movimentacaoRepository.save(Movimentacao.builder()
                .id(MovimentacaoId.generate())
                .contaId(empresa.getContaId())
                .empresaId(empresa.getId())
                .dataHora(LocalDateTime.now())
                .valor(0)
                .tipo(TipoMovimentacao.CADASTRO_EMPRESA)
                .build());

        return idEmpresa;
    }

    @Transactional
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

    @Transactional
    public void handle(SuspenderEmpresaCommand cmd) {

        Empresa empresa = empresaRepository.getOne(cmd.getEmpresa());

        empresa.suspender();

        this.empresaRepository.save(empresa);

        this.movimentacaoRepository.save(Movimentacao.builder()
                .id(MovimentacaoId.generate())
                .contaId(empresa.getContaId())
                .empresaId(empresa.getId())
                .dataHora(LocalDateTime.now())
                .valor(0)
                .tipo(TipoMovimentacao.SUSPENDER_CONTA)
                .build());
    }

    @Transactional
    public double handle(SolicitaCreditoEmergencialCommand cmd) {
        Empresa empresa = this.empresaRepository.getOne(cmd.getEmpresaId());
        empresa.solicitaLimiteEmergencial(cmd.getValor());

        this.empresaRepository.save(empresa);
        this.movimentacaoRepository.save(Movimentacao.builder()
                .id(MovimentacaoId.generate())
                .contaId(empresa.getContaId())
                .empresaId(empresa.getId())
                .dataHora(LocalDateTime.now())
                .valor(cmd.getValor())
                .tipo(TipoMovimentacao.APROVACAO_CREDITO_EMERGENCIAL)
                .build());

        return empresa.getLimiteConta();
    }

}
