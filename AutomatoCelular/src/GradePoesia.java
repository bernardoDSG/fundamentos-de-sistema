import java.util.*;

public class GradePoesia {
    private int linhas;
    private int colunas;
    private Letra[][] celulas;
    private Letra[][] proximaGeracao;
    private int[][] idade; // idade da letra na posição
    private List<Palavra> palavrasAtuais;
    private Random random = new Random();

    public GradePoesia(int linhas, int colunas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.celulas = new Letra[linhas][colunas];
        this.proximaGeracao = new Letra[linhas][colunas];
        this.idade = new int[linhas][colunas];
        this.palavrasAtuais = new ArrayList<>();

        inicializarVazio();
    }

    // Inicia tudo vazio
    private void inicializarVazio() {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                celulas[i][j] = Letra.VAZIO;
                idade[i][j] = 0;
            }
        }
    }

    // Gera poesia aleatória
    public void gerarPoesiaAleatoria() {
        // 30% de chance de ter letra em cada célula
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                if (random.nextDouble() < 0.3) {
                    celulas[i][j] = Letra.letraAleatoria();
                    idade[i][j] = random.nextInt(10);
                } else {
                    celulas[i][j] = Letra.VAZIO;
                    idade[i][j] = 0;
                }
            }
        }
    }

    // Cria um poema pré-definido
    public void criarPoemaExemplo() {
        inicializarVazio();

        // "O SOL NASCE NO MAR"
        String[] poema = {
                "O   SOL   NASCE",
                "NO  MAR   AZUL",
                "A   LUZ   BRILHA",
                "EM  POESIA  VIVA"
        };

        for (int i = 0; i < Math.min(poema.length, linhas); i++) {
            String linha = poema[i];
            for (int j = 0; j < Math.min(linha.length(), colunas); j++) {
                char c = linha.charAt(j);
                if (c != ' ') {
                    celulas[i][j] = encontrarLetraPorChar(c);
                    idade[i][j] = 5;
                }
            }
        }
    }

    private Letra encontrarLetraPorChar(char c) {
        for (Letra letra : Letra.values()) {
            if (letra.getCaractere() == c) {
                return letra;
            }
        }
        return Letra.VAZIO;
    }

    // Calcula próxima geração (regras poéticas)
    public void calcularProximaGeracao() {
        palavrasAtuais = Palavra.encontrarPalavras(celulas, linhas, colunas);

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                Letra atual = celulas[i][j];

                // Regra 1: Letras envelhecem e mudam
                if (atual != Letra.VAZIO) {
                    idade[i][j]++;

                    // Letras muito velhas viram vogais (amadurecem)
                    if (idade[i][j] > 15) {
                        proximaGeracao[i][j] = Letra.O; // A letra O simboliza completude
                    }
                    // Letras velhas podem mudar
                    else if (idade[i][j] > 10 && random.nextDouble() < 0.1) {
                        proximaGeracao[i][j] = Letra.letraAleatoria();
                    } else {
                        proximaGeracao[i][j] = atual;
                    }
                } else {
                    // Espaços vazios podem ganhar letras (inspiração)
                    if (random.nextDouble() < 0.05) {
                        proximaGeracao[i][j] = Letra.letraAleatoria();
                        idade[i][j] = 0;
                    } else {
                        proximaGeracao[i][j] = Letra.VAZIO;
                    }
                }
            }
        }

        // Regra 2: Letras se movem suavemente
        moverLetras();

        // Regra 3: Palavras formadas brilham (marcamos na idade)
        for (Palavra p : palavrasAtuais) {
            if (p.getDirecao() == 0) { // Horizontal
                for (int j = p.getColuna(); j < p.getColuna() + p.getTamanho(); j++) {
                    idade[p.getLinha()][j] = 20; // Marca como parte de palavra
                }
            } else { // Vertical
                for (int i = p.getLinha(); i < p.getLinha() + p.getTamanho(); i++) {
                    idade[i][p.getColuna()] = 20;
                }
            }
        }
    }

    // Movimento suave das letras
    private void moverLetras() {
        Letra[][] temp = new Letra[linhas][colunas];
        for (int i = 0; i < linhas; i++) {
            Arrays.fill(temp[i], Letra.VAZIO);
        }

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                if (proximaGeracao[i][j] != Letra.VAZIO) {
                    // 20% de chance de se mover
                    if (random.nextDouble() < 0.2) {
                        int direcao = random.nextInt(4);
                        int novaLinha = i;
                        int novaColuna = j;

                        switch (direcao) {
                            case 0:
                                novaLinha = Math.max(0, i - 1);
                                break; // cima
                            case 1:
                                novaLinha = Math.min(linhas - 1, i + 1);
                                break; // baixo
                            case 2:
                                novaColuna = Math.max(0, j - 1);
                                break; // esquerda
                            case 3:
                                novaColuna = Math.min(colunas - 1, j + 1);
                                break; // direita
                        }

                        if (temp[novaLinha][novaColuna] == Letra.VAZIO) {
                            temp[novaLinha][novaColuna] = proximaGeracao[i][j];
                            idade[novaLinha][novaColuna] = idade[i][j];
                        } else {
                            temp[i][j] = proximaGeracao[i][j];
                        }
                    } else {
                        temp[i][j] = proximaGeracao[i][j];
                    }
                }
            }
        }

        proximaGeracao = temp;
    }

    public void avancarGeracao() {
        calcularProximaGeracao();

        // Copia próxima geração para atual
        for (int i = 0; i < linhas; i++) {
            System.arraycopy(proximaGeracao[i], 0, celulas[i], 0, colunas);
        }
    }

    // Getters
    public Letra getCelula(int linha, int coluna) {
        return celulas[linha][coluna];
    }

    public int getIdade(int linha, int coluna) {
        return idade[linha][coluna];
    }

    public List<Palavra> getPalavrasAtuais() {
        return palavrasAtuais;
    }

    public int getLinhas() {
        return linhas;
    }

    public int getColunas() {
        return colunas;
    }

    // Clique do mouse - adiciona letra
    public void setLetra(int linha, int coluna, Letra letra) {
        if (linha >= 0 && linha < linhas && coluna >= 0 && coluna < colunas) {
            celulas[linha][coluna] = letra;
            idade[linha][coluna] = 0;
        }
    }
}
