import javax.swing.*;
import java.awt.*;

public class JanelaPoesia extends JFrame {
    private AutomatoPoesia automato;
    
    public JanelaPoesia() {
        setTitle("Autômato de Poesia Visual");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Cria autômato (15x20 letras)
        automato = new AutomatoPoesia(15, 20);
        add(automato, BorderLayout.CENTER);
        
        // Painel de botões com fundo escuro
        JPanel painelBotoes = new JPanel();
        painelBotoes.setBackground(new Color(60, 63, 65)); // Cinza escuro
        
        // Criando botões com cores de fundo escuras e texto claro
        JButton btnIniciar = criarBotao("▶ Iniciar", new Color(46, 125, 50)); // Verde escuro
        JButton btnParar = criarBotao("⏸ Parar", new Color(198, 40, 40)); // Vermelho escuro
        JButton btnAvancar = criarBotao("⏩ Avançar", new Color(2, 136, 209)); // Azul
        JButton btnLimpar = criarBotao("⌧ Limpar", new Color(123, 31, 162)); // Roxo
        JButton btnAleatorio = criarBotao("🎲 Aleatório", new Color(255, 111, 0)); // Laranja
        JButton btnPoema = criarBotao("📜 Poema", new Color(0, 121, 107)); // Verde petróleo
        JButton btnSair = criarBotao("✕ Sair", new Color(97, 97, 97)); // Cinza médio
        
        // Ações dos botões
        btnIniciar.addActionListener(e -> automato.iniciar());
        btnParar.addActionListener(e -> automato.parar());
        btnAvancar.addActionListener(e -> automato.avancarUmaGeracao());
        btnLimpar.addActionListener(e -> automato.limpar());
        btnAleatorio.addActionListener(e -> automato.aleatorio());
        btnPoema.addActionListener(e -> automato.poemaExemplo());
        btnSair.addActionListener(e -> System.exit(0));
        
        // Adiciona botões ao painel
        painelBotoes.add(btnIniciar);
        painelBotoes.add(btnParar);
        painelBotoes.add(btnAvancar);
        painelBotoes.add(btnLimpar);
        painelBotoes.add(btnAleatorio);
        painelBotoes.add(btnPoema);
        painelBotoes.add(btnSair);
        
        add(painelBotoes, BorderLayout.SOUTH);
        
        // Painel de instruções com fundo escuro e texto claro
        JPanel painelInstrucoes = new JPanel();
        painelInstrucoes.setBackground(new Color(43, 43, 43)); // Cinza mais escuro
        JLabel instrucoes = new JLabel(
            "<html>" +
            "<font color='#81C784'>🟢 Nova</font> | " +
            "<font color='#64B5F6'>🔵 Consoante</font> | " +
            "<font color='#FF8A65'>🔴 Vogal</font> | " +
            "<font color='#FFD700'>🟡 Palavra</font> | " +
            "<font color='#F48FB1'>Rosa: Velha</font> | " +
            "<font color='white'>Clique em célula para adicionar letra</font>" +
            "</html>"
        );
        instrucoes.setFont(new Font("Arial", Font.PLAIN, 12));
        instrucoes.setForeground(Color.WHITE); // Texto branco para garantir visibilidade
        painelInstrucoes.add(instrucoes);
        add(painelInstrucoes, BorderLayout.NORTH);
        
        pack();
        setLocationRelativeTo(null);
    }
    
    // Método auxiliar para criar botões padronizados
    private JButton criarBotao(String texto, Color corFundo) {
        JButton botao = new JButton(texto);
        botao.setBackground(corFundo);
        botao.setForeground(Color.WHITE); // Texto branco
        botao.setFocusPainted(false);
        botao.setFont(new Font("Arial", Font.BOLD, 12));
        botao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(corFundo.brighter(), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // Efeito hover (quando o mouse passa por cima)
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(corFundo.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(corFundo);
            }
        });
        
        return botao;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Tenta usar o tema Nimbus (mais moderno)
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
                // Se não conseguir, usa o padrão
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            new JanelaPoesia().setVisible(true);
        });
    }
}