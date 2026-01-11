package br.com.estoque.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionFactory {

    private static ConnectionFactory instance;
    private Connection connection;
    // Nome do arquivo do banco
    private static final String URL = "jdbc:sqlite:estoque.db";

    private ConnectionFactory() {}

    public static ConnectionFactory getInstance() {
        if (instance == null) {
            instance = new ConnectionFactory();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar no banco: " + e.getMessage());
        }
        return connection;
    }

    // --- AQUI ESTAVA O PROBLEMA: Atualizamos as tabelas ---
    public void inicializarBanco() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // 1. Tabela Categoria
            stmt.execute("CREATE TABLE IF NOT EXISTS categoria (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nome TEXT NOT NULL, " +
                    "descricao TEXT)");

            // 2. Tabela Produto (Com nomes em Português no banco para compatibilidade)
            stmt.execute("CREATE TABLE IF NOT EXISTS produto (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nome TEXT NOT NULL, " +
                    "preco REAL, " +
                    "quantidade INTEGER, " +
                    "categoria_id INTEGER, " +
                    "FOREIGN KEY(categoria_id) REFERENCES categoria(id))");

            // 3. Tabela Histórico (ATUALIZADA com tipo_movimentacao)
            stmt.execute("CREATE TABLE IF NOT EXISTS historico (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "produto_id INTEGER, " +
                    "tipo_movimentacao TEXT, " + // <--- A COLUNA QUE FALTAVA
                    "quantidade INTEGER, " +
                    "data_hora TEXT, " +
                    "FOREIGN KEY(produto_id) REFERENCES produto(id))");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}