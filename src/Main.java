import br.com.estoque.util.ConnectionFactory;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        System.out.println("Iniciando teste de conexão...");

        // 1. Tenta rodar a inicialização do banco
        ConnectionFactory.getInstance().inicializarBanco();

        // 2. Tenta pegar uma conexão para ver se não dá erro
        try {
            Connection con = ConnectionFactory.getInstance().getConnection();
            if (con != null) {
                System.out.println("Conexão obtida com sucesso!");
                con.close(); // Fecha a conexão para não travar o arquivo
            }
        } catch (SQLException e) {
            System.err.println("Erro ao conectar: " + e.getMessage());
            e.printStackTrace();
        }
    }
}