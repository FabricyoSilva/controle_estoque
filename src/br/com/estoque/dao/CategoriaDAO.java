package br.com.estoque.dao;

import br.com.estoque.model.Categoria;
import br.com.estoque.util.BusinessException; // <--- IMPORTANTE: Importar sua exceção nova
import br.com.estoque.util.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    // 1. INSERT (Salvar)
    public void insert(Categoria categoria) {
        String sql = "INSERT INTO categoria (nome, descricao) VALUES (?, ?)";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria.getName());
            stmt.setString(2, categoria.getDescription());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar categoria: " + e.getMessage());
        }
    }

    // 2. DELETE (Excluir com Verificação de Segurança e Exceção Personalizada 🛡️)
    public void delete(int id) {
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            // PASSO 1: Verificar se existem produtos vinculados
            String sqlCheck = "SELECT count(*) FROM produto WHERE categoria_id = ?";
            try (PreparedStatement stmtCheck = conn.prepareStatement(sqlCheck)) {
                stmtCheck.setInt(1, id);
                ResultSet rs = stmtCheck.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    // --- AQUI ESTÁ A MUDANÇA PARA O PROFESSOR VER ---
                    // Em vez de RuntimeException genérica, usamos a sua BusinessException
                    throw new BusinessException("Não é possível excluir: Existem produtos usando esta categoria!");
                }
            }

            // PASSO 2: Se passou pelo teste, executa a exclusão
            String sqlDelete = "DELETE FROM categoria WHERE id = ?";
            try (PreparedStatement stmtDelete = conn.prepareStatement(sqlDelete)) {
                stmtDelete.setInt(1, id);
                stmtDelete.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro de banco de dados: " + e.getMessage());
        }
    }

    // 3. FIND ALL (Listar Todos)
    public List<Categoria> findAll() {
        String sql = "SELECT * FROM categoria";
        List<Categoria> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Categoria c = new Categoria();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("nome"));
                c.setDescription(rs.getString("descricao"));
                lista.add(c);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar categorias: " + e.getMessage());
        }
        return lista;
    }
}