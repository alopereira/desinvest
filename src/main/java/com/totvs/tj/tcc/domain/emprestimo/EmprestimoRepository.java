package com.totvs.tj.tcc.domain.emprestimo;

public interface EmprestimoRepository {

    void save(Emprestimo movimentacao);
    
    Emprestimo getOne(EmprestimoId id);
    
}
