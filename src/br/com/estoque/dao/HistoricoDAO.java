package br.com.estoque.dao;

import br.com.estoque.util.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HistoricoDAO {

    public void registrarMovimento(String tipo, String produto, int qtd) {
        String sql = "INSERT INTO historico (data_hora, tipo_movimento, produto_nome, quantidade) VALUES (?, ?, ?, ?)";

        // Pega data e hora atual formatada
        String dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dataHora);
            stmt.setString(2, tipo);
            stmt.setString(3, produto);
            stmt.setInt(4, qtd);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Log silencioso para não travar o sistema principal
        }
    }

    public List<String[]> listarUltimosMovimentos() {
        String sql = "SELECT * FROM historico ORDER BY id DESC"; // Do mais recente pro mais antigo
        List<String[]> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new String[]{
                        rs.getString("data_hora"),
                        rs.getString("tipo_movimento"),
                        rs.getString("produto_nome"),
                        String.valueOf(rs.getInt("quantidade"))
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}