# 📦 Sistema Integrado de Controle de Estoque e Financeiro 

Este projeto é uma solução **Full Desktop Application**.

O software transcende o conceito básico de cadastro, funcionando como um **ERP simplificado** que integra gestão de mercadorias, controle financeiro em tempo real, categorização dinâmica de produtos e auditoria completa de movimentações. O foco foi simular um ambiente corporativo real, com regras de negócio rígidas que garantem a integridade dos dados e do patrimônio.

---
## 👤 Autor

[Fabricyo Silva] - Desenvolvedor Full Stack (Backend, Frontend e Banco de Dados) - ADS2

---

## 🚀 Funcionalidades e Regras de Negócio (Diferenciais)

O sistema foi arquitetado para resolver quatro pilares fundamentais da gestão empresarial:

### 1. 📊 Gestão Financeira Integrada (KPIs)

- **Funcionalidade:** Cálculo automático e em tempo real do valor total imobilizado em estoque (Preço x Quantidade).
- **Impacto:** Permite ao gestor saber instantaneamente quanto capital está investido em mercadorias, auxiliando na tomada de decisão financeira.

### 2. 🗂️ Gestão Dinâmica de Categorias
O sistema implementa um relacionamento **1:N (Um-para-Muitos)** flexível e reativo.
- **Funcionalidade:** O usuário pode criar novas categorias (ex: "Eletrônicos", "Perecíveis") a qualquer momento.
- **Reatividade (Observer):** Ao cadastrar uma nova categoria, todas as interfaces de cadastro de produtos são atualizadas instantaneamente para exibir a nova opção, sem necessidade de reiniciar o sistema.

### 3. ⚖️ Controle de Fluxo (Entrada/Saída) com Travas de Segurança
Implementação de regras de negócio para evitar erros operacionais comuns.
- **Regra de Negócio:** O sistema bloqueia a edição manual arbitrária de quantidades. O usuário é forçado a realizar operações formais de **"Compra/Entrada"** ou **"Venda/Saída"**.
- **Validação de Saldo:** É matematicamente impossível realizar uma saída superior ao saldo atual (Estoque Negativo Bloqueado), garantindo consistência contábil.

### 4. 📝 Auditoria e Rastreabilidade (Histórico de Movimentações)
Segurança e transparência para o negócio.
- **Funcionalidade:** Cada operação realizada no sistema (desde o cadastro inicial até pequenos ajustes de estoque) gera um registro imutável (Log).
- **Detalhes:** O histórico grava a data exata, o tipo de operação, o produto afetado e a quantidade movimentada, permitindo rastrear "quem fez o quê".

### 5. 🚨 Gestão Visual de Risco
- **Funcionalidade:** Produtos com estoque crítico (abaixo de 5 unidades) são destacados visualmente em **vermelho** na listagem, servindo como um alerta passivo para reposição imediata.

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

## 📥 Instalação do Artefato Executável

Baixe a versão final compilada aqui: [Download v1.0 (Executável)](https://github.com/FabricyoSilva/controle_estoque/releases/tag/v1.0)

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

