package br.com.estoque.model;

public class Produto {
    // Atributos privados
    private int id;
    private String nome;
    private double preco;
    private int quantidade;

    // Foreign Key (Chave Estrangeira) para ligar com a Categoria
    private int categoriaId;

    // Construtor vazio
    public Produto() {
    }

    // Construtor para cadastro novo
    public Produto(String nome, double preco, int quantidade, int categoriaId) {
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.categoriaId = categoriaId;
    }

    // Construtor completo
    public Produto(int id, String nome, double preco, int quantidade, int categoriaId) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.categoriaId = categoriaId;
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

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }
}