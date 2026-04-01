/**
 * Pacote model - Contém as classes que representam a lógica da simulação
 */
package src.model;

import java.util.Random;

/**
 * Classe que representa a floresta como um autômato celular.
 * Gerencia a grade de células e aplica as regras de evolução.
 */
public class Floresta {
    
    // Matriz bidimensional que armazena o estado de cada célula
    private Celula[][] grade;
    
    // Dimensões da floresta
    private final int linhas;
    private final int colunas;
    
    // Probabilidades que controlam a simulação
    private double probabilidadeIncendio;    // Chance de um raio iniciar fogo
    private double probabilidadeCrescimento; // Chance de uma nova árvore crescer
    
    // Gerador de números aleatórios para as probabilidades
    private final Random random;
    
    /**
     * Construtor da floresta
     * @param linhas Número de linhas da grade
     * @param colunas Número de colunas da grade
     * @param densidadeArvores Porcentagem inicial de árvores (0.0 a 1.0)
     * @param probabilidadeIncendio Chance de ignição por raio (0.0 a 1.0)
     * @param probabilidadeCrescimento Chance de crescimento de novas árvores (0.0 a 1.0)
     */
    public Floresta(int linhas, int colunas, double densidadeArvores, 
                   double probabilidadeIncendio, double probabilidadeCrescimento) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.probabilidadeIncendio = probabilidadeIncendio;
        this.probabilidadeCrescimento = probabilidadeCrescimento;
        this.random = new Random();
        
        // Inicializa a matriz com as dimensões especificadas
        this.grade = new Celula[linhas][colunas];
        
        // Preenche a floresta com árvores e espaços vazios
        inicializarFloresta(densidadeArvores);
    }
    
    /**
     * Inicializa a floresta com árvores distribuídas aleatoriamente
     * @param densidadeArvores Porcentagem de células que serão árvores
     */
    private void inicializarFloresta(double densidadeArvores) {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                // Decide aleatoriamente se a célula recebe uma árvore
                if (random.nextDouble() < densidadeArvores) {
                    grade[i][j] = Celula.ARVORE;
                } else {
                    grade[i][j] = Celula.VAZIO;
                }
            }
        }
    }
    
    /**
     * Inicia incêndios aleatórios em aproximadamente 1% das árvores
     * Simula focos naturais de incêndio
     */
    public void iniciarIncendio() {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                // 1% de chance de uma árvore pegar fogo inicialmente
                if (grade[i][j] == Celula.ARVORE && random.nextDouble() < 0.01) {
                    grade[i][j] = Celula.QUEIMANDO;
                }
            }
        }
    }
    
    /**
     * Inicia incêndio em um ponto específico (para interação do usuário)
     * @param linha Coordenada linha do clique
     * @param coluna Coordenada coluna do clique
     */
    public void iniciarIncendioEmPonto(int linha, int coluna) {
        if (linha >= 0 && linha < linhas && coluna >= 0 && coluna < colunas) {
            if (grade[linha][coluna] == Celula.ARVORE) {
                grade[linha][coluna] = Celula.QUEIMANDO;
            }
        }
    }
    
    /**
     * Atualiza toda a floresta para o próximo passo da simulação
     * Aplica as regras do autômato celular para cada célula
     */
    public void atualizar() {
        // Cria uma nova grade para armazenar o próximo estado
        Celula[][] novaGrade = new Celula[linhas][colunas];
        
        // Aplica as regras para cada célula
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                novaGrade[i][j] = aplicarRegras(i, j);
            }
        }
        
        // Substitui a grade antiga pela nova
        grade = novaGrade;
    }
    
    /**
     * Aplica as regras do autômato celular para uma célula específica
     * Regras:
     * 1. QUEIMANDO → CINZA (árvore queimada vira cinza)
     * 2. CINZA → CINZA (permanece cinza)
     * 3. ARVORE → QUEIMANDO se tiver vizinho queimando OU por raio
     * 4. ARVORE → ARVORE (permanece árvore)
     * 5. VAZIO → ARVORE com probabilidade de crescimento
     * 6. VAZIO → VAZIO (permanece vazio)
     * 
     * @param linha Coordenada linha da célula
     * @param coluna Coordenada coluna da célula
     * @return Novo estado da célula após aplicar as regras
     */
    private Celula aplicarRegras(int linha, int coluna) {
        Celula estadoAtual = grade[linha][coluna];
        
        // REGRA 1: Árvore queimando vira cinza
        if (estadoAtual == Celula.QUEIMANDO) {
            return Celula.CINZA;
        }
        
        // REGRA 2: Cinza permanece cinza (não regenera no curto prazo)
        if (estadoAtual == Celula.CINZA) {
            return Celula.CINZA;
        }
        
        // REGRA 3 e 4: Árvore pode pegar fogo
        if (estadoAtual == Celula.ARVORE) {
            // Verifica se algum vizinho está queimando (propagação do fogo)
            if (temVizinhoQueimando(linha, coluna)) {
                return Celula.QUEIMANDO;
            }
            // Verifica se um raio causou ignição espontânea
            if (random.nextDouble() < probabilidadeIncendio) {
                return Celula.QUEIMANDO;
            }
            // Se não pegou fogo, continua como árvore
            return Celula.ARVORE;
        }
        
        // REGRA 5 e 6: Espaço vazio pode gerar nova árvore (crescimento)
        if (estadoAtual == Celula.VAZIO) {
            if (random.nextDouble() < probabilidadeCrescimento) {
                return Celula.ARVORE;  // Nova árvore cresce
            }
            return Celula.VAZIO;  // Permanece vazio
        }
        
        // Caso padrão (não deve acontecer)
        return estadoAtual;
    }
    
    /**
     * Verifica se alguma célula vizinha está queimando
     * Considera as 8 direções (inclui diagonais)
     * 
     * @param linha Coordenada linha da célula central
     * @param coluna Coordenada coluna da célula central
     * @return true se pelo menos um vizinho estiver queimando
     */
    private boolean temVizinhoQueimando(int linha, int coluna) {
        // Percorre a vizinhança 3x3 ao redor da célula
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                // Ignora a própria célula
                if (i == 0 && j == 0) continue;
                
                int novaLinha = linha + i;
                int novaColuna = coluna + j;
                
                // Verifica se as coordenadas estão dentro dos limites da grade
                if (novaLinha >= 0 && novaLinha < linhas && 
                    novaColuna >= 0 && novaColuna < colunas) {
                    if (grade[novaLinha][novaColuna] == Celula.QUEIMANDO) {
                        return true;  // Encontrou um vizinho queimando
                    }
                }
            }
        }
        return false;  // Nenhum vizinho queimando
    }
    
    // ========== MÉTODOS GETTERS ==========
    
    public Celula getCelula(int linha, int coluna) {
        return grade[linha][coluna];
    }
    
    public int getLinhas() {
        return linhas;
    }
    
    public int getColunas() {
        return colunas;
    }
    
    /**
     * Conta quantas árvores ainda estão vivas na floresta
     * @return Número de células com árvores
     */
    public int contarArvores() {
        int count = 0;
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                if (grade[i][j] == Celula.ARVORE) {
                    count++;
                }
            }
        }
        return count;
    }
    
    /**
     * Conta quantas células estão queimando no momento
     * @return Número de células em chamas
     */
    public int contarQueimando() {
        int count = 0;
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                if (grade[i][j] == Celula.QUEIMANDO) {
                    count++;
                }
            }
        }
        return count;
    }
    
    /**
     * Conta quantas áreas já foram queimadas (cinzas)
     * @return Número de células queimadas
     */
    public int contarCinza() {
        int count = 0;
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                if (grade[i][j] == Celula.CINZA) {
                    count++;
                }
            }
        }
        return count;
    }
    
    /**
     * Verifica se ainda há fogo ativo na floresta
     * @return true se existir pelo menos uma célula queimando
     */
    public boolean temIncendioAtivo() {
        return contarQueimando() > 0;
    }
    
    /**
     * Atualiza a probabilidade de ignição por raio
     * @param probabilidade Novo valor (entre 0 e 1)
     */
    public void setProbabilidadeIncendio(double probabilidade) {
        this.probabilidadeIncendio = Math.min(1.0, Math.max(0.0, probabilidade));
    }
    
    /**
     * Atualiza a probabilidade de crescimento de novas árvores
     * @param probabilidade Novo valor (entre 0 e 1)
     */
    public void setProbabilidadeCrescimento(double probabilidade) {
        this.probabilidadeCrescimento = Math.min(1.0, Math.max(0.0, probabilidade));
    }
}