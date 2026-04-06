public class TSPBacktracking implements AlgoritmoTSP {

    @Override
    public ResultadoTSP resolver(int n, int[][] dist) {
        if (n <= 0) {
            return new ResultadoTSP(0, new int[0]);
        }

        if (n == 1) {
            return new ResultadoTSP(0, new int[] { 0 });
        }

        int[] permutacao = new int[n - 1];
        for (int i = 0; i < permutacao.length; i++) {
            permutacao[i] = i + 1;
        }

        long melhorCusto = Long.MAX_VALUE;
        int[] melhorRota = null;

        do {
            long custoAtual = 0L;
            int cidadeAnterior = 0;
            boolean rotaValida = true;

            for (int cidade : permutacao) {
                int custoAresta = dist[cidadeAnterior][cidade];
                if (!arestaValida(cidadeAnterior, cidade, custoAresta)) {
                    rotaValida = false;
                    break;
                }

                custoAtual += custoAresta;
                cidadeAnterior = cidade;

                if (custoAtual >= melhorCusto) {
                    rotaValida = false;
                    break;
                }
            }

            if (!rotaValida) {
                continue;
            }

            int custoRetorno = dist[cidadeAnterior][0];
            if (!arestaValida(cidadeAnterior, 0, custoRetorno)) {
                continue;
            }

            long custoTotal = custoAtual + custoRetorno;
            if (custoTotal < melhorCusto) {
                melhorCusto = custoTotal;
                melhorRota = new int[n];
                melhorRota[0] = 0;
                for (int i = 1; i < n; i++) {
                    melhorRota[i] = permutacao[i - 1];
                }
            }
        } while (proximaPermutacao(permutacao));

        if (melhorRota == null || melhorCusto > Integer.MAX_VALUE) {
            return new ResultadoTSP(Integer.MAX_VALUE, criarRotaPadrao(n));
        }

        return new ResultadoTSP((int) melhorCusto, melhorRota);
    }

    private boolean arestaValida(int origem, int destino, int custo) {
        return origem != destino && custo > 0;
    }

    private boolean proximaPermutacao(int[] valores) {
        int i = valores.length - 2;
        while (i >= 0 && valores[i] >= valores[i + 1]) {
            i--;
        }

        if (i < 0) {
            return false;
        }

        int j = valores.length - 1;
        while (valores[j] <= valores[i]) {
            j--;
        }

        trocar(valores, i, j);
        inverterSufixo(valores, i + 1, valores.length - 1);
        return true;
    }

    private void trocar(int[] valores, int i, int j) {
        int aux = valores[i];
        valores[i] = valores[j];
        valores[j] = aux;
    }

    private void inverterSufixo(int[] valores, int inicio, int fim) {
        while (inicio < fim) {
            trocar(valores, inicio, fim);
            inicio++;
            fim--;
        }
    }

    private int[] criarRotaPadrao(int n) {
        int[] rota = new int[n];
        for (int i = 0; i < n; i++) {
            rota[i] = i;
        }
        return rota;
    }
}