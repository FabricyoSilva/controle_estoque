package br.com.estoque.view;

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

// 2. O PULO DO GATO (Observer):
// Dizemos: "CategoriaPanel, quando seus dados mudarem, avise o ProdutoPanel para recarregar"
        categoriaPanel.setListener(() -> {
            produtoPanel.carregarCategorias();
        });

// 3. Adicionamos nas abas
        abas.add("Gerenciar Produtos", produtoPanel);
        abas.add("Gerenciar Categorias", categoriaPanel);

        add(abas);
    }

    // Método main para rodar a tela direto (substitui aquele Main.java de teste)
    public static void main(String[] args) {
        // LookAndFeel para ficar com a cara do sistema operacional (menos feio)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new TelaPrincipal().setVisible(true);
        });
    }
}