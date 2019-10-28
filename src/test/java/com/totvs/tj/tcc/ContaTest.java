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
import com.totvs.tj.tcc.app.conta.SuspenderContaCommand;
import com.totvs.tj.tcc.app.movimentacao.MovimentacaoApplicationService;
import com.totvs.tj.tcc.app.movimentacao.SolicitaCreditoEmergencialCommand;
import com.totvs.tj.tcc.domain.conta.Conta;
import com.totvs.tj.tcc.domain.conta.ContaId;
import com.totvs.tj.tcc.domain.conta.ContaRepository;
import com.totvs.tj.tcc.domain.empresa.Empresa;
import com.totvs.tj.tcc.domain.empresa.EmpresaId;
import com.totvs.tj.tcc.domain.empresa.EmpresaRepository;
import com.totvs.tj.tcc.domain.movimentacao.Movimentacao;
import com.totvs.tj.tcc.domain.movimentacao.MovimentacaoId;
import com.totvs.tj.tcc.domain.movimentacao.MovimentacaoRepository;
import com.totvs.tj.tcc.domain.movimentacao.MovimentacaoSituacao;
import com.totvs.tj.tcc.domain.responsavel.Responsavel;
import com.totvs.tj.tcc.domain.responsavel.ResponsavelId;
import com.totvs.tj.tcc.domain.responsavel.ResponsavelRepository;

public class ContaTest {

    private final ContaId idConta = ContaId.generate();

    private final EmpresaId idEmpresa = EmpresaId.generate();

    private final ResponsavelId idResponsavel = ResponsavelId.generate();

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

        ContaRepository contaRepository = new ContaRepositoryMock();
        ResponsavelRepository responsavelRepository = new ResponsavelRepositoryMock();
        EmpresaRepository empresaRepository = new EmpresaRepositoryMock();
        
        responsavelRepository.save(Responsavel.builder().id(idResponsavel).build());
        empresaRepository.save(Empresa.builder().id(idEmpresa).build());
        
        
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
    public void supenderUmaContaExistente() throws Exception {

        // GIVEN
        SuspenderContaCommand cmd = SuspenderContaCommand.from(idConta);
        
        ContaRepository contaRepository = new ContaRepositoryMock();

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
        SuspenderContaCommand cmd = SuspenderContaCommand.from(idConta);

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
        ContaRepository contaRepository = new ContaRepositoryMock();
        ResponsavelRepository responsavelRepository = new ResponsavelRepositoryMock();
        EmpresaRepository empresaRepository = new EmpresaRepositoryMock();
        
        responsavelRepository.save(Responsavel.builder().id(idResponsavel).build());
        empresaRepository.save(Empresa.builder().id(idEmpresa).build());
        
        
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
        
        ContaRepository repository = new ContaRepositoryMock();
        MovimentacaoRepository movimentacaoRepository = new MovimentacaoRepositoryMock();
        
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
                .valor(9750)
                .build();        
        
        MovimentacaoApplicationService app = new MovimentacaoApplicationService(movimentacaoRepository, repository);
        
        MovimentacaoId idMovimentacao = app.handle(cmd);
        
        Movimentacao movimentacao = movimentacaoRepository.getOne(idMovimentacao);
        
        assertTrue(movimentacao.getSituacao().equals(MovimentacaoSituacao.APROVADO));
    }
    
    @Test
    public void aoSolicitarCreditoEmergencialAcimaDe50PorCento() {
        
        ContaRepository repository = new ContaRepositoryMock();
        MovimentacaoRepository movimentacaoRepository = new MovimentacaoRepositoryMock();
        
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
                
        MovimentacaoApplicationService app = new MovimentacaoApplicationService(movimentacaoRepository, repository);
        
        MovimentacaoId idMovimentacao = app.handle(cmd);
        
        Movimentacao movimentacao = movimentacaoRepository.getOne(idMovimentacao);
        
        assertTrue(movimentacao.getSituacao().equals(MovimentacaoSituacao.AGUARDANDO_APROVACAO));
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
    
    static class MovimentacaoRepositoryMock implements MovimentacaoRepository {

        private final Map<MovimentacaoId, Movimentacao> movimentacoes = new LinkedHashMap<>();

        @Override
        public void save(Movimentacao movimentacao) {
            movimentacoes.put(movimentacao.getId(), movimentacao);
        }

        @Override
        public Movimentacao getOne(MovimentacaoId id) {
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
