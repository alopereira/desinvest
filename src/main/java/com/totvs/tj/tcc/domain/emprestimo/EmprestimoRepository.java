package com.totvs.tj.tcc.domain.emprestimo;

public interface EmprestimoRepository {

    void save(Emprestimo emprestimo);
    
    Emprestimo getOne(EmprestimoId id);
    
}
