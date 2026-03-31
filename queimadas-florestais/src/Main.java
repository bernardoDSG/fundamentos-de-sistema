import modelo.Incendio;
import visualizacao.VisualizadorFloresta;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Main extends JFrame {
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
    
    // Componentes para parâmetros
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
    
    public Main() {
        setTitle("Simulador de Queimadas Florestais - Autômato Celular");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Cria a simulação com parâmetros iniciais
        criarSimulacao();
        
        // Painel de controle principal
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        
        // Painel de parâmetros
        JPanel painelParametros = criarPainelParametros();
        painelPrincipal.add(painelParametros, BorderLayout.NORTH);
        
        // Painel de botões
        JPanel painelBotoes = criarPainelBotoes();
        painelPrincipal.add(painelBotoes, BorderLayout.CENTER);
        
        add(painelPrincipal, BorderLayout.SOUTH);
        
        // Timer para animação
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
        painel.setBorder(BorderFactory.createTitledBorder("Parâmetros da Simulação"));
        
        // Densidade de árvores
        labelDensidade = new JLabel("Densidade de Árvores: 70%");
        sliderDensidade = new JSlider(0, 100, 70);
        sliderDensidade.addChangeListener(e -> {
            int valor = sliderDensidade.getValue();
            labelDensidade.setText(String.format("Densidade de Árvores: %d%%", valor));
        });
        
        // Probabilidade de incêndio (raio) - escala de 0 a 0.01%
        labelProbIncendio = new JLabel("Probabilidade de Raio: 0.05%");
        sliderProbIncendio = new JSlider(0, 100, 5); // 5 = 0.05%
        sliderProbIncendio.addChangeListener(e -> {
            double valor = sliderProbIncendio.getValue() / 100.0;
            labelProbIncendio.setText(String.format("Probabilidade de Raio: %.2f%%", valor));
        });
        
        // Probabilidade de crescimento
        labelProbCrescimento = new JLabel("Taxa de Crescimento: 1.0%");
        sliderProbCrescimento = new JSlider(0, 100, 10); // 10 = 1.0%
        sliderProbCrescimento.addChangeListener(e -> {
            double valor = sliderProbCrescimento.getValue() / 10.0;
            labelProbCrescimento.setText(String.format("Taxa de Crescimento: %.1f%%", valor));
        });
        
        // Velocidade da simulação
        labelVelocidade = new JLabel("Velocidade: 100 ms");
        sliderVelocidade = new JSlider(50, 500, 100);
        sliderVelocidade.addChangeListener(e -> {
            int valor = sliderVelocidade.getValue();
            labelVelocidade.setText(String.format("Velocidade: %d ms", valor));
            if (timer != null) {
                timer.setDelay(valor);
            }
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
        painel.setLayout(new FlowLayout());
        
        botaoPasso = new JButton("▶ Passo");
        botaoIniciar = new JButton("▶ Iniciar");
        botaoReset = new JButton("🔄 Reset");
        JButton botaoAplicar = new JButton("✅ Aplicar Parâmetros");
        
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
        } else {
            JOptionPane.showMessageDialog(this, 
                "A simulação terminou! Clique em Reset para reiniciar.",
                "Simulação Concluída",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void alternarSimulacao() {
        if (!incendio.isExecutando() && !simulando) {
            JOptionPane.showMessageDialog(this, 
                "A simulação terminou! Clique em Reset para reiniciar.",
                "Simulação Concluída",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        simulando = !simulando;
        if (simulando) {
            botaoIniciar.setText("⏸ Pausar");
            timer.start();
        } else {
            botaoIniciar.setText("▶ Iniciar");
            timer.stop();
        }
    }
    
    private void resetSimulacao() {
        simulando = false;
        if (timer.isRunning()) {
            timer.stop();
        }
        botaoIniciar.setText("▶ Iniciar");
        
        criarSimulacao();
    }
    
    private void aplicarParametros() {
        // Pausa a simulação se estiver rodando
        if (simulando) {
            alternarSimulacao();
        }
        
        // Aplica novos parâmetros
        double densidade = sliderDensidade.getValue() / 100.0;
        double probIncendio = sliderProbIncendio.getValue() / 10000.0;
        double probCrescimento = sliderProbCrescimento.getValue() / 1000.0;
        
        incendio.getFloresta().setProbabilidadeIncendio(probIncendio);
        incendio.getFloresta().setProbabilidadeCrescimento(probCrescimento);
        
        // Reinicia a simulação com nova densidade
        criarSimulacao();
        
        JOptionPane.showMessageDialog(this, 
            "Parâmetros aplicados!\n" +
            "Densidade: " + (densidade * 100) + "%\n" +
            "Probabilidade de Raio: " + (probIncendio * 100) + "%\n" +
            "Taxa de Crescimento: " + (probCrescimento * 100) + "%",
            "Parâmetros Atualizados",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void atualizarLabels() {
        labelPassos.setText("Passos: " + incendio.getPassos());
        labelQueimado.setText(String.format("Queimado: %.1f%%", 
                              incendio.getPercentualQueimado()));
        labelArvores.setText("Árvores: " + incendio.getFloresta().contarArvores());
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Main();
        });
    }
}