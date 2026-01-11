package br.com.estoque.dao;

import br.com.estoque.model.Produto;
import br.com.estoque.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    // 1. INSERT (CORRIGIDO: Agora pega o ID gerado pelo banco)
    public void insert(Produto p) {
        String sql = "INSERT INTO produto (nome, preco, quantidade, categoria_id) VALUES (?, ?, ?, ?)";

        // Adicionamos 'Statement.RETURN_GENERATED_KEYS' para pedir o ID de volta
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, p.getName());
            stmt.setDouble(2, p.getPrice());
            stmt.setInt(3, p.getQuantity());
            stmt.setInt(4, p.getCategoryId());

            int linhasAfetadas = stmt.executeUpdate();

            // Se salvou com sucesso, pegamos o ID novo
            if (linhasAfetadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idGerado = generatedKeys.getInt(1);
                        p.setId(idGerado); // Atualiza o objeto com o ID real!
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar produto: " + e.getMessage());
        }
    }

    // 2. UPDATE (Atualizar)
    public void update(Produto p) {
        String sql = "UPDATE produto SET nome=?, preco=?, quantidade=?, categoria_id=? WHERE id=?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getName());
            stmt.setDouble(2, p.getPrice());
            stmt.setInt(3, p.getQuantity());
            stmt.setInt(4, p.getCategoryId());
            stmt.setInt(5, p.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar produto: " + e.getMessage());
        }
    }

    // 3. FIND ALL (Listar Todos)
    public List<Produto> findAll() {
        String sql = "SELECT * FROM produto";
        List<Produto> produtos = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Produto p = new Produto();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("nome"));
                p.setPrice(rs.getDouble("preco"));
                p.setQuantity(rs.getInt("quantidade"));
                p.setCategoryId(rs.getInt("categoria_id"));
                produtos.add(p);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar produtos: " + e.getMessage());
        }
        return produtos;
    }

    // 4. DELETE (Excluir)
    public void delete(int id) {
        String sql = "DELETE FROM produto WHERE id = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir produto: " + e.getMessage());
        }
    }
}