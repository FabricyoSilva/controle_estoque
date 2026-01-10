package br.com.estoque.model;

public class Categoria {
    // Atributos privados (Encapsulamento)
    private int id;
    private String nome;
    private String descricao;

    // Construtor vazio (obrigatório para algumas bibliotecas e boas práticas)
    public Categoria() {
    }

    // Construtor para criar nova categoria (sem ID, pois o banco gera automático)
    public Categoria(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    // Construtor completo (para quando buscamos do banco)
    public Categoria(int id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    // Método toString() para facilitar a exibição em listas (Combobox) depois
    @Override
    public String toString() {
        return this.nome;
    }
}