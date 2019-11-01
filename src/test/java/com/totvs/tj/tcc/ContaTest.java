package com.totvs.tj.tcc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.totvs.tj.tcc.app.empresa.AbrirContaCommand;
import com.totvs.tj.tcc.app.empresa.EmpresaApplicationService;
import com.totvs.tj.tcc.app.empresa.SuspenderEmpresaCommand;
import com.totvs.tj.tcc.app.emprestimo.SolicitaCreditoEmergencialCommand;
import com.totvs.tj.tcc.domain.conta.Conta;
import com.totvs.tj.tcc.domain.conta.ContaId;
import com.totvs.tj.tcc.domain.conta.ContaRepository;
import com.totvs.tj.tcc.domain.empresa.Empresa;
import com.totvs.tj.tcc.domain.empresa.EmpresaId;
import com.totvs.tj.tcc.domain.empresa.EmpresaRepository;
import com.totvs.tj.tcc.domain.emprestimo.Emprestimo;
import com.totvs.tj.tcc.domain.emprestimo.EmprestimoId;
import com.totvs.tj.tcc.domain.emprestimo.EmprestimoRepository;
import com.totvs.tj.tcc.domain.movimentacao.Movimentacao;
import com.totvs.tj.tcc.domain.movimentacao.MovimentacaoId;
import com.totvs.tj.tcc.domain.movimentacao.MovimentacaoRepository;
import com.totvs.tj.tcc.domain.responsavel.Responsavel;
import com.totvs.tj.tcc.domain.responsavel.ResponsavelId;
import com.totvs.tj.tcc.domain.responsavel.ResponsavelRepository;

public class ContaTest {

    private final ContaId idConta = ContaId.generate();

    private final EmpresaId empresaId = EmpresaId.generate();

    private final ResponsavelId responsavelId = ResponsavelId.generate();

    ContaRepository contaRepository = new ContaRepositoryMock();

    ResponsavelRepository responsavelRepository = new ResponsavelRepositoryMock();

    EmpresaRepository empresaRepository = new EmpresaRepositoryMock();

    EmprestimoRepository emprestimoRepository = new EmprestimoRepositoryMock();
    
    MovimentacaoRepository movimentacaoRepository = new MovimentacaoRepositoryMock();

    public void populaMocks() {
        responsavelRepository.save(Responsavel.builder()
                .id(responsavelId)
                .supervisor("Fulano de tal")
                .build());
        
        empresaRepository.save(Empresa.builder()
                .id(empresaId)
                .cnpj("61.366.936/0001-25")
                .qtdFuncionarios(500)
                .valor(10000)
                .build());
    }

    @Test
    public void aoCriarUmaConta() throws Exception {

        // WHEN
        Conta conta = Conta.builder()
                .id(idConta)
                .build();

        // THEN
        assertNotNull(conta);
        assertEquals(idConta, conta.getId());
        assertEquals(idConta.toString(), conta.getId().toString());
    }

    @Test
    public void aoSolicitarAberturaConta() throws Exception {

        this.populaMocks();

        // GIVEN
        EmpresaApplicationService service = EmpresaApplicationService.builder()
                .contaRepository(contaRepository)
                .empresaRepository(empresaRepository)
                .build();

        AbrirContaCommand cmd = AbrirContaCommand.builder()
                .empresaId(empresaId)
                .build();

        // WHEN
        ContaId idConta = service.handle(cmd);

        // THEN
        assertNotNull(idConta);
    }

    @Test
    public void aoSolicitarAberturaContaPessoaFisica() throws Exception {

        // GIVEN
        this.populaMocks();

        EmpresaApplicationService service = EmpresaApplicationService.builder()
                .contaRepository(contaRepository)
                .empresaRepository(empresaRepository)
                .build();

        AbrirContaCommand cmd = AbrirContaCommand.builder()
                .empresaId(empresaId)
                .build();

        // WHEN
        ContaId idConta = service.handle(cmd);

        // THEN
        assertNotNull(idConta);
    }

    @Test
    public void supenderUmaContaExistente() throws Exception {

        // GIVEN
        SuspenderEmpresaCommand cmd = SuspenderEmpresaCommand.from(empresaId);

        EmpresaApplicationService service = EmpresaApplicationService.builder()
                .empresaRepository(empresaRepository)
                .movimentacaoRepository(movimentacaoRepository)
                .contaRepository(contaRepository)
                .build();

        empresaRepository.save(Empresa.builder()
                .id(empresaId)
                .responsaveId(responsavelId)
                .build());

        // WHEN
        service.handle(cmd);

        // THEN
        assertFalse(empresaRepository.getOne(empresaId).isDisponivel());
    }

    @Test(expected = NullPointerException.class)
    public void aoNaoEncontrarContaParaSuspender() throws Exception {

        // GIVEN
        SuspenderEmpresaCommand cmd = SuspenderEmpresaCommand.from(empresaId);

        EmpresaApplicationService service = EmpresaApplicationService
                .builder()
                .empresaRepository(empresaRepository)
                .build();
        // WHEN
        service.handle(cmd);

        // THEN
        assertTrue("Não deve chegar aqui...", false);
    }

    @Test()
    public void validaCriacaoContaComSaldoZero() throws Exception {

        // GIVEN
        this.populaMocks();

        EmpresaApplicationService service = EmpresaApplicationService.builder()
                .contaRepository(contaRepository)
                .empresaRepository(empresaRepository)
                .build();

        AbrirContaCommand cmd = AbrirContaCommand.builder()
                .empresaId(empresaId)
                .build();

        // WHEN
        ContaId idConta = service.handle(cmd);

        // THEN
        assertTrue(contaRepository.getOne(idConta).getSaldo() == 0);

    }

    @Test()
    public void validaLimiteInicialdaConta() throws Exception {

        // GIVEN
        Empresa empresa = Empresa.builder()
                .id(empresaId)
                .valor(15000)
                .qtdFuncionarios(500)
                .build();
        
        // WHEN
        empresa.abrirConta();
        
        Conta conta = empresa.getConta();

        //THEN
        assertTrue(conta.getLimite() == 6500);

    }

    @Test()
    public void validaLimiteMaximoInicialdaConta() throws Exception {

        // GIVEN
        Empresa empresa = Empresa.builder()
                .id(empresaId)
                .valor(150000)
                .qtdFuncionarios(50000)
                .build();
        // WHEN
        empresa.abrirConta();
        
        //THEN
        assertTrue(empresa.getLimiteConta() == 15000);
    }

    @Test
    public void aoSolicitarCreditoEmergencialAte50PorCento() {

        // GIVEN
        Empresa empresa = Empresa.builder()
                .id(empresaId)
                .valor(15000)
                .qtdFuncionarios(500)
                .build();
        
        empresa.abrirConta();
        empresaRepository.save(empresa);

        
        SolicitaCreditoEmergencialCommand cmd = SolicitaCreditoEmergencialCommand.builder()
                .empresaId(empresaId)
                .valor(9750)
                .build();
        
        // WHEN
        EmpresaApplicationService empresaApplication = EmpresaApplicationService.builder()
                .empresaRepository(empresaRepository)
                .build();
        
        //THEN
        double novoLimiteCredito = empresaApplication.handle(cmd);

        assertTrue(novoLimiteCredito == 9750);
    }

    @Test
    public void aoSolicitarCreditoEmergencialAcimaDe50PorCento() {

        // GIVEN
        Empresa empresa = Empresa.builder()
                .id(empresaId)
                .responsaveId(responsavelId)
                .valor(1000)
                .qtdFuncionarios(500)
                .build();
        
        empresa.abrirConta();
        empresaRepository.save(empresa);

        //THEN
        SolicitaCreditoEmergencialCommand cmd = SolicitaCreditoEmergencialCommand.builder()
                .empresaId(empresaId)
                .valor(10000)
                .build();

        EmpresaApplicationService empresaApplication = EmpresaApplicationService.builder()
                .empresaRepository(empresaRepository)
                .movimentacaoRepository(movimentacaoRepository)
                .contaRepository(contaRepository)
                .build();
        
        double limiteAntigo = empresa.getLimiteConta();

        double novoLimite = empresaApplication.handle(cmd);

        assertTrue(novoLimite == limiteAntigo);
    }
    
    static class MovimentacaoRepositoryMock implements MovimentacaoRepository {

        private final Map<MovimentacaoId, Movimentacao> movimentacoes = new LinkedHashMap<>();

        @Override
        public void save(Movimentacao conta) {
            movimentacoes.put(conta.getId(), conta);
        }

        @Override
        public Movimentacao getOne(MovimentacaoId id) {
            return movimentacoes.get(id);
        }

        @Override
        public Map<MovimentacaoId, Movimentacao> getMovimentacaoPorEmpresa(EmpresaId empresaId) {
            // TODO Auto-generated method stub
            return null;
        }
    }
    
    static class ContaRepositoryMock implements ContaRepository {

        private final Map<ContaId, Conta> contas = new LinkedHashMap<>();

        @Override
        public void save(Conta conta) {
            contas.put(conta.getId(), conta);
        }

        @Override
        public Conta getOne(ContaId id) {
            return contas.get(id);
        }
    }

    static class EmprestimoRepositoryMock implements EmprestimoRepository {

        private final Map<EmprestimoId, Emprestimo> emprestimos = new LinkedHashMap<>();

        @Override
        public void save(Emprestimo emprestimo) {
            emprestimos.put(emprestimo.getId(), emprestimo);
        }

        @Override
        public Emprestimo getOne(EmprestimoId id) {
            return emprestimos.get(id);
        }
    }

    static class EmpresaRepositoryMock implements EmpresaRepository {

        private final Map<EmpresaId, Empresa> empresas = new LinkedHashMap<>();

        @Override
        public void save(Empresa empresa) {
            empresas.put(empresa.getId(), empresa);
        }

        @Override
        public Empresa getOne(EmpresaId id) {
            return empresas.get(id);
        }
    }

    static class ResponsavelRepositoryMock implements ResponsavelRepository {

        private final Map<ResponsavelId, Responsavel> responsaveis = new LinkedHashMap<>();

        @Override
        public void save(Responsavel responsavel) {
            responsaveis.put(responsavel.getId(), responsavel);
        }

        @Override
        public Responsavel getOne(ResponsavelId id) {
            return responsaveis.get(id);
        }
    }

}
