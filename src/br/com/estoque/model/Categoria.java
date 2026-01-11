package br.com.estoque.model;

public class Categoria {
    private int id;
    private String name;
    private String description;

    // Construtor vazio (obrigatório)
    public Categoria() {}

    // Construtor completo
    public Categoria(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // --- GETTERS E SETTERS EM INGLÊS (Para corrigir os erros vermelhos) ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Importante para o ComboBox funcionar
    @Override
    public String toString() {
        return this.name;
    }
}