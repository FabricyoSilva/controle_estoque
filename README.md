# 📦 Sistema de Gestão de Estoque com Auditoria e Dashboard Financeiro

Este projeto é uma aplicação Desktop robusta para controle de estoque, desenvolvida como Trabalho Final da disciplina de Programação Orientada a Objetos (POO).

Muito além de um CRUD trivial, o sistema implementa **regras de negócio reais**, controle financeiro, auditoria de movimentações e alertas visuais para tomada de decisão, cumprindo rigorosamente a arquitetura **MVC** e os padrões de projeto exigidos.

---
## 👤 Autor

[Fabricyo Silva] - Desenvolvedor Full Stack (Backend, Frontend e Banco de Dados)

---

## 🚀 Diferenciais e Regras de Negócio

O sistema resolve problemas práticos de gestão através das seguintes funcionalidades:

### 1. 💰 Dashboard Financeiro (KPI)
- **Problema:** "Quanto dinheiro a empresa tem parado no estoque?"
- **Solução:** O sistema calcula em tempo real o patrimônio total (soma de `Preço * Quantidade` de todos os itens) e exibe em destaque no rodapé da aplicação.

### 2. 📦 Fluxo de Movimentação com Validação
- **Problema:** Em sistemas triviais, o usuário apaga um número e digita outro, gerando erros e furos de estoque.
- **Solução:** A edição direta é bloqueada na tabela. O usuário deve realizar operações de **"Entrada"** ou **"Saída"**.
- **Regra de Negócio:** O sistema impede que o estoque fique negativo (não é possível realizar uma saída maior que o saldo atual).

### 3. 📝 Auditoria e Rastreabilidade (Histórico)
- **Problema:** Necessidade de saber quem alterou o estoque, quando e por quê.
- **Solução:** Toda operação de cadastro, entrada ou saída gera automaticamente um registro indelével na aba "Histórico", contendo data, hora, tipo de movimento e o produto afetado.

### 4. 🚨 Gestão Visual de Risco
- **Solução:** Produtos com estoque crítico (abaixo de 5 unidades) são destacados automaticamente em **vermelho** na tabela, facilitando a identificação de itens que precisam de reposição.

---

## 🛠️ Arquitetura e Tecnologias

O projeto foi construído utilizando **Java (Swing)** e banco de dados embarcado, focando na portabilidade e independência de infraestrutura externa.

### Padrões de Projeto Aplicados (Requisitos Técnicos):
1.  **MVC (Model-View-Controller):** Separação clara de responsabilidades.
2.  **DAO (Data Access Object):** Isolamento total das queries SQL e regras de persistência.
3.  **Singleton:** Implementado na `ConnectionFactory` para garantir uma instância única de conexão com o banco SQLite.
4.  **Observer:** Comunicação reativa entre abas. Ao cadastrar uma nova Categoria, a tela de Produtos atualiza seu ComboBox automaticamente, sem reiniciar o sistema.

---

## ⚙️ Como Executar o Projeto

1.  **Pré-requisitos:** Ter o JDK (Java Development Kit) instalado.
2.  **Bibliotecas:** O projeto utiliza o driver JDBC do SQLite e SLF4J (logs). Certifique-se de que os JARs na pasta `lib` estão no Build Path da sua IDE.
3.  **Execução:**
    - Localize a classe principal: `br.com.estoque.view.TelaPrincipal`.
    - Execute o método `main`.
    - **Nota:** Não é necessário configurar o banco de dados previamente. O sistema detecta a primeira execução e cria o arquivo `estoque.db` e todas as tabelas (`produto`, `categoria`, `historico`) automaticamente.

---

## 📂 Estrutura do Projeto

A organização dos pacotes segue o padrão MVC (Model-View-Controller) para facilitar a manutenção e escalabilidade:

```text
src
└── br
    └── com
        └── estoque
            │
            ├── 📦 dao         # Data Access Objects (Isolamento do SQL)
            │   ├── CategoriaDAO.java
            │   ├── HistoricoDAO.java
            │   ├── ProdutoDAO.java
            │   └── GenericDAO.java
            │
            ├── 📦 model       # Modelagem de Dados (Entidades)
            │   ├── Categoria.java
            │   └── Produto.java
            │
            ├── 📦 view        # Interface Gráfica (GUI)
            │   ├── TelaPrincipal.java
            │   ├── ProdutoPanel.java
            │   ├── CategoriaPanel.java
            │   └── HistoricoPanel.java
            │
            └── 📦 util        # Utilitários e Infraestrutura
                ├── ConnectionFactory.java  # Singleton de Conexão
                └── DataListener.java       # Interface Observer
```
---

## 📸 Screenshots

Tela Principal: Dashboard Financeiro e Alerta de Estoque Baixo

<img width="780" height="588" alt="Captura de tela 2026-01-10 165940" src="https://github.com/user-attachments/assets/fd13e5d1-14c9-41b7-a1ac-525795742208" />

Cadastro e Gerenciamento de Categorias

<img width="782" height="591" alt="Captura de tela 2026-01-10 165958" src="https://github.com/user-attachments/assets/f7f4bbd9-7b5e-42da-b551-ff8b98b23363" />

Aba de Histórico de Movimentações

<img width="781" height="590" alt="Captura de tela 2026-01-10 170003" src="https://github.com/user-attachments/assets/37ab8201-314a-4b7a-8a11-40de58f58ab4" />


---

## 📐 Diagrama de Classes (UML)

<img width="1605" height="1255" alt="diagrama_estoque" src="https://github.com/user-attachments/assets/2f94bd11-76d9-4aaa-a9dc-1dbf5d9ef7b2" />

---

