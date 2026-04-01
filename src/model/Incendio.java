/**
 * Pacote model - Contém as classes que representam a lógica da simulação
 */
package src.model;

/**
 * Classe que controla a simulação do incêndio
 * Gerencia o fluxo da simulação e mantém estatísticas
 */
public class Incendio {
    
    // Referência para a floresta que está sendo simulada
    private final Floresta floresta;
    
    // Contador de passos da simulação
    private int passos;
    
    // Indica se a simulação ainda está em execução (fogo ativo)
    private boolean executando;
    
    // Armazena o número inicial de árvores para calcular percentuais
    private int totalArvoresIniciais;
    
    /**
     * Construtor da simulação
     * @param linhas Número de linhas da floresta
     * @param colunas Número de colunas da floresta
     * @param densidadeArvores Porcentagem inicial de árvores
     * @param probIncendio Probabilidade de ignição por raio
     * @param probCrescimento Probabilidade de crescimento de novas árvores
     */
    public Incendio(int linhas, int colunas, double densidadeArvores,
                   double probIncendio, double probCrescimento) {
        // Cria a floresta com os parâmetros fornecidos
        this.floresta = new Floresta(linhas, colunas, densidadeArvores, 
                                     probIncendio, probCrescimento);
        this.passos = 0;
        this.executando = true;
        this.totalArvoresIniciais = floresta.contarArvores();
    }
    
    /**
     * Inicia a simulação criando focos de incêndio iniciais
     */
    public void iniciar() {
        floresta.iniciarIncendio();
    }
    
    /**
     * Inicia um incêndio em um ponto específico (para interação do usuário)
     * @param linha Coordenada linha onde iniciar o fogo
     * @param coluna Coordenada coluna onde iniciar o fogo
     */
    public void iniciarEmPonto(int linha, int coluna) {
        floresta.iniciarIncendioEmPonto(linha, coluna);
    }
    
    /**
     * Executa um passo da simulação
     * Atualiza a floresta e verifica se o fogo ainda está ativo
     */
    public void passo() {
        if (executando) {
            // Atualiza o estado da floresta
            floresta.atualizar();
            passos++;
            
            // Verifica se ainda há fogo ativo
            if (!floresta.temIncendioAtivo()) {
                executando = false;  // Simulação terminou
            }
        }
    }
    
    /**
     * Executa múltiplos passos da simulação de uma vez
     * @param numeroPassos Quantidade de passos a serem executados
     */
    public void simular(int numeroPassos) {
        for (int i = 0; i < numeroPassos && executando; i++) {
            passo();
        }
    }
    
    /**
     * Retorna a floresta sendo simulada
     * @return Objeto Floresta atual
     */
    public Floresta getFloresta() {
        return floresta;
    }
    
    /**
     * Retorna o número de passos já executados
     * @return Quantidade de passos
     */
    public int getPassos() {
        return passos;
    }
    
    /**
     * Verifica se a simulação ainda está em execução
     * @return true se ainda há fogo ativo
     */
    public boolean isExecutando() {
        return executando;
    }
    
    /**
     * Calcula o percentual de árvores que foram queimadas
     * @return Porcentagem de árvores queimadas (0 a 100)
     */
    public double getPercentualQueimado() {
        if (totalArvoresIniciais == 0) return 0;
        int arvoresQueimadas = floresta.contarCinza();
        return (arvoresQueimadas * 100.0) / totalArvoresIniciais;
    }
    
    /**
     * Retorna o número de árvores que foram queimadas
     * @return Quantidade de árvores que viraram cinza
     */
    public int getArvoresQueimadas() {
        return floresta.contarCinza();
    }
    
    /**
     * Retorna o número de árvores que ainda estão vivas
     * @return Quantidade de árvores restantes
     */
    public int getArvoresRestantes() {
        return floresta.contarArvores();
    }
    
    /**
     * Retorna o número total de árvores no início da simulação
     * @return Quantidade inicial de árvores
     */
    public int getTotalArvoresIniciais() {
        return totalArvoresIniciais;
    }
}