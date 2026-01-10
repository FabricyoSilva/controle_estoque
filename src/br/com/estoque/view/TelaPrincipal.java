package br.com.estoque.view;

import br.com.estoque.util.ConnectionFactory;

import javax.swing.*;

public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        // Configurações básicas da janela
        setTitle("Sistema de Controle de Estoque - Vibe Coding");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza na tela

        // Gerenciador de Abas
        JTabbedPane abas = new JTabbedPane();

// 1. Criamos os painéis
        ProdutoPanel produtoPanel = new ProdutoPanel();
        CategoriaPanel categoriaPanel = new CategoriaPanel();
// ...
        HistoricoPanel historicoPanel = new HistoricoPanel();

// Adiciona listener para quando mudar de aba, o histórico atualizar
        abas.addChangeListener(e -> {
            if (abas.getSelectedIndex() == 2) { // Se for a aba 2 (Histórico)
                historicoPanel.carregarDados();
            }
        });


// 2. O PULO DO GATO (Observer):
// Dizemos: "CategoriaPanel, quando seus dados mudarem, avise o ProdutoPanel para recarregar"
        categoriaPanel.setListener(() -> {
            produtoPanel.carregarCategorias();
        });

// 3. Adicionamos nas abas
        abas.add("Gerenciar Produtos", produtoPanel);
        abas.add("Gerenciar Categorias", categoriaPanel);
        abas.add("Histórico", historicoPanel);
        add(abas);
    }

    // Método main para rodar a tela direto (substitui aquele Main.java de teste)
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // --- ADICIONE ESTA LINHA AQUI ---
        // Isso garante que o banco e a tabela 'historico' sejam criados antes da tela abrir
        ConnectionFactory.getInstance().inicializarBanco();
        // -------------------------------

        SwingUtilities.invokeLater(() -> {
            new TelaPrincipal().setVisible(true);
        });
    }
}