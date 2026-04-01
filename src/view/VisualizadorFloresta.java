package src.view;

import javax.swing.JPanel;
import java.awt.*;
import src.model.Celula;
import src.model.Floresta;

public class VisualizadorFloresta extends JPanel {
    private final Floresta floresta;
    private final int tamanhoCelula;

    public VisualizadorFloresta(Floresta floresta, int tamanhoCelula) {
        this.floresta = floresta;
        this.tamanhoCelula = tamanhoCelula;

        setPreferredSize(new Dimension(
                floresta.getColunas() * tamanhoCelula,
                floresta.getLinhas() * tamanhoCelula));
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
