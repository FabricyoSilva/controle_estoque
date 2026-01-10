package br.com.estoque.view;

import br.com.estoque.dao.CategoriaDAO;
import br.com.estoque.model.Categoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CategoriaPanel extends JPanel {

    private JTextField txtNome;
    private JTextField txtDescricao;
    private JTable tabelaCategorias;
    private DefaultTableModel tableModel;
    private CategoriaDAO categoriaDAO;

    public CategoriaPanel() {
        setLayout(new BorderLayout());
        categoriaDAO = new CategoriaDAO();

        // --- FORMULÁRIO (Topo) ---
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtNome = new JTextField();
        txtDescricao = new JTextField();

        formPanel.add(new JLabel("Nome da Categoria:"));
        formPanel.add(txtNome);
        formPanel.add(new JLabel("Descrição:"));
        formPanel.add(txtDescricao);

        JButton btnSalvar = new JButton("Salvar Categoria");
        btnSalvar.addActionListener(e -> salvarCategoria());

        // Adicionando botão de Excluir para cumprir o requisito de CRUD Completo
        JButton btnExcluir = new JButton("Excluir Selecionada");
        btnExcluir.setBackground(new Color(255, 100, 100)); // Vermelho claro
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.addActionListener(e -> excluirCategoria());

        formPanel.add(btnExcluir);
        formPanel.add(btnSalvar);

        add(formPanel, BorderLayout.NORTH);

        // --- TABELA (Centro) ---
        String[] colunas = {"ID", "Nome", "Descrição"};
        tableModel = new DefaultTableModel(colunas, 0);
        tabelaCategorias = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelaCategorias);
        add(scrollPane, BorderLayout.CENTER);

        atualizarTabela();
    }

    private void salvarCategoria() {
        if (txtNome.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Categoria c = new Categoria(txtNome.getText(), txtDescricao.getText());
        try {
            categoriaDAO.salvar(c);
            JOptionPane.showMessageDialog(this, "Categoria salva!");
            txtNome.setText("");
            txtDescricao.setText("");
            atualizarTabela();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage());
        }
    }

    private void excluirCategoria() {
        int linhaSelecionada = tabelaCategorias.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma categoria na tabela para excluir.");
            return;
        }

        // Pega o ID da coluna 0 da linha selecionada
        int id = (int) tabelaCategorias.getValueAt(linhaSelecionada, 0);

        // Pergunta de confirmação (Interface amigável)
        int resposta = JOptionPane.showConfirmDialog(this, "Tem certeza? Isso pode apagar produtos vinculados!", "Confirmação", JOptionPane.YES_NO_OPTION);

        if (resposta == JOptionPane.YES_OPTION) {
            try {
                categoriaDAO.deletar(id);
                atualizarTabela();
                JOptionPane.showMessageDialog(this, "Categoria excluída.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir (verifique se há produtos vinculados): " + e.getMessage());
            }
        }
    }

    private void atualizarTabela() {
        tableModel.setRowCount(0);
        List<Categoria> lista = categoriaDAO.listarTodos();
        for (Categoria c : lista) {
            tableModel.addRow(new Object[]{c.getId(), c.getNome(), c.getDescricao()});
        }
    }
}