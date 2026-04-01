/**
 * Pacote main - Contém a classe principal com a interface gráfica
 */
package src.main;


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

/**
 * Classe principal do simulador de queimadas florestais
 * Implementa a interface gráfica e controla a simulação
 */
public class SimuladorQueimadas extends JFrame {
    
    // Componentes principais da simulação
    private Incendio incendio;              // Controla a lógica da simulação
    private VisualizadorFloresta visualizador;  // Desenha a floresta
    
    // Botões de controle
    private JButton botaoPasso;             // Avança um passo
    private JButton botaoIniciar;           // Inicia/Pausa a simulação
    private JButton botaoReset;             // Reinicia a simulação
    
    // Labels de informação
    private JLabel labelPassos;             // Mostra número de passos
    private JLabel labelQueimado;           // Mostra percentual queimado
    private JLabel labelArvores;            // Mostra árvores restantes
    private JLabel labelQueimadas;          // Mostra árvores queimadas
    
    // Controles deslizantes (sliders) para ajustar parâmetros
    private JSlider sliderDensidade;        // Densidade de árvores
    private JSlider sliderProbIncendio;     // Probabilidade de raio
    private JSlider sliderProbCrescimento;  // Taxa de crescimento
    private JSlider sliderVelocidade;       // Velocidade da simulação
    
    // Labels dos sliders
    private JLabel labelDensidade;
    private JLabel labelProbIncendio;
    private JLabel labelProbCrescimento;
    private JLabel labelVelocidade;
    
    // Timer para animação automática
    private Timer timer;
    
    // Flag para controlar se a simulação está rodando automaticamente
    private boolean simulando;
    
    // Dimensões da floresta
    private int linhas = 50;
    private int colunas = 50;
    
    /**
     * Construtor da janela principal
     * Configura a interface gráfica e inicializa a simulação
     */
    public SimuladorQueimadas() {
        // Configurações básicas da janela
        setTitle("Simulador de Queimadas Florestais");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Inicializa a simulação com parâmetros padrão
        criarSimulacao();
        
        // Cria e adiciona os painéis de controle
        JPanel painelParametros = criarPainelParametros();
        add(painelParametros, BorderLayout.NORTH);
        
        JPanel painelBotoes = criarPainelBotoes();
        add(painelBotoes, BorderLayout.SOUTH);
        
        // Timer que executa a simulação automaticamente
        timer = new Timer(300, e -> {
            if (simulando && incendio.isExecutando()) {
                passoSimulacao();
            } else if (simulando && !incendio.isExecutando()) {
                // Simulação terminou
                simulando = false;
                botaoIniciar.setText("▶ Iniciar");
                mostrarResultadoFinal();
            }
        });
        
        simulando = false;
        
        // Configura tamanho e posição da janela
        setSize(800, 800);
        setLocationRelativeTo(null);  // Centraliza na tela
        setVisible(true);             // Mostra a janela
    }
    
    /**
     * Cria uma nova simulação com os parâmetros atuais
     */
    private void criarSimulacao() {
        // Obtém os valores dos sliders (ou usa valores padrão)
        double densidade = sliderDensidade != null ? sliderDensidade.getValue() / 100.0 : 0.7;
        double probIncendio = sliderProbIncendio != null ? sliderProbIncendio.getValue() / 10000.0 : 0.0005;
        double probCrescimento = sliderProbCrescimento != null ? sliderProbCrescimento.getValue() / 1000.0 : 0.01;
        
        // Cria uma nova simulação
        incendio = new Incendio(linhas, colunas, densidade, probIncendio, probCrescimento);
        incendio.iniciar();  // Inicia alguns focos aleatórios
        
        // Remove o visualizador antigo se existir
        if (visualizador != null) {
            remove(visualizador);
        }
        
        // Cria e adiciona o novo visualizador
        visualizador = new VisualizadorFloresta(incendio.getFloresta(), 12);
        add(visualizador, BorderLayout.CENTER);
        
        // Atualiza a interface
        revalidate();
        repaint();
        
        // Atualiza os labels
        if (labelPassos != null) {
            atualizarLabels();
        }
        
        // Aplica a velocidade atual
        if (timer != null && sliderVelocidade != null) {
            timer.setDelay(sliderVelocidade.getValue());
        }
    }
    
    /**
     * Cria o painel com os sliders de parâmetros
     * @return JPanel configurado com os controles
     */
    private JPanel criarPainelParametros() {
        JPanel painel = new JPanel();
        painel.setLayout(new GridLayout(4, 2, 10, 5));
        painel.setBorder(BorderFactory.createTitledBorder("Parâmetros da Simulação"));
        
        // Slider de densidade de árvores (0-100%)
        labelDensidade = new JLabel("Densidade de Árvores: 70%");
        sliderDensidade = new JSlider(0, 100, 70);
        sliderDensidade.addChangeListener(e -> {
            labelDensidade.setText("Densidade de Árvores: " + sliderDensidade.getValue() + "%");
        });
        
        // Slider de probabilidade de raio (0-1%)
        labelProbIncendio = new JLabel("Probabilidade de Raio: 0.05%");
        sliderProbIncendio = new JSlider(0, 100, 5);
        sliderProbIncendio.addChangeListener(e -> {
            double valor = sliderProbIncendio.getValue() / 100.0;
            labelProbIncendio.setText("Probabilidade de Raio: " + String.format("%.2f", valor) + "%");
        });
        
        // Slider de taxa de crescimento (0-10%)
        labelProbCrescimento = new JLabel("Taxa de Crescimento: 1.0%");
        sliderProbCrescimento = new JSlider(0, 100, 10);
        sliderProbCrescimento.addChangeListener(e -> {
            double valor = sliderProbCrescimento.getValue() / 10.0;
            labelProbCrescimento.setText("Taxa de Crescimento: " + String.format("%.1f", valor) + "%");
        });
        
        // Slider de velocidade (100-1000ms)
        labelVelocidade = new JLabel("Velocidade: 300ms");
        sliderVelocidade = new JSlider(100, 1000, 300);
        sliderVelocidade.setMajorTickSpacing(200);
        sliderVelocidade.setMinorTickSpacing(50);
        sliderVelocidade.setPaintTicks(true);
        sliderVelocidade.setPaintLabels(true);
        sliderVelocidade.addChangeListener(e -> {
            int valor = sliderVelocidade.getValue();
            labelVelocidade.setText("Velocidade: " + valor + "ms");
            if (timer != null) {
                timer.setDelay(valor);
            }
        });
        
        // Adiciona todos os componentes ao painel
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
    
    /**
     * Cria o painel com os botões de controle
     * @return JPanel configurado com os botões
     */
    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel();
        
        // Cria os botões
        botaoPasso = new JButton("▶ Passo");
        botaoIniciar = new JButton("▶ Iniciar");
        botaoReset = new JButton("🔄 Reset");
        JButton botaoAplicar = new JButton("✅ Aplicar");
        
        // Cria os labels de informação
        labelPassos = new JLabel("Passos: 0");
        labelQueimado = new JLabel("Queimado: 0%");
        labelArvores = new JLabel("Árvores: 0");
        labelQueimadas = new JLabel("Queimadas: 0");
        
        // Associa as ações aos botões
        botaoPasso.addActionListener(e -> passoSimulacao());
        botaoIniciar.addActionListener(e -> alternarSimulacao());
        botaoReset.addActionListener(e -> resetSimulacao());
        botaoAplicar.addActionListener(e -> aplicarParametros());
        
        // Adiciona todos os componentes ao painel
        painel.add(botaoPasso);
        painel.add(botaoIniciar);
        painel.add(botaoReset);
        painel.add(botaoAplicar);
        painel.add(labelPassos);
        painel.add(labelQueimado);
        painel.add(labelArvores);
        painel.add(labelQueimadas);
        
        return painel;
    }
    
    /**
     * Executa um passo da simulação
     */
    private void passoSimulacao() {
        if (incendio.isExecutando()) {
            incendio.passo();
            visualizador.atualizar();
            atualizarLabels();
        }
    }
    
    /**
     * Inicia ou pausa a simulação automática
     */
    private void alternarSimulacao() {
        simulando = !simulando;
        if (simulando) {
            botaoIniciar.setText("⏸ Pausar");
            timer.start();
        } else {
            botaoIniciar.setText("▶ Iniciar");
            timer.stop();
        }
    }
    
    /**
     * Reinicia a simulação com os parâmetros atuais
     */
    private void resetSimulacao() {
        simulando = false;
        if (timer.isRunning()) timer.stop();
        botaoIniciar.setText("▶ Iniciar");
        criarSimulacao();
    }
    
    /**
     * Aplica os novos parâmetros sem reiniciar a simulação
     */
    private void aplicarParametros() {
        boolean estavaSimulando = simulando;
        
        // Pausa se estiver rodando
        if (simulando) {
            alternarSimulacao();
        }
        
        // Recria a simulação com novos parâmetros
        criarSimulacao();
        
        // Aplica a velocidade atual
        if (timer != null && sliderVelocidade != null) {
            timer.setDelay(sliderVelocidade.getValue());
        }
        
        // Retoma se estava rodando
        if (estavaSimulando) {
            alternarSimulacao();
        }
    }
    
    /**
     * Atualiza todos os labels com os valores atuais da simulação
     */
    private void atualizarLabels() {
        labelPassos.setText("Passos: " + incendio.getPassos());
        labelQueimado.setText(String.format("Queimado: %.1f%%", incendio.getPercentualQueimado()));
        labelArvores.setText("Árvores: " + incendio.getArvoresRestantes());
        labelQueimadas.setText("Queimadas: " + incendio.getArvoresQueimadas());
    }
    
    /**
     * Mostra uma janela com os resultados finais da simulação
     */
    private void mostrarResultadoFinal() {
        JOptionPane.showMessageDialog(this, 
            String.format("=== RESULTADO DA SIMULAÇÃO ===\n\n" +
                          "Total de passos: %d\n" +
                          "Árvores iniciais: %d\n" +
                          "Árvores queimadas: %d (%.1f%%)\n" +
                          "Árvores sobreviventes: %d\n\n" +
                          "A simulação terminou porque não há mais fogo ativo.",
                          incendio.getPassos(),
                          incendio.getTotalArvoresIniciais(),
                          incendio.getArvoresQueimadas(),
                          incendio.getPercentualQueimado(),
                          incendio.getArvoresRestantes()),
            "Fim da Simulação",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Método principal - ponto de entrada do programa
     * @param args Argumentos da linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        // Executa a interface gráfica na thread de eventos do Swing
        SwingUtilities.invokeLater(() -> {
            try {
                // Tenta usar o visual nativo do sistema operacional
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Cria e exibe a janela principal
            new SimuladorQueimadas();
        });
    }
}