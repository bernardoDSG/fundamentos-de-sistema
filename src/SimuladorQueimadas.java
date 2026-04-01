package src;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import src.model.Incendio;
import src.view.VisualizadorFloresta;

public class SimuladorQueimadas extends JFrame{
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
