package visualizacao;

import modelo.Floresta;
import modelo.Celula;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VisualizadorFloresta extends JPanel {
    private final Floresta floresta;
    private final int tamanhoCelula;
    
    public VisualizadorFloresta(Floresta floresta, int tamanhoCelula) {
        this.floresta = floresta;
        this.tamanhoCelula = tamanhoCelula;
        
        setPreferredSize(new Dimension(
            floresta.getColunas() * tamanhoCelula,
            floresta.getLinhas() * tamanhoCelula
        ));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int linha = e.getY() / tamanhoCelula;
                int coluna = e.getX() / tamanhoCelula;
                if (linha >= 0 && linha < floresta.getLinhas() &&
                    coluna >= 0 && coluna < floresta.getColunas()) {
                    // Inicia fogo onde o usuário clicou
                    if (floresta.getCelula(linha, coluna) == Celula.ARVORE) {
                        // Nota: Para iniciar fogo, precisamos de um método na floresta
                        // Isso seria melhor implementado com um callback
                    }
                }
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        for (int i = 0; i < floresta.getLinhas(); i++) {
            for (int j = 0; j < floresta.getColunas(); j++) {
                Celula celula = floresta.getCelula(i, j);
                
                switch (celula) {
                    case ARVORE:
                        g.setColor(new Color(34, 139, 34)); // Verde floresta
                        break;
                    case QUEIMANDO:
                        g.setColor(new Color(255, 69, 0)); // Laranja fogo
                        break;
                    case CINZA:
                        g.setColor(new Color(105, 105, 105)); // Cinza escuro
                        break;
                    default:
                        g.setColor(new Color(222, 184, 135)); // Marrom claro (terra)
                        break;
                }
                
                g.fillRect(j * tamanhoCelula, i * tamanhoCelula, 
                          tamanhoCelula, tamanhoCelula);
                
                // Desenha borda
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