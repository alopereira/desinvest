package com.totvs.tj.tcc.app.responsavel;

import org.springframework.stereotype.Service;

import com.totvs.tj.tcc.domain.responsavel.Responsavel;
import com.totvs.tj.tcc.domain.responsavel.ResponsavelId;
import com.totvs.tj.tcc.domain.responsavel.ResponsavelRepository;

@Service
public class ResponsavelApplicationService {
    
    private ResponsavelRepository repository;
    
    public ResponsavelApplicationService(ResponsavelRepository repository) {
        this.repository = repository;
    }
    
    public ResponsavelId handle(SalvaResponsavelCommand cmd) {
        
        ResponsavelId idResponsavel = ResponsavelId.generate();
        
        Responsavel responsavel = Responsavel.builder()
                .id(idResponsavel)
                .supervisor(cmd.getSupervisor())
            .build();
        
        repository.save(responsavel);
        
        return idResponsavel; 
    }
    
}
