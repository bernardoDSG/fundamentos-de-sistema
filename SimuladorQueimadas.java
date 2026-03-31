import javax.swing.*;
import java.awt.*;
import java.util.Random;

// Enum para os estados da célula
enum Celula {
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

// Classe Floresta
class Floresta {
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

// Classe Incendio
class Incendio {
    private final Floresta floresta;
    private int passos;
    private boolean executando;
    
    public Incendio(int linhas, int colunas, double densidadeArvores,
                   double probIncendio, double probCrescimento) {
        this.floresta = new Floresta(linhas, colunas, densidadeArvores, 
                                     probIncendio, probCrescimento);
        this.passos = 0;
        this.executando = true;
    }
    
    public void iniciar() {
        floresta.iniciarIncendio();
    }
    
    public void passo() {
        if (executando) {
            floresta.atualizar();
            passos++;
            
            if (!floresta.temIncendioAtivo()) {
                executando = false;
            }
        }
    }
    
    public Floresta getFloresta() {
        return floresta;
    }
    
    public int getPassos() {
        return passos;
    }
    
    public boolean isExecutando() {
        return executando;
    }
    
    public double getPercentualQueimado() {
        int totalArvoresIniciais = floresta.contarArvores() + floresta.contarQueimando();
        int arvoresRestantes = floresta.contarArvores();
        
        if (totalArvoresIniciais == 0) return 0;
        
        return (1.0 - (double) arvoresRestantes / totalArvoresIniciais) * 100;
    }
}

// Visualizador
class VisualizadorFloresta extends JPanel {
    private final Floresta floresta;
    private final int tamanhoCelula;
    
    public VisualizadorFloresta(Floresta floresta, int tamanhoCelula) {
        this.floresta = floresta;
        this.tamanhoCelula = tamanhoCelula;
        
        setPreferredSize(new Dimension(
            floresta.getColunas() * tamanhoCelula,
            floresta.getLinhas() * tamanhoCelula
        ));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        for (int i = 0; i < floresta.getLinhas(); i++) {
            for (int j = 0; j < floresta.getColunas(); j++) {
                Celula celula = floresta.getCelula(i, j);
                
                if (celula == Celula.ARVORE) {
                    g.setColor(new Color(34, 139, 34));
                } else if (celula == Celula.QUEIMANDO) {
                    g.setColor(new Color(255, 69, 0));
                } else if (celula == Celula.CINZA) {
                    g.setColor(new Color(105, 105, 105));
                } else {
                    g.setColor(new Color(222, 184, 135));
                }
                
                g.fillRect(j * tamanhoCelula, i * tamanhoCelula, 
                          tamanhoCelula, tamanhoCelula);
                g.setColor(Color.BLACK);
                g.drawRect(j * tamanhoCelula, i * tamanhoCelula, 
                          tamanhoCelula, tamanhoCelula);
            }
        }
    }
    
    public void atualizar() {
        repaint();
    }
}

// Classe Principal
public class SimuladorQueimadas extends JFrame {
    private Incendio incendio;
    private VisualizadorFloresta visualizador;
    private JButton botaoPasso;
    private JButton botaoIniciar;
    private JButton botaoReset;
    private JLabel labelPassos;
    private JLabel labelQueimado;
    private JLabel labelArvores;
    private Timer timer;
    private boolean simulando;
    
    private JSlider sliderDensidade;
    private JSlider sliderProbIncendio;
    private JSlider sliderProbCrescimento;
    private JSlider sliderVelocidade;
    private JLabel labelDensidade;
    private JLabel labelProbIncendio;
    private JLabel labelProbCrescimento;
    private JLabel labelVelocidade;
    
    private int linhas = 50;
    private int colunas = 50;
    
    public SimuladorQueimadas() {
        setTitle("Simulador de Queimadas Florestais");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        criarSimulacao();
        
        JPanel painelParametros = criarPainelParametros();
        add(painelParametros, BorderLayout.NORTH);
        
        JPanel painelBotoes = criarPainelBotoes();
        add(painelBotoes, BorderLayout.SOUTH);
        
        timer = new Timer(100, e -> {
            if (simulando && incendio.isExecutando()) {
                passoSimulacao();
            } else if (simulando && !incendio.isExecutando()) {
                simulando = false;
                botaoIniciar.setText("Iniciar");
                JOptionPane.showMessageDialog(this, 
                    "Simulação concluída! O fogo se extinguiu.",
                    "Fim da Simulação",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        simulando = false;
        
        setSize(800, 800);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void criarSimulacao() {
        double densidade = sliderDensidade != null ? sliderDensidade.getValue() / 100.0 : 0.7;
        double probIncendio = sliderProbIncendio != null ? sliderProbIncendio.getValue() / 10000.0 : 0.0005;
        double probCrescimento = sliderProbCrescimento != null ? sliderProbCrescimento.getValue() / 1000.0 : 0.01;
        
        incendio = new Incendio(linhas, colunas, densidade, probIncendio, probCrescimento);
        incendio.iniciar();
        
        if (visualizador != null) {
            remove(visualizador);
        }
        
        visualizador = new VisualizadorFloresta(incendio.getFloresta(), 12);
        add(visualizador, BorderLayout.CENTER);
        
        revalidate();
        repaint();
        
        if (labelPassos != null) {
            atualizarLabels();
        }
    }
    
    private JPanel criarPainelParametros() {
        JPanel painel = new JPanel();
        painel.setLayout(new GridLayout(4, 2, 10, 5));
        painel.setBorder(BorderFactory.createTitledBorder("Parâmetros"));
        
        labelDensidade = new JLabel("Densidade: 70%");
        sliderDensidade = new JSlider(0, 100, 70);
        sliderDensidade.addChangeListener(e -> {
            labelDensidade.setText("Densidade: " + sliderDensidade.getValue() + "%");
        });
        
        labelProbIncendio = new JLabel("Raio: 0.05%");
        sliderProbIncendio = new JSlider(0, 100, 5);
        sliderProbIncendio.addChangeListener(e -> {
            labelProbIncendio.setText("Raio: " + sliderProbIncendio.getValue() / 100.0 + "%");
        });
        
        labelProbCrescimento = new JLabel("Crescimento: 1.0%");
        sliderProbCrescimento = new JSlider(0, 100, 10);
        sliderProbCrescimento.addChangeListener(e -> {
            labelProbCrescimento.setText("Crescimento: " + sliderProbCrescimento.getValue() / 10.0 + "%");
        });
        
        labelVelocidade = new JLabel("Velocidade: 100ms");
        sliderVelocidade = new JSlider(50, 500, 100);
        sliderVelocidade.addChangeListener(e -> {
            labelVelocidade.setText("Velocidade: " + sliderVelocidade.getValue() + "ms");
            if (timer != null) timer.setDelay(sliderVelocidade.getValue());
        });
        
        painel.add(labelDensidade);
        painel.add(sliderDensidade);
        painel.add(labelProbIncendio);
        painel.add(sliderProbIncendio);
        painel.add(labelProbCrescimento);
        painel.add(sliderProbCrescimento);
        painel.add(labelVelocidade);
        painel.add(sliderVelocidade);
        
        return painel;
    }
    
    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel();
        
        botaoPasso = new JButton("Passo");
        botaoIniciar = new JButton("Iniciar");
        botaoReset = new JButton("Reset");
        JButton botaoAplicar = new JButton("Aplicar");
        
        labelPassos = new JLabel("Passos: 0");
        labelQueimado = new JLabel("Queimado: 0%");
        labelArvores = new JLabel("Árvores: 0");
        
        botaoPasso.addActionListener(e -> passoSimulacao());
        botaoIniciar.addActionListener(e -> alternarSimulacao());
        botaoReset.addActionListener(e -> resetSimulacao());
        botaoAplicar.addActionListener(e -> aplicarParametros());
        
        painel.add(botaoPasso);
        painel.add(botaoIniciar);
        painel.add(botaoReset);
        painel.add(botaoAplicar);
        painel.add(labelPassos);
        painel.add(labelQueimado);
        painel.add(labelArvores);
        
        return painel;
    }
    
    private void passoSimulacao() {
        if (incendio.isExecutando()) {
            incendio.passo();
            visualizador.atualizar();
            atualizarLabels();
        }
    }
    
    private void alternarSimulacao() {
        simulando = !simulando;
        if (simulando) {
            botaoIniciar.setText("Pausar");
            timer.start();
        } else {
            botaoIniciar.setText("Iniciar");
            timer.stop();
        }
    }
    
    private void resetSimulacao() {
        simulando = false;
        if (timer.isRunning()) timer.stop();
        botaoIniciar.setText("Iniciar");
        criarSimulacao();
    }
    
    private void aplicarParametros() {
        if (simulando) alternarSimulacao();
        criarSimulacao();
    }
    
    private void atualizarLabels() {
        labelPassos.setText("Passos: " + incendio.getPassos());
        labelQueimado.setText(String.format("Queimado: %.1f%%", incendio.getPercentualQueimado()));
        labelArvores.setText("Árvores: " + incendio.getFloresta().contarArvores());
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new SimuladorQueimadas();
        });
    }
}