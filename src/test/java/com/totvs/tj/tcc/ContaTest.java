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
import com.totvs.tj.tcc.domain.conta.ResponsavelId;
import com.totvs.tj.tcc.domain.empresa.EmpresaId;

public class ContaTest {

    private final ContaId idConta = ContaId.generate();

    private final EmpresaId idEmpresa = EmpresaId.generate();

    private final ResponsavelId idResponsavel = ResponsavelId.generate();

    @Test
    public void aoCriarUmaConta() throws Exception {

        // WHEN
        Conta conta = Conta.builder()
                .id(idConta)
                .empresa(idEmpresa)
                .responsavel(idResponsavel)
            .build();

        // THEN
        assertNotNull(conta);

        assertEquals(idConta, conta.getId());
        assertEquals(idEmpresa, conta.getEmpresa());
        assertEquals(idResponsavel, conta.getResponsavel());

        assertEquals(idConta.toString(), conta.getId().toString());
        assertEquals(idEmpresa.toString(), conta.getEmpresa().toString());
        assertEquals(idResponsavel.toString(), conta.getResponsavel().toString());
    }

    @Test
    public void aoSolicitarAberturaConta() throws Exception {

        // GIVEN
        ContaRepository repository = new ContaRepositoryMock();
        ContaApplicationService service = new ContaApplicationService(repository);

        AbrirContaCommand cmd = AbrirContaCommand.builder()
                .empresa(idEmpresa)
                .responsavel(idResponsavel)
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
                .empresa(idEmpresa)
                .responsavel(idResponsavel)
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
                .empresa(idEmpresa)
                .responsavel(idResponsavel)
                .build();

        // WHEN
        ContaId idConta = service.handle(cmd);

        // THEN
        assertTrue(repository.getOne(idConta).getSaldo() == 0);
        
        
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
