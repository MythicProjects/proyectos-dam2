import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException{
        System.out.println("=============== Ejercicio 2: Calendario ===============");
        MonthCalendar.run();

        System.out.println("=============== Ejercicio 3: Figura ===============");
        Figure.run();
        
        System.out.println("=============== Ejercicio 4: Matriz ordenada ===============");
        SortedMatrix.run();

        System.out.println("=============== Ejercicio 5: Analisis de palabras ===============");
        List<String> textList = Files.readAllLines(Path.of("assets/spanish.lst"));
        TextAnalisis analisis = new TextAnalisis(textList);
        analisis.run();

        System.out.println("=============== Ejercicio 6: Calculo de matriz ===============");
        CalculateMatrix.run();
    }
}
