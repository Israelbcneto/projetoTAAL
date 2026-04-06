public class TSPBranchAndBound implements AlgoritmoTSP {

    private int n;
    private int[][] dist;
    private int[] minimoSaida;
    private int melhorCusto;
    private int[] melhorRota;

    @Override
    public ResultadoTSP resolver(int n, int[][] dist) {
        if (n <= 0) {
            return new ResultadoTSP(0, new int[0]);
        }

        if (n == 1) {
            return new ResultadoTSP(0, new int[] { 0 });
        }

        this.n = n;
        this.dist = dist;
        this.melhorCusto = Integer.MAX_VALUE;
        this.melhorRota = null;
        this.minimoSaida = calcularMinimosSaida();

        for (int i = 0; i < n; i++) {
            if (minimoSaida[i] == Integer.MAX_VALUE) {
                return new ResultadoTSP(Integer.MAX_VALUE, criarRotaPadrao(n));
            }
        }

        int[] rotaAtual = new int[n];
        boolean[] visitado = new boolean[n];
        rotaAtual[0] = 0;
        visitado[0] = true;

        branchAndBound(1, 0, rotaAtual, visitado);

        if (melhorRota == null) {
            return new ResultadoTSP(Integer.MAX_VALUE, criarRotaPadrao(n));
        }

        return new ResultadoTSP(melhorCusto, melhorRota);
    }

    private void branchAndBound(int nivel, int custoAtual, int[] rotaAtual, boolean[] visitado) {
        if (nivel == n) {
            int ultimaCidade = rotaAtual[n - 1];
            int custoRetorno = dist[ultimaCidade][0];
            if (!arestaValida(ultimaCidade, 0, custoRetorno)) {
                return;
            }

            long custoTotal = (long) custoAtual + custoRetorno;
            if (custoTotal < melhorCusto) {
                melhorCusto = (int) custoTotal;
                melhorRota = rotaAtual.clone();
            }
            return;
        }

        int cidadeAtual = rotaAtual[nivel - 1];
        long limiteInferior = calcularLimiteInferior(cidadeAtual, custoAtual, visitado);
        if (limiteInferior >= melhorCusto) {
            return;
        }

        for (int prox = 1; prox < n; prox++) {
            if (visitado[prox]) {
                continue;
            }

            int custoAresta = dist[cidadeAtual][prox];
            if (!arestaValida(cidadeAtual, prox, custoAresta)) {
                continue;
            }

            long novoCusto = (long) custoAtual + custoAresta;
            if (novoCusto >= melhorCusto) {
                continue;
            }

            rotaAtual[nivel] = prox;
            visitado[prox] = true;
            branchAndBound(nivel + 1, (int) novoCusto, rotaAtual, visitado);
            visitado[prox] = false;
        }
    }

    private long calcularLimiteInferior(int cidadeAtual, int custoAtual, boolean[] visitado) {
        long limiteInferior = custoAtual;

        if (minimoSaida[cidadeAtual] == Integer.MAX_VALUE) {
            return Long.MAX_VALUE;
        }
        limiteInferior += minimoSaida[cidadeAtual];

        for (int i = 0; i < n; i++) {
            if (!visitado[i]) {
                if (minimoSaida[i] == Integer.MAX_VALUE) {
                    return Long.MAX_VALUE;
                }
                limiteInferior += minimoSaida[i];
            }
        }

        return limiteInferior;
    }

    private int[] calcularMinimosSaida() {
        int[] minimos = new int[n];

        for (int i = 0; i < n; i++) {
            int minimo = Integer.MAX_VALUE;
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    continue;
                }

                int custo = dist[i][j];
                if (custo > 0 && custo < minimo) {
                    minimo = custo;
                }
            }
            minimos[i] = minimo;
        }

        return minimos;
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