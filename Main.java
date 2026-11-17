import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    static final String ARQ_PRODUTOS = "produtos.csv";
    static final String ARQ_CLIENTES = "clientes.csv";

    static Scanner sc = new Scanner(System.in);

    // Vetores (com contadores) para guardar os dados em memoria
    static Produto[] produtos = new Produto[10];
    static int qtdProdutos = 0;
    static Cliente[] clientes = new Cliente[10];
    static int qtdClientes = 0;

    public static void main(String[] args) {
        carregarProdutos();
        carregarClientes();

        int op;
        do {
            cabecalho("MINI MERCADO");
            System.out.println("1. Produtos");
            System.out.println("2. Clientes");
            System.out.println("3. Realizar Compras");
            System.out.println("4. Controle Estoque");
            System.out.println("0. Sair");
            op = lerInt("Escolha uma Opção: ");

            switch (op) {
                case 1: menuProdutos(); break;
                case 2: menuClientes(); break;
                case 3: realizarCompra(); break;
                case 4: menuEstoque(); break;
                case 0: System.out.println("Encerrando o sistema. Até logo!"); break;
                default: System.out.println("Opção inválida!"); pausar();
            }
        } while (op != 0);
    }

    // ===================== UTILITARIOS =====================

    static void cabecalho(String titulo) {
        System.out.println();
        System.out.println("======================================");
        System.out.println("  " + titulo);
        System.out.println("======================================");
    }

    static void pausar() {
        System.out.println();
        System.out.print("Pressione ENTER para continuar...");
        sc.nextLine();
    }

    static int lerInt(String msg) {
        while (true) {
            System.out.print(msg);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido! Digite um número inteiro.");
            }
        }
    }

    static double lerDouble(String msg) {
        while (true) {
            System.out.print(msg);
            try {
                return Double.parseDouble(sc.nextLine().trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido! Digite um número (ex: 5.50).");
            }
        }
    }

    static String lerTexto(String msg) {
        while (true) {
            System.out.print(msg);
            String t = sc.nextLine().trim();
            if (!t.isEmpty() && !t.contains(";")) {
                return t;
            }
            System.out.println("Texto inválido! Não deixe vazio e não use ponto e vírgula (;).");
        }
    }

    // ===================== PERSISTENCIA CSV =====================

    static void carregarProdutos() {
        File f = new File(ARQ_PRODUTOS);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linha = br.readLine(); // cabecalho
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] p = linha.split(";");
                if (p.length < 4) continue;
                try {
                    adicionarProdutoVetor(new Produto(Integer.parseInt(p[0].trim()), p[1].trim(),
                            Double.parseDouble(p[2].trim()), Integer.parseInt(p[3].trim())));
                } catch (NumberFormatException e) {
                    System.out.println("Linha ignorada em " + ARQ_PRODUTOS + ": " + linha);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler " + ARQ_PRODUTOS + ": " + e.getMessage());
        }
    }

    static void salvarProdutos() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQ_PRODUTOS))) {
            pw.println("codigo;nome;preco;estoque");
            for (int i = 0; i < qtdProdutos; i++) {
                pw.println(produtos[i].toCsv());
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar " + ARQ_PRODUTOS + ": " + e.getMessage());
        }
    }

    static void carregarClientes() {
        File f = new File(ARQ_CLIENTES);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linha = br.readLine(); // cabecalho
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] p = linha.split(";");
                if (p.length < 4) continue;
                adicionarClienteVetor(new Cliente(p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim()));
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler " + ARQ_CLIENTES + ": " + e.getMessage());
        }
    }

    static void salvarClientes() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQ_CLIENTES))) {
            pw.println("cpf;nome;telefone;email");
            for (int i = 0; i < qtdClientes; i++) {
                pw.println(clientes[i].toCsv());
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar " + ARQ_CLIENTES + ": " + e.getMessage());
        }
    }

    // ===================== VETORES =====================

    static void adicionarProdutoVetor(Produto p) {
        if (qtdProdutos == produtos.length) {
            produtos = Arrays.copyOf(produtos, produtos.length * 2);
        }
        produtos[qtdProdutos++] = p;
    }

    static void adicionarClienteVetor(Cliente c) {
        if (qtdClientes == clientes.length) {
            clientes = Arrays.copyOf(clientes, clientes.length * 2);
        }
        clientes[qtdClientes++] = c;
    }

    // Retorna a posicao do produto no vetor ou -1
    static int buscarProduto(int codigo) {
        for (int i = 0; i < qtdProdutos; i++) {
            if (produtos[i].getCodigo() == codigo) return i;
        }
        return -1;
    }

    // Retorna a posicao do cliente no vetor ou -1
    static int buscarCliente(String cpf) {
        for (int i = 0; i < qtdClientes; i++) {
            if (clientes[i].getCpf().equals(cpf)) return i;
        }
        return -1;
    }

    // ===================== CRUD PRODUTOS =====================

    static void menuProdutos() {
        int op;
        do {
            cabecalho("PRODUTOS");
            System.out.println("1. Cadastrar Produto");
            System.out.println("2. Listar Produtos");
            System.out.println("3. Buscar Produto");
            System.out.println("4. Alterar Produto");
            System.out.println("5. Remover Produto");
            System.out.println("0. Voltar");
            op = lerInt("Escolha uma opção: ");

            switch (op) {
                case 1: cadastrarProduto(); break;
                case 2: listarProdutos(); break;
                case 3: buscarProdutoMenu(); break;
                case 4: alterarProduto(); break;
                case 5: removerProduto(); break;
                case 0: break;
                default: System.out.println("Opção inválida!"); pausar();
            }
        } while (op != 0);
    }

    static void cadastrarProduto() {
        cabecalho("CADASTRAR PRODUTO");
        int codigo = lerInt("Digite o Código: ");
        if (codigo <= 0) {
            System.out.println("O código deve ser maior que zero.");
            pausar();
            return;
        }
        if (buscarProduto(codigo) != -1) {
            System.out.println("Ja existe um produto com esse código!");
            pausar();
            return;
        }
        String nome = lerTexto("Digite o Nome: ");
        double preco = lerDouble("Digite o Preço: ");
        int estoque = lerInt("Digite o Estoque: ");
        if (preco < 0 || estoque < 0) {
            System.out.println("Preço e estoque não podem ser negativos.");
            pausar();
            return;
        }
        adicionarProdutoVetor(new Produto(codigo, nome, preco, estoque));
        salvarProdutos();
        System.out.println("\nProduto cadastrado com sucesso!");
        pausar();
    }

    static void listarProdutos() {
        cabecalho("LISTA DE PRODUTOS");
        if (qtdProdutos == 0) {
            System.out.println("Nenhum produto cadastrado.");
        }
        for (int i = 0; i < qtdProdutos; i++) {
            System.out.println(produtos[i]);
        }
        pausar();
    }

    static void buscarProdutoMenu() {
        cabecalho("BUSCAR PRODUTO");
        int pos = buscarProduto(lerInt("Digite o Código: "));
        if (pos == -1) {
            System.out.println("Produto não encontrado.");
        } else {
            System.out.println(produtos[pos]);
        }
        pausar();
    }

    static void alterarProduto() {
        cabecalho("ALTERAR PRODUTO");
        int pos = buscarProduto(lerInt("Digite o código do produto: "));
        if (pos == -1) {
            System.out.println("Produto não encontrado.");
            pausar();
            return;
        }
        System.out.println("Atual: " + produtos[pos]);
        String nome = lerTexto("Novo Nome: ");
        double preco = lerDouble("Novo Preço: ");
        int estoque = lerInt("Novo Estoque: ");
        if (preco < 0 || estoque < 0) {
            System.out.println("Preço e estoque não podem ser negativos. Nada foi alterado.");
            pausar();
            return;
        }
        produtos[pos].setNome(nome);
        produtos[pos].setPreco(preco);
        produtos[pos].setEstoque(estoque);
        salvarProdutos();
        System.out.println("\nProduto alterado com sucesso!");
        pausar();
    }

    static void removerProduto() {
        cabecalho("REMOVER PRODUTO");
        int pos = buscarProduto(lerInt("Digite o código do produto: "));
        if (pos == -1) {
            System.out.println("Produto não encontrado.");
            pausar();
            return;
        }
        System.out.println(produtos[pos]);
        System.out.print("Confirma a remoção? (S/N): ");
        if (sc.nextLine().trim().equalsIgnoreCase("S")) {
            for (int i = pos; i < qtdProdutos - 1; i++) {
                produtos[i] = produtos[i + 1];
            }
            produtos[--qtdProdutos] = null;
            salvarProdutos();
            System.out.println("Produto removido com sucesso!");
        } else {
            System.out.println("Remoção cancelada.");
        }
        pausar();
    }

    // ===================== CRUD CLIENTES =====================

    static void menuClientes() {
        int op;
        do {
            cabecalho("CLIENTES");
            System.out.println("1. Cadastrar Cliente");
            System.out.println("2. Listar Clientes");
            System.out.println("3. Buscar Cliente");
            System.out.println("4. Alterar Cliente");
            System.out.println("5. Remover Cliente");
            System.out.println("0. Voltar");
            op = lerInt("Escolha uma opção: ");

            switch (op) {
                case 1: cadastrarCliente(); break;
                case 2: listarClientes(); break;
                case 3: buscarClienteMenu(); break;
                case 4: alterarCliente(); break;
                case 5: removerCliente(); break;
                case 0: break;
                default: System.out.println("Opção inválida!"); pausar();
            }
        } while (op != 0);
    }

    static void cadastrarCliente() {
        cabecalho("CADASTRAR CLIENTE");
        String cpf = lerTexto("Digite o CPF: ");
        if (buscarCliente(cpf) != -1) {
            System.out.println("Ja existe um cliente com esse CPF!");
            pausar();
            return;
        }
        String nome = lerTexto("Digite o Nome: ");
        String telefone = lerTexto("Digite o Telefone: ");
        String email = lerTexto("Digite o Email: ");
        adicionarClienteVetor(new Cliente(cpf, nome, telefone, email));
        salvarClientes();
        System.out.println("\nCliente cadastrado com sucesso!");
        pausar();
    }

    static void listarClientes() {
        cabecalho("LISTA DE CLIENTES");
        if (qtdClientes == 0) {
            System.out.println("Nenhum cliente cadastrado.");
        }
        for (int i = 0; i < qtdClientes; i++) {
            System.out.println(clientes[i]);
        }
        pausar();
    }

    static void buscarClienteMenu() {
        cabecalho("BUSCAR CLIENTE");
        int pos = buscarCliente(lerTexto("Digite o CPF: "));
        if (pos == -1) {
            System.out.println("Cliente não encontrado.");
        } else {
            System.out.println(clientes[pos]);
        }
        pausar();
    }

    static void alterarCliente() {
        cabecalho("ALTERAR CLIENTE");
        int pos = buscarCliente(lerTexto("Digite o CPF do cliente: "));
        if (pos == -1) {
            System.out.println("Cliente não encontrado.");
            pausar();
            return;
        }
        System.out.println("Atual: " + clientes[pos]);
        clientes[pos].setNome(lerTexto("Novo Nome: "));
        clientes[pos].setTelefone(lerTexto("Novo Telefone: "));
        clientes[pos].setEmail(lerTexto("Novo Email: "));
        salvarClientes();
        System.out.println("\nCliente alterado com sucesso!");
        pausar();
    }

    static void removerCliente() {
        cabecalho("REMOVER CLIENTE");
        int pos = buscarCliente(lerTexto("Digite o CPF do cliente: "));
        if (pos == -1) {
            System.out.println("Cliente não encontrado.");
            pausar();
            return;
        }
        System.out.println(clientes[pos]);
        System.out.print("Confirma a remoção? (S/N): ");
        if (sc.nextLine().trim().equalsIgnoreCase("S")) {
            for (int i = pos; i < qtdClientes - 1; i++) {
                clientes[i] = clientes[i + 1];
            }
            clientes[--qtdClientes] = null;
            salvarClientes();
            System.out.println("Cliente removido com sucesso!");
        } else {
            System.out.println("Remoção cancelada.");
        }
        pausar();
    }

    // ===================== REALIZAR COMPRA =====================

    static void realizarCompra() {
        cabecalho("REALIZAR COMPRA");

        // Identificacao do cliente
        String nomeCliente = "Cliente não cadastrado";
        System.out.print("Sou cadastrado? (S/N): ");
        String resp = sc.nextLine().trim();
        if (resp.equalsIgnoreCase("S")) {
            int pos = buscarCliente(lerTexto("Digite o CPF: "));
            if (pos == -1) {
                System.out.println("Cliente não encontrado. A compra continuará sem cadastro.");
            } else {
                nomeCliente = clientes[pos].getNome();
                System.out.println("Bem-vindo(a), " + nomeCliente + "!");
            }
        }

        // Carrinho: vetores paralelos (produto e quantidade)
        Produto[] itens = new Produto[10];
        int[] quantidades = new int[10];
        int qtdItens = 0;

        String continuar;
        do {
            int codigo = lerInt("\nCódigo do produto: ");
            int pos = buscarProduto(codigo);
            if (pos == -1) {
                System.out.println("Erro: produto não existe.");
            } else {
                Produto p = produtos[pos];
                int quantidade = lerInt("Quantidade desejada: ");

                // Quanto desse produto ja esta no carrinho?
                int idx = -1;
                for (int i = 0; i < qtdItens; i++) {
                    if (itens[i] == p) idx = i;
                }
                int jaNoCarrinho = (idx == -1) ? 0 : quantidades[idx];

                if (quantidade <= 0) {
                    System.out.println("Erro: a quantidade deve ser maior que zero.");
                } else if (jaNoCarrinho + quantidade > p.getEstoque()) {
                    System.out.println("Erro: estoque insuficiente! Disponível: "
                            + (p.getEstoque() - jaNoCarrinho) + ". Item não adicionado.");
                } else if (idx != -1) {
                    quantidades[idx] += quantidade;
                    System.out.println("Quantidade atualizada no carrinho.");
                } else {
                    if (qtdItens == itens.length) {
                        itens = Arrays.copyOf(itens, itens.length * 2);
                        quantidades = Arrays.copyOf(quantidades, quantidades.length * 2);
                    }
                    itens[qtdItens] = p;
                    quantidades[qtdItens] = quantidade;
                    qtdItens++;
                    System.out.println("Item adicionado ao carrinho.");
                }
            }
            System.out.print("Adicionar outro produto? (S/N): ");
            continuar = sc.nextLine().trim();
        } while (continuar.equalsIgnoreCase("S"));

        if (qtdItens == 0) {
            System.out.println("\nNenhum item no carrinho. Compra cancelada.");
            pausar();
            return;
        }

        // Resumo da compra
        cabecalho("RESUMO DA COMPRA");
        System.out.println("Cliente: " + nomeCliente);
        System.out.println();
        System.out.printf(Locale.US, "%-20s %5s %12s %12s%n", "Produto", "Qtd", "Unitario", "Subtotal");
        double total = 0;
        for (int i = 0; i < qtdItens; i++) {
            double subtotal = itens[i].getPreco() * quantidades[i];
            total += subtotal;
            System.out.printf(Locale.US, "%-20s %5d %12.2f %12.2f%n",
                    itens[i].getNome(), quantidades[i], itens[i].getPreco(), subtotal);
        }
        System.out.println("--------------------------------------------------");
        System.out.printf(Locale.US, "TOTAL DA COMPRA: R$ %.2f%n", total);

        // Baixa no estoque e salva no CSV
        for (int i = 0; i < qtdItens; i++) {
            itens[i].setEstoque(itens[i].getEstoque() - quantidades[i]);
        }
        salvarProdutos();
        System.out.println("\nCompra finalizada com sucesso! Estoque atualizado.");
        pausar();
    }

    // ===================== CONTROLE DE ESTOQUE =====================

    static void menuEstoque() {
        int op;
        do {
            cabecalho("CONTROLE DE ESTOQUE");
            System.out.println("1. Listar Estoque");
            System.out.println("2. Repor Estoque");
            System.out.println("3. Produtos com Estoque Baixo (menos de 5)");
            System.out.println("0. Voltar");
            op = lerInt("Escolha uma opção: ");

            switch (op) {
                case 1: listarEstoque(false); break;
                case 2: reporEstoque(); break;
                case 3: listarEstoque(true); break;
                case 0: break;
                default: System.out.println("Opção inválida!"); pausar();
            }
        } while (op != 0);
    }

    static void listarEstoque(boolean apenasBaixo) {
        cabecalho(apenasBaixo ? "ESTOQUE BAIXO" : "ESTOQUE ATUAL");
        boolean achou = false;
        for (int i = 0; i < qtdProdutos; i++) {
            if (!apenasBaixo || produtos[i].getEstoque() < 5) {
                System.out.printf("Código: %d | %s | Estoque: %d%n",
                        produtos[i].getCodigo(), produtos[i].getNome(), produtos[i].getEstoque());
                achou = true;
            }
        }
        if (!achou) {
            System.out.println("Nenhum produto para exibir.");
        }
        pausar();
    }

    static void reporEstoque() {
        cabecalho("REPOR ESTOQUE");
        int pos = buscarProduto(lerInt("Digite o código do produto: "));
        if (pos == -1) {
            System.out.println("Produto não encontrado.");
            pausar();
            return;
        }
        System.out.println(produtos[pos]);
        int qtd = lerInt("Quantidade a adicionar: ");
        if (qtd <= 0) {
            System.out.println("A quantidade deve ser maior que zero.");
        } else {
            produtos[pos].setEstoque(produtos[pos].getEstoque() + qtd);
            salvarProdutos();
            System.out.println("Novo estoque: " + produtos[pos].getEstoque());
        }
        pausar();
    }
}
