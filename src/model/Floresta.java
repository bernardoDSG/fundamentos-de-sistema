package src.model;

import java.util.Random;

public class Floresta {
    private Celula[][] grade;
    private final int linhas;
    private final int colunas;
    private double probabilidadeIncendio;
    private double probabilidadeCrescimento;
    private final Random random;
    
    public Floresta(int linhas, int colunas, double densidadeArvores, 
                   double probabilidadeIncendio, double probabilidadeCrescimento) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.probabilidadeIncendio = probabilidadeIncendio;
        this.probabilidadeCrescimento = probabilidadeCrescimento;
        this.random = new Random();
        
        this.grade = new Celula[linhas][colunas];
        inicializarFloresta(densidadeArvores);
    }
    
    private void inicializarFloresta(double densidadeArvores) {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                if (random.nextDouble() < densidadeArvores) {
                    grade[i][j] = Celula.ARVORE;
                } else {
                    grade[i][j] = Celula.VAZIO;
                }
            }
        }
    }
    
    public void iniciarIncendio() {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                if (grade[i][j] == Celula.ARVORE && random.nextDouble() < 0.01) {
                    grade[i][j] = Celula.QUEIMANDO;
                }
            }
        }
    }
    
    public void atualizar() {
        Celula[][] novaGrade = new Celula[linhas][colunas];
        
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                novaGrade[i][j] = aplicarRegras(i, j);
            }
        }
        
        grade = novaGrade;
    }
    
    private Celula aplicarRegras(int linha, int coluna) {
        Celula estadoAtual = grade[linha][coluna];
        
        if (estadoAtual == Celula.QUEIMANDO) {
            return Celula.CINZA;
        }
        
        if (estadoAtual == Celula.CINZA) {
            return Celula.CINZA;
        }
        
        if (estadoAtual == Celula.ARVORE) {
            if (temVizinhoQueimando(linha, coluna)) {
                return Celula.QUEIMANDO;
            }
            if (random.nextDouble() < probabilidadeIncendio) {
                return Celula.QUEIMANDO;
            }
            return Celula.ARVORE;
        }
        
        if (estadoAtual == Celula.VAZIO) {
            if (random.nextDouble() < probabilidadeCrescimento) {
                return Celula.ARVORE;
            }
            return Celula.VAZIO;
        }
        
        return estadoAtual;
    }
    
    private boolean temVizinhoQueimando(int linha, int coluna) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                
                int novaLinha = linha + i;
                int novaColuna = coluna + j;
                
                if (novaLinha >= 0 && novaLinha < linhas && 
                    novaColuna >= 0 && novaColuna < colunas) {
                    if (grade[novaLinha][novaColuna] == Celula.QUEIMANDO) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public Celula getCelula(int linha, int coluna) {
        return grade[linha][coluna];
    }
    
    public int getLinhas() {
        return linhas;
    }
    
    public int getColunas() {
        return colunas;
    }
    
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
    
    public boolean temIncendioAtivo() {
        return contarQueimando() > 0;
    }
    
    public void setProbabilidadeIncendio(double probabilidade) {
        this.probabilidadeIncendio = Math.min(1.0, Math.max(0.0, probabilidade));
    }
    
    public void setProbabilidadeCrescimento(double probabilidade) {
        this.probabilidadeCrescimento = Math.min(1.0, Math.max(0.0, probabilidade));
    }
}