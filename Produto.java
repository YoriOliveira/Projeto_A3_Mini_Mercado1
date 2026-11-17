import java.util.Locale;

public class Produto {
    private int codigo;
    private String nome;
    private double preco;
    private int estoque;

    public Produto(int codigo, String nome, double preco, int estoque) {
        this.codigo = codigo;
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
    }

    public int getCodigo() { return codigo; }
    public String getNome() { return nome; }
    public double getPreco() { return preco; }
    public int getEstoque() { return estoque; }

    public void setNome(String nome) { this.nome = nome; }
    public void setPreco(double preco) { this.preco = preco; }
    public void setEstoque(int estoque) { this.estoque = estoque; }

    // Linha no formato do CSV: codigo;nome;preco;estoque
    public String toCsv() {
        return codigo + ";" + nome + ";" + String.format(Locale.US, "%.2f", preco) + ";" + estoque;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "Código: %d | Nome: %s | Preço: R$ %.2f | Estoque: %d",
                codigo, nome, preco, estoque);
    }
}
