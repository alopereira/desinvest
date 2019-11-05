package com.totvs.tj.tcc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.totvs.tj.tcc.ContaTest.ContaRepositoryMock;
import com.totvs.tj.tcc.app.empresa.EmpresaApplicationService;
import com.totvs.tj.tcc.app.empresa.SalvaEmpresaCommand;
import com.totvs.tj.tcc.domain.conta.ContaRepository;
import com.totvs.tj.tcc.domain.empresa.Empresa;
import com.totvs.tj.tcc.domain.empresa.EmpresaRepository;
import com.totvs.tj.tcc.domain.movimentacao.Movimentacao;
import com.totvs.tj.tcc.domain.movimentacao.MovimentacaoRepository;

public class EmpresaTest {

    private final String idEmpresa = Empresa.generate();
    
    EmpresaRepository empresaRepository = new EmpresaRepositoryMock();
    
    MovimentacaoRepository movimentacaoRepository = new MovimentacaoRepositoryMock();
    
    ContaRepository contaRepository = new ContaRepositoryMock();

    @Test
    public void aoCriarUmaEmpresa() throws Exception {

        // WHEN
        Empresa empresa = Empresa.builder()
                .id(idEmpresa)
                .valor(0)
                .qtdFuncionarios(0)
            .build();

        // THEN
        assertNotNull(empresa);
        assertEquals(idEmpresa, empresa.getId());
        assertEquals(idEmpresa.toString(), empresa.getId().toString());
        
    }
    
    @Test
    public void aoSalvarUmaEmpresa() throws Exception {

        // GIVEN        
        EmpresaApplicationService service = EmpresaApplicationService.builder()
                .empresaRepository(empresaRepository)
                .movimentacaoRepository(movimentacaoRepository)
                .contaRepository(contaRepository)
                .build();

        SalvaEmpresaCommand cmd = SalvaEmpresaCommand.builder()
                .valor(50000)
                .qtdFuncionarios(2000)
                .build();

        // WHEN
        String idEmpresa = service.handle(cmd);

        // THEN
        assertNotNull(idEmpresa);
    }
    

    static class EmpresaRepositoryMock implements EmpresaRepository {

        private final Map<String, Empresa> empresas = new LinkedHashMap<>();

        @Override
        public void save(Empresa empresa) {
            empresas.put(empresa.getId(), empresa);
        }

        @Override
        public Empresa getOne(String id) {
            return empresas.get(id);
        }
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
            // TODO Auto-generated method stub
            return null;
        }
    }
}
