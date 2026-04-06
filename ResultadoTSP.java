public class ResultadoTSP {
    public int custoMinimo;
    public int[] melhorRota;
    public long tempoExecucaoMs;

    public ResultadoTSP() {
    }

    public ResultadoTSP(int custoMinimo, int[] melhorRota) {
        this(custoMinimo, melhorRota, 0L);
    }

    public ResultadoTSP(int custoMinimo, int[] melhorRota, long tempoExecucaoMs) {
        this.custoMinimo = custoMinimo;
        this.melhorRota = melhorRota != null ? melhorRota.clone() : null;
        this.tempoExecucaoMs = tempoExecucaoMs;
    }
}