public class TSPGreedy implements AlgoritmoTSP {

    @Override
    public ResultadoTSP resolver(int n, int[][] dist) {
        if (n <= 0) {
            return new ResultadoTSP(0, new int[0]);
        }

        if (n == 1) {
            return new ResultadoTSP(0, new int[] { 0 });
        }

        boolean[] visitado = new boolean[n];
        int[] rota = new int[n];
        long custoTotal = 0L;
        int cidadeAtual = 0;

        rota[0] = 0;
        visitado[0] = true;

        for (int posicao = 1; posicao < n; posicao++) {
            int melhorVizinho = -1;
            int menorDistancia = Integer.MAX_VALUE;

            for (int candidata = 1; candidata < n; candidata++) {
                int custo = dist[cidadeAtual][candidata];
                if (visitado[candidata] || !arestaValida(cidadeAtual, candidata, custo)) {
                    continue;
                }

                if (custo < menorDistancia) {
                    menorDistancia = custo;
                    melhorVizinho = candidata;
                }
            }

            if (melhorVizinho == -1) {
                return new ResultadoTSP(Integer.MAX_VALUE, criarRotaPadrao(n));
            }

            rota[posicao] = melhorVizinho;
            visitado[melhorVizinho] = true;
            custoTotal += menorDistancia;
            cidadeAtual = melhorVizinho;
        }

        int custoRetorno = dist[cidadeAtual][0];
        if (!arestaValida(cidadeAtual, 0, custoRetorno)) {
            return new ResultadoTSP(Integer.MAX_VALUE, criarRotaPadrao(n));
        }

        custoTotal += custoRetorno;
        if (custoTotal > Integer.MAX_VALUE) {
            return new ResultadoTSP(Integer.MAX_VALUE, criarRotaPadrao(n));
        }

        return new ResultadoTSP((int) custoTotal, rota);
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