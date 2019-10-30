package com.totvs.tj.tcc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.totvs.tj.tcc.app.conta.AbrirContaCommand;
import com.totvs.tj.tcc.app.conta.ContaApplicationService;
import com.totvs.tj.tcc.app.empresa.SuspenderEmpresaCommand;
import com.totvs.tj.tcc.app.emprestimo.EmprestimoApplicationService;
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
import com.totvs.tj.tcc.domain.emprestimo.EmprestimoSituacao;
import com.totvs.tj.tcc.domain.responsavel.Responsavel;
import com.totvs.tj.tcc.domain.responsavel.ResponsavelId;
import com.totvs.tj.tcc.domain.responsavel.ResponsavelRepository;

public class ContaTest {

    private final ContaId idConta = ContaId.generate();

    private final EmpresaId idEmpresa = EmpresaId.generate();

    private final ResponsavelId idResponsavel = ResponsavelId.generate();
    
    ContaRepository contaRepository = new ContaRepositoryMock();
    
    ResponsavelRepository responsavelRepository = new ResponsavelRepositoryMock();
    
    EmpresaRepository empresaRepository = new EmpresaRepositoryMock();
    
    EmprestimoRepository movimentacaoRepository = new MovimentacaoRepositoryMock();
    
    public void populaMocks() {
        responsavelRepository.save(Responsavel.builder().id(idResponsavel).build());
        empresaRepository.save(Empresa.builder().id(idEmpresa).cnpj("61.366.936/0001-25").build());
    }

    @Test
    public void aoCriarUmaConta() throws Exception {
        
        // WHEN
        Conta conta = Conta.builder()
                .id(idConta)
                .empresa(Empresa.builder().id(idEmpresa).build())
                .responsavel(Responsavel.builder().id(idResponsavel).build())
                .build();

        // THEN
        assertNotNull(conta);

        assertEquals(idConta, conta.getId());
        assertEquals(idEmpresa, conta.getEmpresa().getId());
        assertEquals(idResponsavel, conta.getResponsavel().getId());

        assertEquals(idConta.toString(), conta.getId().toString());
        assertEquals(idEmpresa.toString(), conta.getEmpresaId().toString());
        assertEquals(idResponsavel.toString(), conta.getResponsavelId().toString());
    }

    @Test
    public void aoSolicitarAberturaConta() throws Exception {

        this.populaMocks();
        
        // GIVEN
        ContaApplicationService service = ContaApplicationService.builder()
                .contaRepository(contaRepository)
                .responsavelRepository(responsavelRepository)
                .empresaRepository(empresaRepository)
                .build();
        
        

        AbrirContaCommand cmd = AbrirContaCommand.builder()
                .empresaId(idEmpresa)
                .responsavelId(idResponsavel)
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
        
        ContaApplicationService service = ContaApplicationService.builder()
                .contaRepository(contaRepository)
                .empresaRepository(empresaRepository)
                .responsavelRepository(responsavelRepository)
                .build();

        AbrirContaCommand cmd = AbrirContaCommand.builder()
                .empresaId(idEmpresa)
                .responsavelId(idResponsavel)
                .build();

        // WHEN
        ContaId idConta = service.handle(cmd);

        // THEN
        assertNotNull(idConta);
    }

    @Test
    public void supenderUmaContaExistente() throws Exception {

        // GIVEN
        SuspenderEmpresaCommand cmd = SuspenderEmpresaCommand.from(idConta);

        ContaApplicationService service = ContaApplicationService.builder()
                .contaRepository(contaRepository)
                .responsavelRepository(new ResponsavelRepositoryMock())
                .empresaRepository(new EmpresaRepositoryMock())
                .build();

        contaRepository.save(Conta.builder()
                .id(idConta)
                .empresa(Empresa.builder().id(idEmpresa).build())
                .responsavel(Responsavel.builder().id(idResponsavel).build())
                .build());

        // WHEN
        service.handle(cmd);

        // THEN
        assertFalse(contaRepository.getOne(idConta).isDisponivel());
    }

    @Test(expected = NullPointerException.class)
    public void aoNaoEncontrarContaParaSuspender() throws Exception {

        // GIVEN
        SuspenderEmpresaCommand cmd = SuspenderEmpresaCommand.from(idConta);

        ContaApplicationService service = ContaApplicationService.builder()
                .contaRepository(new ContaRepositoryMock())
                .responsavelRepository(new ResponsavelRepositoryMock())
                .empresaRepository(new EmpresaRepositoryMock())
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
        
        ContaApplicationService service = ContaApplicationService.builder()
                .contaRepository(contaRepository)
                .responsavelRepository(responsavelRepository)
                .empresaRepository(empresaRepository)
                .build();
        
        AbrirContaCommand cmd = AbrirContaCommand.builder()
                .empresaId(idEmpresa)
                .responsavelId(idResponsavel)
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
                .id(idEmpresa)
                .valor(15000)
                .qtdFuncionarios(500)
                .build();

        Responsavel responsavel = Responsavel.builder()
                .id(idResponsavel)
                .supervisor("vitor.barba@totvs.com.br")
                .build();
        
        
        Conta conta = Conta.builder()
                .id(idConta)
                .responsavel(responsavel)
                .empresa(empresa)
                .build();
                
        
        assertTrue(conta.getLimite() == 6500);

    }
    
    @Test()
    public void validaLimiteMaximoInicialdaConta() throws Exception {

        // GIVEN
        Empresa empresa = Empresa.builder()
                .id(idEmpresa)
                .valor(150000)
                .qtdFuncionarios(50000)
                .build();

        Responsavel responsavel = Responsavel.builder()
                .id(idResponsavel)
                .supervisor("vitor.barba@totvs.com.br")
                .build();
        
        
        Conta conta = Conta.builder()
                .id(idConta)
                .responsavel(responsavel)
                .empresa(empresa)
                .build();
                
        
        assertTrue(conta.getLimite() == 15000);
    }
    
    @Test
    public void aoSolicitarCreditoEmergencialAte50PorCento() {
        
        // GIVEN
        Empresa empresa = Empresa.builder()
                .id(idEmpresa)
                .valor(15000)
                .qtdFuncionarios(500)
                .build();    

        Responsavel responsavel = Responsavel.builder()
                .id(idResponsavel)
                .supervisor("vitor.barba@totvs.com.br")
                .build();        
        
        Conta conta = Conta.builder()
                .id(idConta)
                .responsavel(responsavel)
                .empresa(empresa)
                .build();
        
        contaRepository.save(conta);
        
        //THEN
        SolicitaCreditoEmergencialCommand cmd = SolicitaCreditoEmergencialCommand.builder()
                .contaId(idConta)
                .valor(9750)
                .build();        
        
        EmprestimoApplicationService app = new EmprestimoApplicationService(movimentacaoRepository, contaRepository);
        
        EmprestimoId idMovimentacao = app.handle(cmd);
        
        Emprestimo movimentacao = movimentacaoRepository.getOne(idMovimentacao);
        
        assertTrue(movimentacao.getSituacao().equals(EmprestimoSituacao.APROVADO));
    }
    
    @Test
    public void aoSolicitarCreditoEmergencialAcimaDe50PorCento() {
        
        ContaRepository repository = new ContaRepositoryMock();
        EmprestimoRepository movimentacaoRepository = new MovimentacaoRepositoryMock();
        
        // GIVEN
        Empresa empresa = Empresa.builder()
                .id(idEmpresa)
                .valor(15000)
                .qtdFuncionarios(500)
                .build();    

        Responsavel responsavel = Responsavel.builder()
                .id(idResponsavel)
                .supervisor("vitor.barba@totvs.com.br")
                .build();        
        
        Conta conta = Conta.builder()
                .id(idConta)
                .responsavel(responsavel)
                .empresa(empresa)
                .build();
        
        repository.save(conta);
        
        //THEN
        SolicitaCreditoEmergencialCommand cmd = SolicitaCreditoEmergencialCommand.builder()
                .contaId(idConta)
                .valor(10000)
                .build();
                
        EmprestimoApplicationService app = new EmprestimoApplicationService(movimentacaoRepository, repository);
        
        EmprestimoId idMovimentacao = app.handle(cmd);
        
        Emprestimo movimentacao = movimentacaoRepository.getOne(idMovimentacao);
        
        assertTrue(movimentacao.getSituacao().equals(EmprestimoSituacao.AGUARDANDO_APROVACAO));
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
    
    static class MovimentacaoRepositoryMock implements EmprestimoRepository {

        private final Map<EmprestimoId, Emprestimo> movimentacoes = new LinkedHashMap<>();

        @Override
        public void save(Emprestimo movimentacao) {
            movimentacoes.put(movimentacao.getId(), movimentacao);
        }

        @Override
        public Emprestimo getOne(EmprestimoId id) {
            return movimentacoes.get(id);
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

        private final Map<ResponsavelId, Responsavel > responsaveis = new LinkedHashMap<>();

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
