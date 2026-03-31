import java.util.*;

public class Palavra {
    // Dicionário de palavras em português
    private static final Set<String> DICIONARIO = new HashSet<>(Arrays.asList(
            "AMOR", "PAZ", "LUZ", "SOL", "MAR", "CÉU", "FLOR", "VIDA",
            "SONHO", "POESIA", "ALMA", "CORPO", "TEMPO", "MUNDO", "ALEGRIA",
            "TRISTEZA", "BELO", "AR", "FOGO", "ÁGUA", "TERRA", "VENTO",
            "ESTRELA", "LUA", "INFINITO", "ETERNIDADE", "INSTANTE"));

    private String texto;
    private int linha;
    private int coluna;
    private int direcao; // 0=horizontal, 1=vertical
    private int tamanho;

    public Palavra(String texto, int linha, int coluna, int direcao) {
        this.texto = texto;
        this.linha = linha;
        this.coluna = coluna;
        this.direcao = direcao;
        this.tamanho = texto.length();
    }

    // Verifica se uma sequência forma palavra
    public static boolean ehPalavra(String sequencia) {
        return DICIONARIO.contains(sequencia);
    }

    // Busca palavras em todas as direções
    public static List<Palavra> encontrarPalavras(Letra[][] grade, int linhas, int colunas) {
        List<Palavra> palavrasEncontradas = new ArrayList<>();

        // Busca horizontal
        for (int i = 0; i < linhas; i++) {
            StringBuilder linhaStr = new StringBuilder();
            for (int j = 0; j < colunas; j++) {
                linhaStr.append(grade[i][j].getCaractere());
            }
            palavrasEncontradas.addAll(buscarNaLinha(linhaStr.toString(), i, 0));
        }

        // Busca vertical
        for (int j = 0; j < colunas; j++) {
            StringBuilder colunaStr = new StringBuilder();
            for (int i = 0; i < linhas; i++) {
                colunaStr.append(grade[i][j].getCaractere());
            }
            palavrasEncontradas.addAll(buscarNaColuna(colunaStr.toString(), j, 1));
        }

        return palavrasEncontradas;
    }

    private static List<Palavra> buscarNaLinha(String linhaStr, int linha, int direcao) {
        List<Palavra> encontradas = new ArrayList<>();
        for (int inicio = 0; inicio < linhaStr.length(); inicio++) {
            for (int fim = inicio + 2; fim <= linhaStr.length(); fim++) {
                String substring = linhaStr.substring(inicio, fim);
                if (ehPalavra(substring)) {
                    encontradas.add(new Palavra(substring, linha, inicio, direcao));
                }
            }
        }
        return encontradas;
    }

    private static List<Palavra> buscarNaColuna(String colunaStr, int coluna, int direcao) {
        List<Palavra> encontradas = new ArrayList<>();
        for (int inicio = 0; inicio < colunaStr.length(); inicio++) {
            for (int fim = inicio + 2; fim <= colunaStr.length(); fim++) {
                String substring = colunaStr.substring(inicio, fim);
                if (ehPalavra(substring)) {
                    encontradas.add(new Palavra(substring, inicio, coluna, direcao));
                }
            }
        }
        return encontradas;
    }

    // Getters
    public String getTexto() {
        return texto;
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    public int getDirecao() {
        return direcao;
    }

    public int getTamanho() {
        return tamanho;
    }
}
