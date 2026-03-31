import java.awt.Color;

public enum Letra {
    // Espaço vazio
    VAZIO(' ', Color.WHITE, false),

    // Consoantes (cores frias)
    B('B', new Color(70, 130, 180), true), // Azul aço
    C('C', new Color(100, 149, 237), true), // Azul centáurea
    D('D', new Color(72, 61, 139), true), // Azul escuro
    F('F', new Color(123, 104, 238), true), // Azul médio
    G('G', new Color(106, 90, 205), true), // Azul ardósia
    H('H', new Color(135, 206, 235), true), // Azul céu
    J('J', new Color(176, 224, 230), true), // Azul pó
    K('K', new Color(95, 158, 160), true), // Verde azulado
    L('L', new Color(72, 209, 204), true), // Turquesa médio
    M('M', new Color(0, 139, 139), true), // Ciano escuro
    N('N', new Color(0, 255, 255), true), // Ciano
    P('P', new Color(64, 224, 208), true), // Turquesa
    Q('Q', new Color(32, 178, 170), true), // Verde mar
    R('R', new Color(70, 130, 180), true), // Azul aço
    S('S', new Color(100, 149, 237), true), // Azul centáurea
    T('T', new Color(72, 61, 139), true), // Azul escuro
    V('V', new Color(123, 104, 238), true), // Azul médio
    W('W', new Color(106, 90, 205), true), // Azul ardósia
    X('X', new Color(135, 206, 235), true), // Azul céu
    Y('Y', new Color(176, 224, 230), true), // Azul pó
    Z('Z', new Color(95, 158, 160), true), // Verde azulado

    // Vogais (cores quentes)
    A('A', new Color(255, 99, 71), true), // Tomate
    E('E', new Color(255, 127, 80), true), // Coral
    I('I', new Color(255, 160, 122), true), // Salmão claro
    O('O', new Color(255, 69, 0), true), // Vermelho laranja
    U('U', new Color(255, 140, 0), true); // Laranja escuro

    private final char caractere;
    private final Color cor;
    private final boolean isLetra;

    Letra(char caractere, Color cor, boolean isLetra) {
        this.caractere = caractere;
        this.cor = cor;
        this.isLetra = isLetra;
    }

    public char getCaractere() {
        return caractere;
    }

    public Color getCor() {
        return cor;
    }

    public boolean isLetra() {
        return isLetra;
    }

    // Retorna letra aleatória (exceto vazio)
    public static Letra letraAleatoria() {
        Letra[] letras = values();
        int index;
        do {
            index = (int) (Math.random() * letras.length);
        } while (letras[index] == VAZIO);
        return letras[index];
    }
}
