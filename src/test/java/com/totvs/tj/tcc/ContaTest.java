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
import com.totvs.tj.tcc.domain.conta.Conta;
import com.totvs.tj.tcc.domain.conta.ContaId;
import com.totvs.tj.tcc.domain.conta.ContaRepository;
import com.totvs.tj.tcc.domain.empresa.Empresa;
import com.totvs.tj.tcc.domain.empresa.EmpresaId;
import com.totvs.tj.tcc.domain.responsavel.Responsavel;
import com.totvs.tj.tcc.domain.responsavel.ResponsavelId;

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

        // GIVEN
        ContaRepository repository = new ContaRepositoryMock();
        ContaApplicationService service = new ContaApplicationService(repository);

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

        ContaRepository repository = new ContaRepositoryMock();
        ContaApplicationService service = new ContaApplicationService(repository);

        repository.save(Conta.builder()
                .id(idConta)
                .empresa(Empresa.builder().id(idEmpresa).build())
                .responsavel(Responsavel.builder().id(idResponsavel).build())
                .build());

        // WHEN
        service.handle(cmd);

        // THEN
        assertFalse(repository.getOne(idConta).isDisponivel());
    }

    @Test(expected = NullPointerException.class)
    public void aoNaoEncontrarContaParaSuspender() throws Exception {

        // GIVEN
        SuspenderContaCommand cmd = SuspenderContaCommand.from(idConta);

        ContaRepository repository = new ContaRepositoryMock();
        ContaApplicationService service = new ContaApplicationService(repository);

        // WHEN
        service.handle(cmd);

        // THEN
        assertTrue("Não deve chegar aqui...", false);
    }

    @Test()
    public void validaCriacaoContaComSaldoZero() throws Exception {

        // GIVEN
        ContaRepository repository = new ContaRepositoryMock();
        ContaApplicationService service = new ContaApplicationService(repository);

        AbrirContaCommand cmd = AbrirContaCommand.builder()
                .empresaId(idEmpresa)
                .responsavelId(idResponsavel)
                .build();

        // WHEN
        ContaId idConta = service.handle(cmd);

        // THEN
        assertTrue(repository.getOne(idConta).getSaldo() == 0);

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
    public void validaSolicitacaoCreditoEmergencialAte50PorCento() {
        
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
        
        double valor = 9750;
        
        //THEN
        assertTrue(conta.solicitarCreditoEmergencial(valor));
    }
    
    @Test
    public void validaSolicitacaoCreditoEmergencialAcimaDe50PorCento() {
        
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
        
        double valor = 10000;
        
        //THEN
        assertFalse(conta.solicitarCreditoEmergencial(valor));
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

}
