package br.com.estoque.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectionFactory {

    // 1. Atributo estático para armazenar a instância única (SINGLETON)
    private static ConnectionFactory instance;

    // URL de conexão com o SQLite. O arquivo 'estoque.db' será criado na raiz do projeto.
    private static final String URL = "jdbc:sqlite:estoque.db";

    // 2. Construtor privado para impedir 'new ConnectionFactory()' fora daqui (SINGLETON)
    private ConnectionFactory() {
        // Opcional: Carregar o driver manualmente (necessário em versões antigas do Java)
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 3. Método público estático para recuperar a instância única (SINGLETON)
    public static ConnectionFactory getInstance() {
        if (instance == null) {
            instance = new ConnectionFactory();
        }
        return instance;
    }

    // 4. Método Factory para fabricar conexões (FACTORY / JDBC PURO)
    public Connection getConnection() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(URL);
            // Configuração para suportar Foreign Keys no SQLite (por padrão vem desativado)
            try (java.sql.Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar com o banco de dados: " + e.getMessage());
        }
    }

    // Método auxiliar para criar as tabelas automaticamente se não existirem (PORTABILIDADE)
    public void inicializarBanco() {
        String sqlCategoria = "CREATE TABLE IF NOT EXISTS categoria ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nome TEXT NOT NULL, "
                + "descricao TEXT)";

        String sqlProduto = "CREATE TABLE IF NOT EXISTS produto ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nome TEXT NOT NULL, "
                + "preco REAL NOT NULL, "
                + "quantidade INTEGER NOT NULL, "
                + "categoria_id INTEGER, "
                + "FOREIGN KEY(categoria_id) REFERENCES categoria(id))";

        try (Connection conn = getConnection();
             PreparedStatement st1 = conn.prepareStatement(sqlCategoria);
             PreparedStatement st2 = conn.prepareStatement(sqlProduto)) {

            st1.execute();
            st2.execute();
            System.out.println("Banco de dados e tabelas verificados com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}