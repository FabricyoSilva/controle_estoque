package br.com.estoque.dao;

import java.util.List;

// O <T> significa que essa interface aceita qualquer tipo de objeto (Genérico)
public interface GenericDAO<T> {
    void salvar(T objeto);
    void atualizar(T objeto);
    void deletar(int id);
    List<T> listarTodos();
    T buscarPorId(int id);
}