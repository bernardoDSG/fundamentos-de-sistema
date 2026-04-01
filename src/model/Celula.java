package src.model;

// Enum para os estados da célula
public enum Celula {
    VAZIO("⬜"),
    ARVORE("🌳"),
    QUEIMANDO("🔥"),
    CINZA("⬛");
    
    private final String simbolo;
    
    Celula(String simbolo) {
        this.simbolo = simbolo;
    }
    
    public String getSimbolo() {
        return simbolo;
    }
    
    public boolean isArvore() {
        return this == ARVORE;
    }
    
    public boolean isQueimando() {
        return this == QUEIMANDO;
    }
    
    public boolean isCinza() {
        return this == CINZA;
    }
    
    public boolean isVazio() {
        return this == VAZIO;
    }
}
