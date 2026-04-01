/**
 * Pacote model - Contém as classes que representam a lógica da simulação
 */
package src.model;

/**
 * Enum que representa os possíveis estados de uma célula na floresta.
 * Cada célula pode estar em um destes 4 estados:
 * - VAZIO: Solo sem vegetação
 * - ARVORE: Vegetação saudável
 * - QUEIMANDO: Árvore em chamas (propaga o fogo)
 * - CINZA: Área já queimada (não propaga fogo)
 */
public enum Celula {
    
    // Constantes do enum com seus respectivos emojis para visualização
    VAZIO("⬜"),      // Solo vazio - representado por quadrado branco
    ARVORE("🌳"),     // Árvore saudável - representada por emoji de árvore
    QUEIMANDO("🔥"),  // Árvore queimando - representada por emoji de fogo
    CINZA("⬛");      // Área queimada - representada por quadrado preto
    
    // Atributo que armazena o símbolo visual da célula
    private final String simbolo;
    
    /**
     * Construtor do enum
     * @param simbolo Representação visual da célula (emoji ou caractere)
     */
    Celula(String simbolo) {
        this.simbolo = simbolo;
    }
    
    /**
     * Retorna o símbolo visual da célula
     * @return String com o emoji/caractere representativo
     */
    public String getSimbolo() {
        return simbolo;
    }
    
    /**
     * Verifica se a célula é uma árvore
     * @return true se for árvore, false caso contrário
     */
    public boolean isArvore() {
        return this == ARVORE;
    }
    
    /**
     * Verifica se a célula está queimando
     * @return true se estiver queimando, false caso contrário
     */
    public boolean isQueimando() {
        return this == QUEIMANDO;
    }
    
    /**
     * Verifica se a célula é cinza (já queimada)
     * @return true se for cinza, false caso contrário
     */
    public boolean isCinza() {
        return this == CINZA;
    }
    
    /**
     * Verifica se a célula está vazia
     * @return true se estiver vazia, false caso contrário
     */
    public boolean isVazio() {
        return this == VAZIO;
    }
    
    /**
     * Retorna uma representação textual da célula
     * @return O símbolo visual da célula
     */
    @Override
    public String toString() {
        return simbolo;
    }
}