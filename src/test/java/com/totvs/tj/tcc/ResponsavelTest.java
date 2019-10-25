package com.totvs.tj.tcc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.totvs.tj.tcc.app.responsavel.ResponsavelApplicationService;
import com.totvs.tj.tcc.app.responsavel.SalvaResponsavelCommand;
import com.totvs.tj.tcc.domain.responsavel.Responsavel;
import com.totvs.tj.tcc.domain.responsavel.ResponsavelId;
import com.totvs.tj.tcc.domain.responsavel.ResponsavelRepository;

public class ResponsavelTest {

    private final ResponsavelId idResponsavel = ResponsavelId.generate();

    @Test
    public void aoCriarUmResponsavel() throws Exception {

        // WHEN
        Responsavel responsavel = Responsavel.builder()
                .id(idResponsavel)
                .supervisor("teste")
            .build();

        // THEN
        assertNotNull(responsavel);
        assertEquals(idResponsavel, responsavel.getId());
        
    }
    
    @Test
    public void aoSalvarUmResponsavel() throws Exception {

        // GIVEN
        ResponsavelRepository repository = new ResponsavelRepositoryMock();
        ResponsavelApplicationService service = new ResponsavelApplicationService(repository);

        SalvaResponsavelCommand cmd = SalvaResponsavelCommand.builder()
                .supervisor("Vitor")
                .build();

        // WHEN
        ResponsavelId idResponsavel = service.handle(cmd);

        // THEN
        assertNotNull(idResponsavel);
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
