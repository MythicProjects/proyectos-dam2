package view.dto;

public record HistogramVM(int[][] columns, int[] percents) {
    // ------------------------------------------------------------------
    // Defensive copies
    // ------------------------------------------------------------------
    public HistogramVM{
        columns = copy(columns);
        percents = percents.clone();
    }

    @Override public int[][] columns(){ return copy(columns); }
    @Override public int[] percents(){ return percents.clone(); }

    private static int[][] copy(int [][] source){
        int[][] result = new int[source.length][];
        for(int i = 0; i < source.length; i++){
            result[i] = source[i].clone();
        }

        return result;
    }
}
