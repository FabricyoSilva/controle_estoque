package br.com.estoque.view;

import br.com.estoque.dao.HistoricoDAO;
import br.com.estoque.dao.ProdutoDAO;
import br.com.estoque.dao.CategoriaDAO;
import br.com.estoque.model.Categoria;
import br.com.estoque.model.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ProdutoPanel extends JPanel {

    private JTextField txtNome;
    private JTextField txtPreco;
    private JTextField txtQtdInicial;
    private JComboBox<Categoria> comboCategoria;
    private JTable tabela;
    private DefaultTableModel tableModel;

    private ProdutoDAO produtoDAO;
    private CategoriaDAO categoriaDAO;
    private HistoricoDAO historicoDAO;

    public ProdutoPanel() {
        setLayout(new BorderLayout());
        produtoDAO = new ProdutoDAO();
        categoriaDAO = new CategoriaDAO();
        historicoDAO = new HistoricoDAO();

        // --- 1. FORMULÁRIO (Topo) ---
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Novo Produto"));

        txtNome = new JTextField();
        txtPreco = new JTextField();
        txtQtdInicial = new JTextField();
        comboCategoria = new JComboBox<>();

        JButton btnSalvar = new JButton("Salvar Novo Produto");

        // --- CORREÇÃO VISUAL BOTÃO SALVAR ---
        btnSalvar.setBackground(new Color(40, 167, 69)); // Verde
        btnSalvar.setForeground(Color.WHITE);            // Texto Branco
        btnSalvar.setFont(new Font("Arial", Font.BOLD, 14));
        btnSalvar.setOpaque(true);           // <--- OBRIGATÓRIO NO LINUX
        btnSalvar.setBorderPainted(false);   // <--- OBRIGATÓRIO NO LINUX
        btnSalvar.setFocusPainted(false);    // Remove linha pontilhada ao clicar
        // ------------------------------------

        formPanel.add(new JLabel("Nome:"));
        formPanel.add(txtNome);
        formPanel.add(new JLabel("Preço (R$):"));
        formPanel.add(txtPreco);
        formPanel.add(new JLabel("Qtd. Inicial:"));
        formPanel.add(txtQtdInicial);
        formPanel.add(new JLabel("Categoria:"));
        formPanel.add(comboCategoria);

        JPanel topoContainer = new JPanel(new BorderLayout());
        topoContainer.add(formPanel, BorderLayout.CENTER);
        topoContainer.add(btnSalvar, BorderLayout.SOUTH);

        add(topoContainer, BorderLayout.NORTH);

        // --- 2. TABELA (Centro) ---
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "Preço", "Qtd", "Categoria"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(tableModel);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // --- 3. PAINEL DE MOVIMENTAÇÃO (Rodapé) ---
        JPanel panelMovimentacao = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelMovimentacao.setBorder(BorderFactory.createTitledBorder("Gerenciar Estoque (Selecione um produto)"));

        JButton btnEntrada = new JButton(" (+) Entrada Estoque ");
        JButton btnSaida = new JButton(" (-) Saída Estoque ");

        // --- CORREÇÃO VISUAL BOTÕES RODAPÉ ---

        // Botão Entrada (Verde Escuro)
        btnEntrada.setBackground(new Color(25, 135, 84));
        btnEntrada.setForeground(Color.WHITE);
        btnEntrada.setFont(new Font("Arial", Font.BOLD, 14));
        btnEntrada.setOpaque(true);          // <--- IMPORTANTE
        btnEntrada.setBorderPainted(false);  // <--- IMPORTANTE
        btnEntrada.setFocusPainted(false);

        // Botão Saída (Vermelho Escuro)
        btnSaida.setBackground(new Color(220, 53, 69));
        btnSaida.setForeground(Color.WHITE);
        btnSaida.setFont(new Font("Arial", Font.BOLD, 14));
        btnSaida.setOpaque(true);            // <--- IMPORTANTE
        btnSaida.setBorderPainted(false);    // <--- IMPORTANTE
        btnSaida.setFocusPainted(false);
        // -------------------------------------

        panelMovimentacao.add(btnEntrada);
        panelMovimentacao.add(btnSaida);

        add(panelMovimentacao, BorderLayout.SOUTH);

        // --- AÇÕES ---
        carregarCategorias();
        atualizarTabela();

        btnSalvar.addActionListener(e -> salvarProduto());
        btnEntrada.addActionListener(e -> realizarMovimentacao("ENTRADA"));
        btnSaida.addActionListener(e -> realizarMovimentacao("SAIDA"));

        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tabela.getSelectedRow();
                if (row != -1) {
                    // Limpa o form de cadastro para evitar confusão
                    txtNome.setText("");
                    txtPreco.setText("");
                    txtQtdInicial.setText("");
                }
            }
        });
    }

    private void realizarMovimentacao(String tipo) {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela primeiro!");
            return;
        }

        int idProduto = (int) tabela.getValueAt(linha, 0);
        String nomeProduto = (String) tabela.getValueAt(linha, 1);
        int qtdAtual = (int) tabela.getValueAt(linha, 3);

        String mensagem = (tipo.equals("ENTRADA")) ? "Quanto vai ENTRAR de " + nomeProduto + "?" : "Quanto vai SAIR de " + nomeProduto + "?";
        String input = JOptionPane.showInputDialog(this, mensagem);

        if (input == null || input.isEmpty()) return;

        try {
            int qtdMovimentada = Integer.parseInt(input);
            if (qtdMovimentada <= 0) {
                JOptionPane.showMessageDialog(this, "A quantidade deve ser maior que zero.");
                return;
            }

            int novoSaldo = qtdAtual;
            if (tipo.equals("ENTRADA")) {
                novoSaldo += qtdMovimentada;
            } else {
                if (qtdMovimentada > qtdAtual) {
                    JOptionPane.showMessageDialog(this, "Erro: Estoque insuficiente! Você tem " + qtdAtual);
                    return;
                }
                novoSaldo -= qtdMovimentada;
            }

            // Busca objeto completo para atualizar
            List<Produto> lista = produtoDAO.findAll();
            for (Produto p : lista) {
                if (p.getId() == idProduto) {
                    p.setQuantity(novoSaldo);
                    produtoDAO.update(p);

                    try {
                        historicoDAO.add(p, tipo, qtdMovimentada);
                    } catch (Exception ex) {
                        System.out.println("Erro ao salvar histórico: " + ex.getMessage());
                    }
                    break;
                }
            }

            atualizarTabela();
            JOptionPane.showMessageDialog(this, "Estoque atualizado com sucesso!");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Digite apenas números inteiros!");
        }
    }

    private void salvarProduto() {
        try {
            String nome = txtNome.getText();
            double preco = Double.parseDouble(txtPreco.getText().replace(",", "."));
            int qtd = Integer.parseInt(txtQtdInicial.getText());

            // Verificação de categoria
            if (comboCategoria.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma categoria!");
                return;
            }
            Categoria cat = (Categoria) comboCategoria.getSelectedItem();

            Produto p = new Produto();
            p.setName(nome);
            p.setPrice(preco);
            p.setQuantity(qtd);
            p.setCategoryId(cat.getId());

            produtoDAO.insert(p);

            try {
                historicoDAO.add(p, "ENTRADA", qtd);
            } catch (Exception ignored) {}

            JOptionPane.showMessageDialog(this, "Produto salvo!");
            txtNome.setText("");
            txtPreco.setText("");
            txtQtdInicial.setText("");
            atualizarTabela();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: Verifique os campos numéricos e se todos estão preenchidos.");
        }
    }

    public void atualizarTabela() {
        tableModel.setRowCount(0);
        List<Produto> lista = produtoDAO.findAll();
        for (Produto p : lista) {
            String nomeCategoria = "Desconhecida";
            for(int i=0; i<comboCategoria.getItemCount(); i++){
                Categoria c = comboCategoria.getItemAt(i);
                if(c.getId() == p.getCategoryId()){
                    nomeCategoria = c.getName();
                    break;
                }
            }
            tableModel.addRow(new Object[]{p.getId(), p.getName(), p.getPrice(), p.getQuantity(), nomeCategoria});
        }
    }

    public void carregarCategorias() {
        comboCategoria.removeAllItems();
        List<Categoria> categorias = categoriaDAO.findAll();
        for (Categoria c : categorias) {
            comboCategoria.addItem(c);
        }
    }
}