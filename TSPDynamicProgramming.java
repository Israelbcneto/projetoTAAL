import java.util.Arrays;

public class TSPDynamicProgramming implements AlgoritmoTSP {

    private static final long INFINITO = Long.MAX_VALUE / 4L;

    private int n;
    private int[][] dist;
    private long[][] memo;
    private boolean[][] calculado;
    private int[][] proximaCidade;
    private int mascaraCompleta;

    @Override
    public ResultadoTSP resolver(int n, int[][] dist) {
        if (n <= 0) {
            return new ResultadoTSP(0, new int[0]);
        }

        if (n == 1) {
            return new ResultadoTSP(0, new int[] { 0 });
        }

        if (n >= 31) {
            throw new IllegalArgumentException(
                    "A implementação de Programação Dinâmica suporta até 30 cidades com bitmask em int.");
        }

        this.n = n;
        this.dist = dist;
        this.mascaraCompleta = (1 << n) - 1;

        int totalEstados = 1 << n;
        this.memo = new long[n][totalEstados];
        this.calculado = new boolean[n][totalEstados];
        this.proximaCidade = new int[n][totalEstados];

        for (int i = 0; i < n; i++) {
            Arrays.fill(proximaCidade[i], -1);
        }

        long custoMinimo = heldKarp(0, 1);
        if (custoMinimo >= INFINITO || custoMinimo > Integer.MAX_VALUE) {
            return new ResultadoTSP(Integer.MAX_VALUE, criarRotaPadrao(n));
        }

        return new ResultadoTSP((int) custoMinimo, reconstruirRota());
    }

    private long heldKarp(int cidadeAtual, int mascaraVisitados) {
        if (mascaraVisitados == mascaraCompleta) {
            int custoRetorno = dist[cidadeAtual][0];
            return arestaValida(cidadeAtual, 0, custoRetorno) ? custoRetorno : INFINITO;
        }

        if (calculado[cidadeAtual][mascaraVisitados]) {
            return memo[cidadeAtual][mascaraVisitados];
        }

        calculado[cidadeAtual][mascaraVisitados] = true;

        long melhorCusto = INFINITO;
        int melhorProxima = -1;

        for (int proxima = 0; proxima < n; proxima++) {
            if ((mascaraVisitados & (1 << proxima)) != 0) {
                continue;
            }

            int custoAresta = dist[cidadeAtual][proxima];
            if (!arestaValida(cidadeAtual, proxima, custoAresta)) {
                continue;
            }

            long custoRestante = heldKarp(proxima, mascaraVisitados | (1 << proxima));
            if (custoRestante >= INFINITO) {
                continue;
            }

            long custoTotal = custoAresta + custoRestante;
            if (custoTotal < melhorCusto) {
                melhorCusto = custoTotal;
                melhorProxima = proxima;
            }
        }

        proximaCidade[cidadeAtual][mascaraVisitados] = melhorProxima;
        memo[cidadeAtual][mascaraVisitados] = melhorCusto;
        return melhorCusto;
    }

    private int[] reconstruirRota() {
        int[] rota = new int[n];
        int cidadeAtual = 0;
        int mascaraVisitados = 1;
        rota[0] = 0;

        for (int posicao = 1; posicao < n; posicao++) {
            int proxima = proximaCidade[cidadeAtual][mascaraVisitados];
            if (proxima < 0) {
                return criarRotaPadrao(n);
            }

            rota[posicao] = proxima;
            mascaraVisitados |= (1 << proxima);
            cidadeAtual = proxima;
        }

        return rota;
    }

    private boolean arestaValida(int origem, int destino, int custo) {
        return origem != destino && custo > 0;
    }

    private int[] criarRotaPadrao(int n) {
        int[] rota = new int[n];
        for (int i = 0; i < n; i++) {
            rota[i] = i;
        }
        return rota;
    }
}