package src.model;

public class Incendio {
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
