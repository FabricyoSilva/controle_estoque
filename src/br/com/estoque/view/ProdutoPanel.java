package br.com.estoque.view;

import br.com.estoque.dao.CategoriaDAO;
import br.com.estoque.dao.HistoricoDAO;
import br.com.estoque.dao.ProdutoDAO;
import br.com.estoque.model.Categoria;
import br.com.estoque.model.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
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
    private JComboBox<Categoria> cbCategoria;
    private JTable tabelaProdutos;
    private DefaultTableModel tableModel;
    private JLabel lblValorTotal; // Label do Dashboard Financeiro

    // DAOs
    private ProdutoDAO produtoDAO;
    private CategoriaDAO categoriaDAO;
    private HistoricoDAO historicoDAO;

    public ProdutoPanel() {
        setLayout(new BorderLayout());

        produtoDAO = new ProdutoDAO();
        categoriaDAO = new CategoriaDAO();
        historicoDAO = new HistoricoDAO();

        // --- PAINEL DE FORMULÁRIO (Topo) ---
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtNome = new JTextField();
        txtPreco = new JTextField();
        cbCategoria = new JComboBox<>();

        // Configuração do campo de Quantidade (LIVRE PARA DIGITAÇÃO)
        txtQuantidade = new JTextField();
        txtQuantidade.setText("0");
        txtQuantidade.setHorizontalAlignment(JTextField.CENTER);
        // NOTA: A linha setEditable(false) foi removida para permitir digitação livre!

        // Painelzinho para agrupar o campo de texto e os botões de + e -
        JPanel panelQtd = new JPanel(new BorderLayout(5, 0));
        JPanel panelBotoes = new JPanel(new GridLayout(1, 2, 2, 0));

        JButton btnEntrada = new JButton("+");
        btnEntrada.setBackground(new Color(150, 255, 150)); // Verde claro
        btnEntrada.setToolTipText("Adicionar Estoque");

        JButton btnSaida = new JButton("-");
        btnSaida.setBackground(new Color(255, 150, 150)); // Vermelho claro
        btnSaida.setToolTipText("Remover Estoque");

        // --- LÓGICA HÍBRIDA DOS BOTÕES ---
        btnEntrada.addActionListener(e -> {
            if (tabelaProdutos.getSelectedRow() != -1) {
                // Cenário 1: Linha selecionada -> Atualiza Banco (Entrada de Nota Fiscal, etc)
                atualizarEstoque("ENTRADA");
            } else {
                // Cenário 2: Cadastro Novo -> Soma no campo de texto para facilitar
                try {
                    int qtd = txtQuantidade.getText().isEmpty() ? 0 : Integer.parseInt(txtQuantidade.getText());
                    txtQuantidade.setText(String.valueOf(qtd + 1));
                } catch (NumberFormatException ex) {
                    txtQuantidade.setText("1");
                }
            }
        });

        btnSaida.addActionListener(e -> {
            if (tabelaProdutos.getSelectedRow() != -1) {
                // Cenário 1: Linha selecionada -> Atualiza Banco (Venda/Saída)
                atualizarEstoque("SAIDA");
            } else {
                // Cenário 2: Cadastro Novo -> Subtrai no campo de texto
                try {
                    int qtd = txtQuantidade.getText().isEmpty() ? 0 : Integer.parseInt(txtQuantidade.getText());
                    if (qtd > 0) txtQuantidade.setText(String.valueOf(qtd - 1));
                } catch (NumberFormatException ex) {
                    txtQuantidade.setText("0");
                }
            }
        });

        panelBotoes.add(btnEntrada);
        panelBotoes.add(btnSaida);

        panelQtd.add(txtQuantidade, BorderLayout.CENTER);
        panelQtd.add(panelBotoes, BorderLayout.EAST);

        // Validação: Impedir letras no Preço
        txtPreco.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                String caracteres = "0987654321.";
                if (!caracteres.contains(e.getKeyChar() + "")) {
                    e.consume();
                }
            }
        });

        // Validação: Impedir letras na Quantidade (caso digite manualmente)
        txtQuantidade.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                String caracteres = "0987654321";
                if (!caracteres.contains(e.getKeyChar() + "")) {
                    e.consume();
                }
            }
        });

        carregarCategorias();

        // Adicionando componentes ao Grid
        formPanel.add(new JLabel("Nome do Produto:"));
        formPanel.add(txtNome);

        formPanel.add(new JLabel("Preço (R$):"));
        formPanel.add(txtPreco);

        formPanel.add(new JLabel("Estoque (Digite ou use +/-):"));
        formPanel.add(panelQtd); // Adiciona o painel composto aqui

        formPanel.add(new JLabel("Categoria:"));
        formPanel.add(cbCategoria);

        JButton btnSalvar = new JButton("Salvar Novo Produto");
        btnSalvar.addActionListener(e -> salvarProduto());
        formPanel.add(new JLabel(""));
        formPanel.add(btnSalvar);

        add(formPanel, BorderLayout.NORTH);

        // --- TABELA (Centro) ---
        String[] colunas = {"ID", "Nome", "Preço", "Qtd", "Categoria ID"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Bloqueia edição direta na tabela
            }
        };

        tabelaProdutos = new JTable(tableModel);

        // Listener para mudar o comportamento dos botões quando clica na tabela
        tabelaProdutos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean linhaSelecionada = tabelaProdutos.getSelectedRow() != -1;
                if (linhaSelecionada) {
                    btnEntrada.setToolTipText("Adicionar ao Estoque do item selecionado");
                    btnSaida.setToolTipText("Vender item selecionado");
                } else {
                    btnEntrada.setToolTipText("Aumentar quantidade do cadastro");
                    btnSaida.setToolTipText("Diminuir quantidade do cadastro");
                }
            }
        });

        // ALERTA DE ESTOQUE BAIXO (Vermelho se < 5)
        tabelaProdutos.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Pega a quantidade da coluna 3
                int quantidade = Integer.parseInt(table.getModel().getValueAt(row, 3).toString());

                if (quantidade < 5) {
                    c.setBackground(new Color(255, 180, 180)); // Vermelho Suave
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
                    c.setForeground(isSelected ? table.getSelectionForeground() : Color.BLACK);
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabelaProdutos);
        add(scrollPane, BorderLayout.CENTER);

        // --- DASHBOARD FINANCEIRO (Rodapé) ---
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statsPanel.setBackground(new Color(230, 240, 255));
        statsPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));

        lblValorTotal = new JLabel("Carregando...");
        lblValorTotal.setFont(new Font("Arial", Font.BOLD, 14));
        lblValorTotal.setForeground(new Color(0, 100, 0));

        statsPanel.add(lblValorTotal);
        add(statsPanel, BorderLayout.SOUTH);

        // Carregar dados iniciais
        atualizarTabela();
    }

    public void carregarCategorias() {
        List<Categoria> categorias = categoriaDAO.listarTodos();
        cbCategoria.removeAllItems();
        for (Categoria c : categorias) {
            cbCategoria.addItem(c);
        }
    }

    private void salvarProduto() {
        if (txtNome.getText().isEmpty() || txtPreco.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha nome e preço!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String nome = txtNome.getText();
            double preco = Double.parseDouble(txtPreco.getText());

            // Pega o valor digitado ou ajustado pelos botões
            int qtd = txtQuantidade.getText().isEmpty() ? 0 : Integer.parseInt(txtQuantidade.getText());

            Categoria categoriaSelecionada = (Categoria) cbCategoria.getSelectedItem();

            Produto p = new Produto(nome, preco, qtd, categoriaSelecionada.getId());
            produtoDAO.salvar(p);

            // Registra histórico inicial
            historicoDAO.registrarMovimento("CADASTRO INICIAL", p.getNome(), qtd);

            JOptionPane.showMessageDialog(this, "Produto salvo com sucesso!");

            // Limpa os campos
            txtNome.setText("");
            txtPreco.setText("");
            txtQuantidade.setText("0");

            // IMPORTANTE: Limpa a seleção da tabela para os botões voltarem a controlar o campo de texto
            tabelaProdutos.clearSelection();

            atualizarTabela();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage());
        }
    }

    private void atualizarEstoque(String tipo) {
        int linhaSelecionada = tabelaProdutos.getSelectedRow();
        if (linhaSelecionada == -1) return;

        int idProduto = (int) tabelaProdutos.getValueAt(linhaSelecionada, 0);
        String nomeProd = (String) tabelaProdutos.getValueAt(linhaSelecionada, 1);
        int qtdAtual = (int) tabelaProdutos.getValueAt(linhaSelecionada, 3);

        String input = JOptionPane.showInputDialog(this, "Digite a quantidade para " + tipo + ":");
        if (input == null || input.isEmpty()) return;

        try {
            int qtdMovimentada = Integer.parseInt(input);
            if (qtdMovimentada <= 0) {
                JOptionPane.showMessageDialog(this, "A quantidade deve ser maior que zero!");
                return;
            }

            int novaQtd = 0;
            if (tipo.equals("ENTRADA")) {
                novaQtd = qtdAtual + qtdMovimentada;
            } else if (tipo.equals("SAIDA")) {
                if (qtdMovimentada > qtdAtual) {
                    JOptionPane.showMessageDialog(this, "Estoque insuficiente! Saldo atual: " + qtdAtual);
                    return;
                }
                novaQtd = qtdAtual - qtdMovimentada;
            }

            Produto p = produtoDAO.buscarPorId(idProduto);
            p.setQuantidade(novaQtd);
            produtoDAO.atualizar(p);

            historicoDAO.registrarMovimento(tipo, nomeProd, qtdMovimentada);

            atualizarTabela();
            JOptionPane.showMessageDialog(this, "Estoque atualizado!");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Digite apenas números inteiros!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }

    private void atualizarTabela() {
        tableModel.setRowCount(0);
        List<Produto> produtos = produtoDAO.listarTodos();
        for (Produto p : produtos) {
            tableModel.addRow(new Object[]{
                    p.getId(), p.getNome(), p.getPreco(), p.getQuantidade(), p.getCategoriaId()
            });
        }

        double total = produtoDAO.calcularValorTotalEstoque();
        lblValorTotal.setText(String.format("Patrimônio Total em Estoque: R$ %.2f   ", total));
    }
}