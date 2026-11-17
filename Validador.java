// Funcoes de validacao usadas nos cadastros de clientes.
public class Validador {

    // Remove tudo que nao for numero: "123.456.789-09" -> "12345678909"
    public static String somenteNumeros(String texto) {
        return texto.replaceAll("[^0-9]", "");
    }

    // CPF valido: 11 numeros, nao pode ter todos iguais e os 2 digitos verificadores devem bater
    public static boolean cpfValido(String cpf) {
        cpf = somenteNumeros(cpf);
        if (cpf.length() != 11) return false;

        boolean todosIguais = true;
        for (int i = 1; i < 11; i++) {
            if (cpf.charAt(i) != cpf.charAt(0)) {
                todosIguais = false;
                break;
            }
        }
        if (todosIguais) return false; // ex: 111.111.111-11

        int digito1 = calcularDigitoCpf(cpf, 9);
        int digito2 = calcularDigitoCpf(cpf, 10);
        return digito1 == cpf.charAt(9) - '0' && digito2 == cpf.charAt(10) - '0';
    }

    // Calcula um digito verificador usando os 'tamanho' primeiros numeros do CPF
    private static int calcularDigitoCpf(String cpf, int tamanho) {
        int soma = 0;
        int peso = tamanho + 1;
        for (int i = 0; i < tamanho; i++) {
            soma += (cpf.charAt(i) - '0') * peso;
            peso--;
        }
        int resto = soma % 11;
        return (resto < 2) ? 0 : 11 - resto;
    }

    // Mostra o CPF no formato 123.456.789-09
    public static String formatarCpf(String cpf) {
        String n = somenteNumeros(cpf);
        if (n.length() != 11) return cpf;
        return n.substring(0, 3) + "." + n.substring(3, 6) + "." + n.substring(6, 9) + "-" + n.substring(9);
    }

    // E-mail valido: texto@dominio.algo, sem espacos
    public static boolean emailValido(String email) {
        return email.matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");
    }
}
