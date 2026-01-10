import br.com.estoque.dao.CategoriaDAO;
import br.com.estoque.dao.ProdutoDAO;
import br.com.estoque.model.Categoria;
import br.com.estoque.model.Produto;
import br.com.estoque.util.ConnectionFactory;

public class Main {
    public static void main(String[] args) {
        // 1. Inicializa o banco
        ConnectionFactory.getInstance().inicializarBanco();

        // 2. Cria e Salva uma Categoria
        System.out.println("--- Teste Categoria ---");
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        Categoria cat = new Categoria("Informática", "Computadores e acessórios");
        categoriaDAO.salvar(cat);

        // Vamos pegar a lista para descobrir qual ID foi gerado para essa categoria
        // (Num sistema real, você pegaria o ID retornado ou selecionado na tela)
        int idCategoriaGerado = 0;
        for(Categoria c : categoriaDAO.listarTodos()) {
            System.out.println("Categoria encontrada: " + c.getNome() + " (ID: " + c.getId() + ")");
            idCategoriaGerado = c.getId(); // Pega o último ID para usar no produto
        }

        // 3. Cria e Salva um Produto ligado a essa Categoria
        System.out.println("\n--- Teste Produto ---");
        ProdutoDAO produtoDAO = new ProdutoDAO();

        // Observe o ultimo argumento: passamos o idCategoriaGerado
        Produto prod = new Produto("Notebook Gamer", 4500.00, 10, idCategoriaGerado);
        produtoDAO.salvar(prod);

        // 4. Lista os Produtos
        System.out.println("\n--- Lista de Produtos no Estoque ---");
        for (Produto p : produtoDAO.listarTodos()) {
            System.out.println("ID: " + p.getId() +
                    " | Nome: " + p.getNome() +
                    " | Preço: R$ " + p.getPreco() +
                    " | Categoria ID: " + p.getCategoriaId());
        }
    }
}