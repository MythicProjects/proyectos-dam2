public class MonthCalendar {

    public static void run() {
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio",
                            "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        String[] days = {"  L ", " M ", " X ", " J ", " V ", " S ", " D "};
        int startPos = 3; // January 2026

        for (String m : meses) {
            System.out.println("----------");
            System.out.println(m);
            System.out.println("----------");
            
            for (int w = 0; w < days.length; w++){
                System.out.print(days[w]);
            }

            System.out.println("");
            
            int total = totalMonthDays(m);
            
            for (int j = 1; j <= total; j++){
                //Encajar dias
                if(j <= startPos){
                    System.out.print("   ");
                }
                else if(j - startPos < 10){
                    System.out.print("  "+ (j - startPos));
                }
                else{
                    System.out.print(" "+ (j - startPos));
                }

                // Separacion
                if(j % 7 == 0){
                    System.out.println("");
                }

                if(j == total){
                    startPos = j % 7;
                }
            }
            System.out.println("");

        }
    }

    private static int totalMonthDays(String month){
        String[] a = {"Enero", "Marzo", "Mayo", "Julio", "Agosto", "Octubre", "Diciembre"};
        String[] b = {"Abril", "Junio", "Septiembre", "Noviembre"};

        // 31
        for (int i = 0; i < a.length; i++){
                if(a[i].equals(month)){
                    return 31;
                }
        }

        //30
        for (int i = 0; i < b.length; i++){
            if(b[i].equals(month)){
                return 30;
            }
        }

        //28
        return 28;
    }
}