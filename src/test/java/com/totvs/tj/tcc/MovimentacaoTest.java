package com.totvs.tj.tcc;

import static org.junit.Assert.assertTrue;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.totvs.tj.tcc.app.empresa.AbrirContaCommand;
import com.totvs.tj.tcc.app.empresa.EmpresaApplicationService;
import com.totvs.tj.tcc.app.empresa.SalvaEmpresaCommand;
import com.totvs.tj.tcc.app.movimentacao.ConsultaMovimentacaoPorEmpresalCommand;
import com.totvs.tj.tcc.app.movimentacao.MovimentacaoApplicationService;
import com.totvs.tj.tcc.domain.conta.ContaRepository;
import com.totvs.tj.tcc.domain.empresa.EmpresaRepository;
import com.totvs.tj.tcc.domain.movimentacao.Movimentacao;
import com.totvs.tj.tcc.domain.movimentacao.MovimentacaoRepository;
import com.totvs.tj.tcc.domain.responsavel.Responsavel;

public class MovimentacaoTest {

    ContaRepository contaRepository = new ContaTest.ContaRepositoryMock();
    MovimentacaoRepository movimentacaoRepository = new MovimentacaoRepositoryMock();
    EmpresaRepository empresaRepository = new EmpresaTest.EmpresaRepositoryMock();

    EmpresaApplicationService empresaApplication = EmpresaApplicationService.builder()
            .empresaRepository(empresaRepository)
            .movimentacaoRepository(movimentacaoRepository)
            .contaRepository(contaRepository)
            .build();

    @Test
    public void aoMovimentacoesPorEmpresa() throws Exception {
        // GIVEN

        String empresaId = empresaApplication.handle(SalvaEmpresaCommand.builder()
                .responsavelId(Responsavel.generate())
                .qtdFuncionarios(1000)
                .valor(40000)
                .build());

        // WHEN

        ConsultaMovimentacaoPorEmpresalCommand consultaCmd = ConsultaMovimentacaoPorEmpresalCommand.builder()
                .empresaId(empresaId)
                .build();

        MovimentacaoApplicationService movimentacaoApplication = MovimentacaoApplicationService.builder()
                .movimentacaoRepository(movimentacaoRepository)
                .build();

        Map<String, Movimentacao> movimentacoes = movimentacaoApplication.handle(consultaCmd);

        // THEN
        assertTrue(movimentacoes.size() == 1);

    }

    @Test
    public void aoMovimentacoesPorEmpresaAbrirConta() throws Exception {
        // GIVEN

        String empresaId = empresaApplication.handle(SalvaEmpresaCommand.builder()
                .responsavelId(Responsavel.generate())
                .qtdFuncionarios(1000)
                .valor(40000)
                .build());

        empresaApplication.handle(AbrirContaCommand.builder()
                .empresaId(empresaId)
                .build());

        // WHEN

        ConsultaMovimentacaoPorEmpresalCommand consultaCmd = ConsultaMovimentacaoPorEmpresalCommand.builder()
                .empresaId(empresaId)
                .build();

        MovimentacaoApplicationService movimentacaoApplication = MovimentacaoApplicationService.builder()
                .movimentacaoRepository(movimentacaoRepository)
                .build();

        Map<String, Movimentacao> movimentacoes = movimentacaoApplication.handle(consultaCmd);

        // THEN

        assertTrue(movimentacoes.size() == 2);

    }

    static class MovimentacaoRepositoryMock implements MovimentacaoRepository {

        private final Map<String, Movimentacao> movimentacoes = new LinkedHashMap<>();

        @Override
        public void save(Movimentacao movimentacao) {
            movimentacoes.put(movimentacao.getId(), movimentacao);
        }

        @Override
        public Movimentacao getOne(String id) {
            return movimentacoes.get(id);
        }

        @Override
        public Map<String, Movimentacao> getMovimentacaoPorEmpresa(String empresaId) {
            Map<String, Movimentacao> movimentosPorEmpresa = new LinkedHashMap<>();
            for (Map.Entry<String, Movimentacao> entry : movimentacoes.entrySet()) {
                Movimentacao movimentacao = entry.getValue();
                if (movimentacao.getEmpresaId().equals(empresaId)) {
                    movimentosPorEmpresa.put(entry.getKey(), entry.getValue());
                }
            }
            return movimentosPorEmpresa;
        }
    }

}
