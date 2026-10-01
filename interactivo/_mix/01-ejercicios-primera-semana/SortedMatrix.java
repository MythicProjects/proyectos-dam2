import java.util.Random;

public class SortedMatrix {

    public static void run() {
        int rows = 4;
        int columns = 5;

        int [][] matrix = new int[rows][columns];


        randomize(matrix, 1, 100);
        System.out.println("Antes de ordenar:");
        printMatrix(matrix);

        sortNumbers(matrix);
        System.out.println("\nDespués de ordenar:");
        printMatrix(matrix);
    }

    public static void randomize(int[][] matriz, int min, int max) {
        Random randomNum = new Random();

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                matriz[i][j] = randomNum.nextInt(max - min + 1) + min;
            }
        }
    }

    public static void sortNumbers(int[][] matrixIntro) {
        int rLength = matrixIntro.length;
        int cLength = matrixIntro[0].length;
        int totalLength = rLength * cLength;

        // ----- Extract numbers
        int[] numList = new int[totalLength];
        int index = 0;

        for (int i = 0; i < rLength; i++) {
            for (int j = 0; j < cLength; j++) {
                numList[index++] = matrixIntro[i][j];
            }
        }

        // ----- Bubble Sort
        for (int i = 0; i < totalLength - 1; i++) {
            for (int j = 0; j < totalLength - 1 - i; j++) {
                if (numList[j] > numList[j + 1]) {
                    int temp = numList[j];
                    numList[j] = numList[j + 1];
                    numList[j + 1] = temp;
                }
            }
        }

        // ----- Reorder
        index = 0;
        for (int i = 0; i < rLength; i++) {
            for (int j = 0; j < cLength; j++) {
                matrixIntro[i][j] = numList[index++];
            }
        }
    }

    public static void printMatrix(int[][] matriz) {
        for (int[] row : matriz) {
            for (int num : row) {
                System.out.printf("%4d", num);
            }
            System.out.println();
        }
    }
}
