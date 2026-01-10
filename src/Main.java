import br.com.estoque.dao.CategoriaDAO;
import br.com.estoque.model.Categoria;
import br.com.estoque.util.ConnectionFactory;

public class Main {
    public static void main(String[] args) {
        // 1. Garante que a tabela existe
        ConnectionFactory.getInstance().inicializarBanco();

        // 2. Cria um objeto Categoria (Modelo)
        Categoria novaCategoria = new Categoria("Eletrônicos", "Gadgets e dispositivos");

        // 3. Instancia o DAO (Quem grava no banco)
        CategoriaDAO dao = new CategoriaDAO();

        // 4. Salva
        dao.salvar(novaCategoria);

        // 5. Lista para confirmar que salvou
        System.out.println("\n--- Lista de Categorias no Banco ---");
        for (Categoria c : dao.listarTodos()) {
            System.out.println("ID: " + c.getId() + " | Nome: " + c.getNome());
        }
    }
}