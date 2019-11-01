package com.totvs.tj.tcc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.totvs.tj.tcc.app.emprestimo.DevolverEmpresstimoCommand;
import com.totvs.tj.tcc.app.emprestimo.EmprestimoApplicationService;
import com.totvs.tj.tcc.app.emprestimo.SolicitaEmprestimoCommand;
import com.totvs.tj.tcc.domain.conta.Conta;
import com.totvs.tj.tcc.domain.empresa.Empresa;
import com.totvs.tj.tcc.domain.empresa.EmpresaId;
import com.totvs.tj.tcc.domain.empresa.EmpresaRepository;
import com.totvs.tj.tcc.domain.emprestimo.Emprestimo;
import com.totvs.tj.tcc.domain.emprestimo.EmprestimoId;
import com.totvs.tj.tcc.domain.emprestimo.EmprestimoRepository;
import com.totvs.tj.tcc.domain.responsavel.ResponsavelId;

public class EmprestimoTest {

    private final EmpresaId empresaId = EmpresaId.generate();
    private final ResponsavelId responsavelId = ResponsavelId.generate();
    
    EmprestimoRepository emprestimoRepository = new EmprestimoRepositoryMock();
    EmpresaRepository empresaRepository = new EmpresaRepositoryMock();

    @Test
    public void aoSolicitarEmprestimoDentroDoLimite() throws Exception {
        
        // GIVEN
        Empresa empresa = Empresa.builder()
                .id(empresaId)
                .cnpj("9999999999")
                .qtdFuncionarios(1500)
                .valor(15000)
                .responsaveId(responsavelId)
                .build();

        empresa.abrirConta();
        empresaRepository.save(empresa);

        EmprestimoApplicationService emprestimoApplication = new EmprestimoApplicationService(emprestimoRepository, empresaRepository);
        
        // WHEN
        SolicitaEmprestimoCommand cmd = SolicitaEmprestimoCommand.builder()
                .empresaId(empresaId)
                .valor(10)
                .build();
        
        EmprestimoId emprestimoId = emprestimoApplication.handle(cmd);
        
        Emprestimo emprestimo = emprestimoRepository.getOne(emprestimoId);

        // THEN
        assertNotNull(emprestimo);
        assertEquals(emprestimoId, emprestimo.getId());

    }
    
    @Test
    public void aoDevolver() {
        
        // GIVEN
        Empresa empresa = Empresa.builder()
                .id(empresaId)
                .cnpj("9999999999")
                .qtdFuncionarios(1500)
                .valor(15000)
                .responsaveId(responsavelId)
                .build();

        empresa.abrirConta();
        empresaRepository.save(empresa);

        EmprestimoApplicationService emprestimoApplication = new EmprestimoApplicationService(emprestimoRepository, empresaRepository);
        
        // WHEN
        SolicitaEmprestimoCommand cmd = SolicitaEmprestimoCommand.builder()
                .empresaId(empresaId)
                .valor(5000)
                .build();
        
        EmprestimoId emprestimoId = emprestimoApplication.handle(cmd);
        
        DevolverEmpresstimoCommand cmdDevolver = DevolverEmpresstimoCommand.builder()
                .empresaId(empresaId)
                .valor(2000)
                .build();
        
        emprestimoApplication.handle(cmdDevolver);
        
        empresa = empresaRepository.getOne(empresaId);
            
        //THEN
        Conta conta = empresa.getConta();
        assertTrue(conta.getSaldo() == 3000);
        
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
}
