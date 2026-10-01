public class Figure {

    public static void run() {
        int size= 13;
        int mid = size/2;
        for(int f = 0; f < size; f++){
            for(int c = 0; c < size; c++){
                boolean centro = f == mid || c == mid;
                boolean diagonal = f == c || f == size - 1 - c;
                boolean borde = f == 0 || f == size - 1 || c == 0 || c == size - 1;

                if (centro || (diagonal && !borde)){
                    System.out.print("  #");
                }
                else{
                    System.out.print("  -");
                }
            }
            System.out.println("");
        }
    }
}
