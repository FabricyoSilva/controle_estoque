package br.com.estoque.model;

public class Produto {
    private int id;
    private String name;
    private double price;
    private int quantity;
    private int categoryId;

    // Construtor vazio
    public Produto() {}

    // Construtor completo
    public Produto(int id, String name, double price, int quantity, int categoryId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.categoryId = categoryId;
    }

    // --- GETTERS E SETTERS CORRETOS ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    // Este era o principal causador do erro:
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
}