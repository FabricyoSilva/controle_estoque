package br.com.estoque.view;

import br.com.estoque.dao.CategoriaDAO;
import br.com.estoque.model.Categoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class CategoriaPanel extends JPanel {

    private JTextField txtNome;
    private JTextField txtDescricao;
    private JTable tabelaCategorias;
    private DefaultTableModel tableModel;

    // Usamos Runnable para ser compatível com a TelaPrincipal
    private Runnable listener;

    public void setListener(Runnable listener) {
        this.listener = listener;
    }

    public CategoriaPanel() {
        setLayout(new BorderLayout());

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

        // --- BOTÃO EXCLUIR (Estilo Vermelho) ---
        JButton btnExcluir = new JButton("Excluir Selecionada");
        btnExcluir.setBackground(new Color(220, 53, 69));
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.setFont(new Font("Arial", Font.BOLD, 12));
        btnExcluir.setFocusPainted(false);
        btnExcluir.setBorderPainted(false);
        btnExcluir.setOpaque(true);
        // ---------------------------------------

        // Ações dos botões
        btnSalvar.addActionListener(e -> salvarCategoria());
        btnExcluir.addActionListener(e -> excluirCategoria());

        formPanel.add(btnExcluir);
        formPanel.add(btnSalvar);

        add(formPanel, BorderLayout.NORTH);

        // --- TABELA (Centro) ---
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "Descrição"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaCategorias = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelaCategorias);
        add(scrollPane, BorderLayout.CENTER);

        // Clique na tabela para preencher os campos
        tabelaCategorias.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tabelaCategorias.getSelectedRow();
                if (row != -1) {
                    txtNome.setText(tabelaCategorias.getValueAt(row, 1).toString());
                    txtDescricao.setText(tabelaCategorias.getValueAt(row, 2).toString());
                }
            }
        });

        atualizarTabela();
    }

    private void salvarCategoria() {
        if (txtNome.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // CORREÇÃO: Usando construtor vazio e setters (Corrigindo o erro da linha 70)
        Categoria c = new Categoria();
        c.setName(txtNome.getText());
        c.setDescription(txtDescricao.getText());

        try {
            // CORREÇÃO: Usando 'insert' (Corrigindo o erro da linha 72)
            new CategoriaDAO().insert(c);

            JOptionPane.showMessageDialog(this, "Categoria salva!");
            txtNome.setText("");
            txtDescricao.setText("");
            atualizarTabela();

            // Avisa a TelaPrincipal
            if (listener != null) {
                listener.run();
            }
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

        int id = (int) tabelaCategorias.getValueAt(linhaSelecionada, 0);

        int resposta = JOptionPane.showConfirmDialog(this,
                "Tem certeza? Se houver produtos nesta categoria, a exclusão falhará.",
                "Confirmação", JOptionPane.YES_NO_OPTION);

        if (resposta == JOptionPane.YES_OPTION) {
            try {
                // CORREÇÃO: Usando 'delete'
                new CategoriaDAO().delete(id);

                atualizarTabela();
                JOptionPane.showMessageDialog(this, "Categoria excluída.");

                if (listener != null) listener.run();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Não foi possível excluir. Verifique se existem produtos vinculados a esta categoria.",
                        "Erro de Integridade", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void atualizarTabela() {
        tableModel.setRowCount(0);
        // CORREÇÃO: Usando 'findAll', 'getName' e 'getDescription'
        List<Categoria> lista = new CategoriaDAO().findAll();
        for (Categoria c : lista) {
            tableModel.addRow(new Object[]{c.getId(), c.getName(), c.getDescription()});
        }
    }
}