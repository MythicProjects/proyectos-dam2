import java.util.Random;

public class CalculateMatrix {

    public static void run() {
        int rows = 2;
        int columns = 2;

        int [][] matrixA = new int[rows][columns];
        int [][] matrixB = new int[rows][columns];


        System.out.println("----------- Matrix A");
        randomize(matrixA, 1, 10);
        printMatrix(matrixA);
        System.out.println("----------- Matrix B");
        randomize(matrixB, 1, 10);
        printMatrix(matrixB);

        System.out.println("----------- Matrix C");
        int [][] matrixC = calculateMatrix(matrixA, matrixB);

        printMatrix(matrixC);

    }

    private static void randomize(int[][] matrix, int min, int max) {
        Random randomNum = new Random();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = randomNum.nextInt(max - min + 1) + min;
            }
        }
    }

    private static int [][] calculateMatrix(int [][] A, int [][] B){

        int rowsA = A.length;
        int colA = A[0].length;
        int colB = B[0].length;

        int [][] matrix = new int[rowsA][colB];

        for(int r = 0; r < rowsA; r++){
            for(int c = 0; c < colB; c++){
                int sum = 0;
                
                for(int k = 0; k < colA; k++){
                    sum += (A[r][k] * B[k][c]);
                }

                matrix[r][c] = sum;
                // System.out.println("A" + (r + 1) + " -- " + A[r][c]);
                // System.out.println("B" + (c + 1) + " -- " + B[c][r]);
            }
        }

        return matrix;
    } 


    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int num : row) {
                System.out.printf("%4d", num);
            }
            System.out.println();
        }
    }
}
