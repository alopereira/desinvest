package com.totvs.tj.tcc.domain.responsavel;

import static lombok.AccessLevel.PRIVATE;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@Entity
@AllArgsConstructor(access = PRIVATE)
public class Responsavel {
    
    
    private ResponsavelId id;
    
    private String supervisor;
    
    @Id
    public String getIdValue() {
        return this.id.getValue();
    }
    
}
