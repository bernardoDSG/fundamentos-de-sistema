import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class AutomatoPoesia extends JPanel implements ActionListener {
    private GradePoesia grade;
    private int tamanhoCelula = 30; // Maior para letras serem legíveis
    private Timer timer;
    private boolean executando = false;
    private int geracao = 0;
    private Font fonteLetra = new Font("Monospaced", Font.BOLD, 18);
    private Font fonteInfo = new Font("Arial", Font.BOLD, 12);

   

    public AutomatoPoesia(int linhas, int colunas) {
        this.grade = new GradePoesia(linhas, colunas);
        setPreferredSize(new Dimension(colunas * tamanhoCelula, linhas * tamanhoCelula));
        setBackground(Color.BLACK);

        // Timer mais lento para dar tempo de ler
        timer = new Timer(500, this);

        // Listener do mouse para desenhar letras
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!executando) {
                    int linha = e.getY() / tamanhoCelula;
                    int coluna = e.getX() / tamanhoCelula;

                    if (linha >= 0 && linha < grade.getLinhas() &&
                            coluna >= 0 && coluna < grade.getColunas()) {

                        // Mostra menu para escolher letra
                        String letraStr = JOptionPane.showInputDialog(
                                AutomatoPoesia.this,
                                "Digite uma letra (A-Z):",
                                "Inserir Letra",
                                JOptionPane.PLAIN_MESSAGE);

                        if (letraStr != null && letraStr.length() > 0) {
                            char c = Character.toUpperCase(letraStr.charAt(0));
                            Letra letra = encontrarLetra(c);
                            if (letra != null) {
                                grade.setLetra(linha, coluna, letra);
                                repaint();
                            }
                        }
                    }
                }
            }
        });

        // Inicia com poema exemplo
        grade.criarPoemaExemplo();
    }

    private Letra encontrarLetra(char c) {
        for (Letra letra : Letra.values()) {
            if (letra.getCaractere() == c) {
                return letra;
            }
        }
        return Letra.A; // Padrão
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Fundo preto para melhor contraste
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Desenha grade suave
        g2.setColor(new Color(50, 50, 50));
        for (int i = 0; i <= grade.getLinhas(); i++) {
            g2.drawLine(0, i * tamanhoCelula, getWidth(), i * tamanhoCelula);
        }
        for (int j = 0; j <= grade.getColunas(); j++) {
            g2.drawLine(j * tamanhoCelula, 0, j * tamanhoCelula, getHeight());
        }

        // Desenha letras
        g2.setFont(fonteLetra);
        FontMetrics fm = g2.getFontMetrics();

        for (int i = 0; i < grade.getLinhas(); i++) {
            for (int j = 0; j < grade.getColunas(); j++) {
                Letra letra = grade.getCelula(i, j);
                if (letra != Letra.VAZIO) {
                    int x = j * tamanhoCelula + (tamanhoCelula - fm.charWidth(letra.getCaractere())) / 2;
                    int y = i * tamanhoCelula + (tamanhoCelula + fm.getAscent()) / 2;

                    // Escolhe cor baseada na idade e se faz parte de palavra
                    Color cor;
                    int idade = grade.getIdade(i, j);

                    if (fazParteDePalavra(i, j)) {
                        cor = new Color(255, 215, 0); // Dourado brilhante
                    } else if (idade > 15) {
                        cor = new Color(255, 182, 193); // Rosa claro
                    } else if (idade < 5) {
                        cor = new Color(144, 238, 144); // Verde claro
                    } else {
                        // Usa a cor da letra mas mais brilhante
                        cor = letra.getCor().brighter();
                    }

                    g2.setColor(cor);
                    g2.drawString(String.valueOf(letra.getCaractere()), x, y);

                    // Efeito de brilho para palavras
                    if (fazParteDePalavra(i, j)) {
                        g2.setColor(new Color(255, 215, 0, 30));
                        g2.fillRect(j * tamanhoCelula + 2, i * tamanhoCelula + 2,
                                tamanhoCelula - 4, tamanhoCelula - 4);
                    }
                }
            }
        }

        // Mostra informações com fundo semi-transparente para melhor leitura
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(5, 5, 150, 25);
        g2.setColor(Color.WHITE);
        g2.setFont(fonteInfo);
        g2.drawString("Geração: " + geracao, 10, 20);

        // Mostra palavras encontradas
        List<Palavra> palavras = grade.getPalavrasAtuais();
        int y = 40;

        // Fundo semi-transparente para lista de palavras
        if (!palavras.isEmpty()) {
            g2.setColor(new Color(0, 0, 0, 180));
            g2.fillRect(5, 35, 120, palavras.size() * 15 + 5);
        }

        g2.setColor(new Color(255, 215, 0)); // Dourado para palavras
        for (Palavra p : palavras) {
            g2.drawString("✓ " + p.getTexto(), 10, y);
            y += 15;
        }
    }

    private boolean fazParteDePalavra(int linha, int coluna) {
        List<Palavra> palavras = grade.getPalavrasAtuais();
        for (Palavra p : palavras) {
            if (p.getDirecao() == 0) { // Horizontal
                if (p.getLinha() == linha &&
                        coluna >= p.getColuna() &&
                        coluna < p.getColuna() + p.getTamanho()) {
                    return true;
                }
            } else { // Vertical
                if (p.getColuna() == coluna &&
                        linha >= p.getLinha() &&
                        linha < p.getLinha() + p.getTamanho()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        grade.avancarGeracao();
        geracao++;
        repaint();
    }

    public void iniciar() {
        executando = true;
        timer.start();
    }

    public void parar() {
        executando = false;
        timer.stop();
    }

    public void avancarUmaGeracao() {
        if (!executando) {
            grade.avancarGeracao();
            geracao++;
            repaint();
        }
    }

    public void limpar() {
        parar();
        grade = new GradePoesia(grade.getLinhas(), grade.getColunas());
        geracao = 0;
        repaint();
    }

    public void aleatorio() {
        parar();
        grade.gerarPoesiaAleatoria();
        geracao = 0;
        repaint();
    }

    public void poemaExemplo() {
        parar();
        grade.criarPoemaExemplo();
        geracao = 0;
        repaint();
    }
}
