package br.com.estoque.dao;

import br.com.estoque.model.Produto;
import br.com.estoque.util.ConnectionFactory;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HistoricoDAO {

    public void add(Produto p, String tipo, int quantidade) {
        String sql = "INSERT INTO historico (produto_id, tipo_movimentacao, quantidade, data_hora) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, p.getId());
            stmt.setString(2, tipo);
            stmt.setInt(3, quantidade);
            stmt.setString(4, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao salvar histórico: " + e.getMessage());
        }
    }

    // O Painel procura por ESTE nome exato:
    public List<String[]> listarUltimosMovimentos() {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT h.id, p.nome, h.tipo_movimentacao, h.quantidade, h.data_hora " +
                "FROM historico h " +
                "LEFT JOIN produto p ON h.produto_id = p.id " +
                "ORDER BY h.id DESC"; // Mostra os mais recentes primeiro

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new String[]{
                        rs.getString("data_hora"),        // Coluna 1: Data
                        rs.getString("tipo_movimentacao"),// Coluna 2: Tipo
                        rs.getString("nome"),             // Coluna 3: Produto
                        String.valueOf(rs.getInt("quantidade")) // Coluna 4: Qtd
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}