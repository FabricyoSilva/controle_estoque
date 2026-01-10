package br.com.estoque.view;

import br.com.estoque.dao.CategoriaDAO;
import br.com.estoque.dao.ProdutoDAO;
import br.com.estoque.model.Categoria;
import br.com.estoque.model.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class ProdutoPanel extends JPanel {

    // Componentes da tela
    private JTextField txtNome;
    private JTextField txtPreco;
    private JTextField txtQuantidade;
    private JComboBox<Categoria> cbCategoria; // O "Pulo do Gato" para o 1:N
    private JTable tabelaProdutos;
    private DefaultTableModel tableModel;

    private ProdutoDAO produtoDAO;
    private CategoriaDAO categoriaDAO;

    public ProdutoPanel() {
        setLayout(new BorderLayout());

        produtoDAO = new ProdutoDAO();
        categoriaDAO = new CategoriaDAO();

        // --- PAINEL DE FORMULÁRIO (Topo) ---
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtNome = new JTextField();
        txtPreco = new JTextField();
        txtQuantidade = new JTextField();
        cbCategoria = new JComboBox<>();

        // Validação: Impedir letras em campos numéricos (Requisito 4.4)
        txtPreco.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                String caracteres = "0987654321.";
                if (!caracteres.contains(e.getKeyChar() + "")) {
                    e.consume(); // Ignora a digitação se não for número
                }
            }
        });

        txtQuantidade.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                String caracteres = "0987654321";
                if (!caracteres.contains(e.getKeyChar() + "")) {
                    e.consume();
                }
            }
        });

        // Carregar categorias no ComboBox
        carregarCategorias();

        formPanel.add(new JLabel("Nome do Produto:"));
        formPanel.add(txtNome);
        formPanel.add(new JLabel("Preço (R$):"));
        formPanel.add(txtPreco);
        formPanel.add(new JLabel("Quantidade:"));
        formPanel.add(txtQuantidade);
        formPanel.add(new JLabel("Categoria:"));
        formPanel.add(cbCategoria);

        JButton btnSalvar = new JButton("Salvar Produto");
        btnSalvar.addActionListener(e -> salvarProduto());
        formPanel.add(new JLabel("")); // Espaço vazio
        formPanel.add(btnSalvar);

        add(formPanel, BorderLayout.NORTH);

        // --- TABELA (Centro) ---
        String[] colunas = {"ID", "Nome", "Preço", "Qtd", "Categoria ID"};
        tableModel = new DefaultTableModel(colunas, 0);
        tabelaProdutos = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelaProdutos);

        add(scrollPane, BorderLayout.CENTER);

        // Carregar dados iniciais
        atualizarTabela();
    }

    public void carregarCategorias() {
        List<Categoria> categorias = categoriaDAO.listarTodos();
        cbCategoria.removeAllItems();
        for (Categoria c : categorias) {
            cbCategoria.addItem(c); // O toString() da classe Categoria vai mostrar o nome aqui
        }
    }

    private void salvarProduto() {
        // Validação de campos vazios (Requisito 4.4)
        if (txtNome.getText().isEmpty() || txtPreco.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String nome = txtNome.getText();
            double preco = Double.parseDouble(txtPreco.getText());
            int qtd = Integer.parseInt(txtQuantidade.getText());
            Categoria categoriaSelecionada = (Categoria) cbCategoria.getSelectedItem();

            Produto p = new Produto(nome, preco, qtd, categoriaSelecionada.getId());
            produtoDAO.salvar(p);

            JOptionPane.showMessageDialog(this, "Produto salvo com sucesso!");

            // Limpar campos
            txtNome.setText("");
            txtPreco.setText("");
            txtQuantidade.setText("");

            // Atualizar tabela (Observer simplificado)
            atualizarTabela();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage());
        }
    }

    private void atualizarTabela() {
        tableModel.setRowCount(0); // Limpa a tabela
        List<Produto> produtos = produtoDAO.listarTodos();
        for (Produto p : produtos) {
            tableModel.addRow(new Object[]{
                    p.getId(), p.getNome(), p.getPreco(), p.getQuantidade(), p.getCategoriaId()
            });
        }
    }
}