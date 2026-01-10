package br.com.estoque.view;

import br.com.estoque.dao.HistoricoDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HistoricoPanel extends JPanel {
    private JTable tabela;
    private DefaultTableModel model;
    private HistoricoDAO dao;

    public HistoricoPanel() {
        setLayout(new BorderLayout());
        dao = new HistoricoDAO();

        String[] colunas = {"Data/Hora", "Tipo", "Produto", "Qtd"};
        model = new DefaultTableModel(colunas, 0);
        tabela = new JTable(model);

        JButton btnAtualizar = new JButton("Atualizar Histórico");
        btnAtualizar.addActionListener(e -> carregarDados());

        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(btnAtualizar, BorderLayout.NORTH);

        carregarDados();
    }

    public void carregarDados() {
        model.setRowCount(0);
        List<String[]> lista = dao.listarUltimosMovimentos();
        for (String[] linha : lista) {
            model.addRow(linha);
        }
    }
}