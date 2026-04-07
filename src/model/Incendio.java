/**
 * Pacote modelo - Contém as classes que representam a lógica da simulação
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
    
    // Armazena o número de árvores que sobreviveram (NÃO queimaram)
    private int arvoresSobreviventes;
    
    // Flag para indicar se a simulação já terminou e a contagem foi feita
    private boolean contagemFinalRealizada;
    
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
        this.contagemFinalRealizada = false;
        
        // Conta o total inicial de árvores (ANTES de iniciar o fogo)
        this.totalArvoresIniciais = floresta.contarArvores();
        this.arvoresSobreviventes = this.totalArvoresIniciais;
    }
    
    /**
     * Inicia a simulação criando focos de incêndio iniciais
     */
    public void iniciar() {
        floresta.iniciarIncendio();
        // Após iniciar o fogo, recalcula as árvores sobreviventes
        recalcularSobreviventes();
    }
    
    /**
     * Inicia um incêndio em um ponto específico (para interação do usuário)
     * @param linha Coordenada linha onde iniciar o fogo
     * @param coluna Coordenada coluna onde iniciar o fogo
     */
    public void iniciarEmPonto(int linha, int coluna) {
        floresta.iniciarIncendioEmPonto(linha, coluna);
        recalcularSobreviventes();
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
            
            // Recalcula as árvores sobreviventes após a atualização
            recalcularSobreviventes();
            
            // Verifica se ainda há fogo ativo
            if (!floresta.temIncendioAtivo()) {
                executando = false;  // Simulação terminou
                realizarContagemFinal();  // Faz a contagem final precisa
            }
        }
    }
    
    /**
     * Recalcula o número de árvores sobreviventes
     * Conta apenas células que estão no estado ARVORE
     */
    private void recalcularSobreviventes() {
        this.arvoresSobreviventes = floresta.contarArvores();
    }
    
    /**
     * Realiza a contagem final quando a simulação termina
     * Garante que a contagem está correta
     */
    private void realizarContagemFinal() {
        if (!contagemFinalRealizada) {
            // Conta novamente para garantir precisão
            this.arvoresSobreviventes = floresta.contarArvores();
            this.contagemFinalRealizada = true;
            
            // DEBUG: Imprime informações no console para verificação
            System.out.println("=== CONTAGEM FINAL ===");
            System.out.println("Árvores iniciais: " + totalArvoresIniciais);
            System.out.println("Árvores sobreviventes: " + arvoresSobreviventes);
            System.out.println("Árvores queimadas: " + getArvoresQueimadas());
            System.out.println("Cinzas (áreas queimadas): " + floresta.contarCinza());
            System.out.println("=====================");
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
        int arvoresQueimadas = totalArvoresIniciais - arvoresSobreviventes;
        return (arvoresQueimadas * 100.0) / totalArvoresIniciais;
    }
    
    /**
     * Retorna o número de árvores que foram queimadas
     * @return Quantidade de árvores que viraram cinza
     */
    public int getArvoresQueimadas() {
        return totalArvoresIniciais - arvoresSobreviventes;
    }
    
    /**
     * Retorna o número de árvores que ainda estão vivas
     * @return Quantidade de árvores sobreviventes
     */
    public int getArvoresRestantes() {
        return arvoresSobreviventes;
    }
    
    /**
     * Retorna o número total de árvores no início da simulação
     * @return Quantidade inicial de árvores
     */
    public int getTotalArvoresIniciais() {
        return totalArvoresIniciais;
    }
    
    /**
     * Verifica se a contagem está consistente
     * @return true se os números são consistentes
     */
    public boolean isContagemConsistente() {
        int arvoresAtuais = floresta.contarArvores();
        int cinzas = floresta.contarCinza();
        int queimando = floresta.contarQueimando();
        int vazios = 0;
        
        // Conta os vazios
        for (int i = 0; i < floresta.getLinhas(); i++) {
            for (int j = 0; j < floresta.getColunas(); j++) {
                if (floresta.getCelula(i, j) == Celula.VAZIO) {
                    vazios++;
                }
            }
        }
        
        int totalCelulas = floresta.getLinhas() * floresta.getColunas();
        int soma = arvoresAtuais + cinzas + queimando + vazios;
        
        return soma == totalCelulas;
    }
}