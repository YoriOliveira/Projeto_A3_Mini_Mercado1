# Projeto A3 - Sistema de Mini Mercado

Sistema de terminal (console) em Java para gerenciar produtos, clientes, compras e estoque de um mini mercado.

## Funcionalidades

- **Produtos:** cadastrar, listar, buscar, alterar e remover
- **Clientes:** cadastrar, listar, buscar, alterar e remover
- **Realizar Compras:** identifica cliente por CPF (opcional), valida estoque, calcula subtotais e total
- **Controle de Estoque:** listar, repor e ver produtos com estoque baixo
- **Persistência:** dados salvos em `produtos.csv` e `clientes.csv` e carregados ao iniciar

## Estrutura

```
MiniMercado/
├── Main.java        # menus, CRUDs, compra, estoque e leitura/gravação CSV
├── Produto.java     # classe Produto (codigo, nome, preco, estoque)
├── Cliente.java     # classe Cliente (cpf, nome, telefone, email)
├── produtos.csv     # dados de exemplo
├── clientes.csv     # dados de exemplo
└── README.md
```

## Como executar

Requisito: JDK 8 ou superior.

Pelo terminal, dentro da pasta do projeto:

```
javac *.java
java Main
```

> Execute sempre a partir da pasta onde estão os arquivos `.csv`, pois eles são lidos e gravados no diretório atual. Se não existirem, são criados automaticamente ao cadastrar o primeiro registro.

Pelo NetBeans: crie um projeto Java, copie os três arquivos `.java` para o pacote padrão e execute `Main`.

## Formato dos arquivos CSV

`produtos.csv`
```
codigo;nome;preco;estoque
1;Coca-Cola;5.50;10
```

`clientes.csv`
```
cpf;nome;telefone;email
12345678900;Joao Silva;(11)99999-9999;joao@email.com
```

## Observações

- Não usa interface gráfica, banco de dados nem bibliotecas externas.
- Os dados são mantidos em vetores (arrays) que crescem automaticamente.
- Não é permitido usar `;` nos textos digitados, pois é o separador do CSV.
