/**
 * Pacote view - Contém as classes responsáveis pela interface gráfica
 */
package src.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import src.model.Celula;
import src.model.Floresta;

/**
 * Componente gráfico que desenha a floresta na tela
 * Estende JPanel para permitir desenho personalizado
 */
public class VisualizadorFloresta extends JPanel {

    // Referência para a floresta que será desenhada
    private final Floresta floresta;

    // Tamanho em pixels de cada célula na grade
    private final int tamanhoCelula;

    /**
     * Construtor do visualizador
     * 
     * @param floresta      A floresta a ser visualizada
     * @param tamanhoCelula Tamanho de cada célula em pixels
     */
    public VisualizadorFloresta(Floresta floresta, int tamanhoCelula) {
        this.floresta = floresta;
        this.tamanhoCelula = tamanhoCelula;

        // Define o tamanho preferido do painel com base no tamanho da grade
        setPreferredSize(new Dimension(
                floresta.getColunas() * tamanhoCelula,
                floresta.getLinhas() * tamanhoCelula));
    }

    /**
     * Método que desenha a floresta na tela
     * É chamado automaticamente pelo Swing quando necessário
     * 
     * @param g Objeto Graphics usado para desenhar
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Percorre todas as células da floresta
        for (int i = 0; i < floresta.getLinhas(); i++) {
            for (int j = 0; j < floresta.getColunas(); j++) {
                Celula celula = floresta.getCelula(i, j);

                // Define a cor baseada no estado da célula
                if (celula == Celula.ARVORE) {
                    // Verde floresta para árvores saudáveis
                    g.setColor(new Color(34, 139, 34));
                } else if (celula == Celula.QUEIMANDO) {
                    // Laranja vibrante para fogo
                    g.setColor(new Color(255, 69, 0));
                } else if (celula == Celula.CINZA) {
                    // Cinza escuro para área queimada
                    g.setColor(new Color(105, 105, 105));
                } else {
                    // Marrom claro para solo vazio
                    g.setColor(new Color(222, 184, 135));
                }

                // Desenha o retângulo preenchido da célula
                g.fillRect(j * tamanhoCelula, i * tamanhoCelula,
                        tamanhoCelula, tamanhoCelula);

                // Desenha a borda preta ao redor da célula
                g.setColor(Color.BLACK);
                g.drawRect(j * tamanhoCelula, i * tamanhoCelula,
                        tamanhoCelula, tamanhoCelula);
            }
        }
    }

    /**
     * Atualiza a visualização
     * Força o redesenho do componente
     */
    public void atualizar() {
        repaint(); // Solicita ao Swing que redesenhe o componente
    }
}