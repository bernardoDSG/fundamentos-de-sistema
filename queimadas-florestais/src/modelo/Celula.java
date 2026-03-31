package modelo;

public enum Celula {
    VAZIO(' ', "⬜"),
    ARVORE('T', "🌳"),
    QUEIMANDO('F', "🔥"),
    CINZA('C', "⬛");
    
    private final char codigo;
    private final String simbolo;
    
    Celula(char codigo, String simbolo) {
        this.codigo = codigo;
        this.simbolo = simbolo;
    }
    
    public char getCodigo() {
        return codigo;
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