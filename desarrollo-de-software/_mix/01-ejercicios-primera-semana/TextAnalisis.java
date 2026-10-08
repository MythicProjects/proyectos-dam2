import java.util.*;

public class TextAnalisis {

    private final List<String> words;

    public TextAnalisis(List<String> rawText){
        words = new ArrayList<>();

            for (String line : rawText) {
                String word = line.trim().toLowerCase()
                    .replace("\\", "ñ").replace("{", "ü").replace("/", "");

                if(!word.isEmpty())
                    words.add(word);
        }
    }

    public void run() {

        Map<Character, Integer> letters = countLetters();
        Map<Character, Integer> firstLetters = countFirstLetters();
        int totalLetters = sum(letters);

        System.out.println("---------- Part 1 ---");
        System.out.println("Número total de letras: " + totalLetters);
        printCounts(letters, "La letra %c aparece %d veces%n");

        System.out.println("---------- Part 2 ---");
        System.out.println("Número total de palabras: " + words.size());

        System.out.println("---------- Part 3 ---");
        printCounts(firstLetters, "Palabras que empiezan por %c: %d%n");

        System.out.println("---------- Part 4 ---");
        printAverages(letters, "Media de '%c' por palabra: %.4f%n");

        System.out.println("---------- Part 5 ---");
        printAverages(firstLetters, "Proporción de palabras que empiezan por '%c': %.4f%n");
    }

    private Map<Character, Integer> countLetters(){
        Map<Character, Integer> count = new TreeMap<>();

        for(String word : words){
            for(char c : word.toLowerCase().toCharArray()){
                if (Character.isLetter(c)){
                    count.merge(c, 1, Integer::sum);
                }
            }
        }
        return count;
    }

    private Map<Character, Integer> countFirstLetters(){
        Map<Character, Integer> count = new TreeMap<>();

        for(String word : words){
            char first = word.charAt(0);
            if (Character.isLetter(first)){
                count.merge(first, 1, Integer::sum);
            }
        }
        return count;
    }

    private int sum(Map<Character, Integer> counts){
        int total = 0;
        for(int n : counts.values()){
            total += n;
        }

        return total;
    }

    private void printCounts(Map<Character, Integer> counts, String format){
        for(Map.Entry<Character, Integer> e : counts.entrySet()){
            System.out.printf(format, e.getKey(), e.getValue());
        }
    }

    private void printAverages(Map<Character, Integer> counts, String format){
        int totalWords = words.size();
        for(Map.Entry<Character, Integer> e : counts.entrySet()){
            System.out.printf(format, e.getKey(), (double) e.getValue() / totalWords);
        }
    }
}
/*
    Errores o resultados incorrectos

    1. Estás contando \, { y / como letras. No conviertes la ñ ni la ü del archivo, y tampoco filtras, así que en tus resultados aparecerán "Letter \ appears..." y "Letter { appears...". Arréglalo al leer y filtra con Character.isLetter:

    java
    word.replace("\\", "ñ").replace("{", "ü").replace("/", "")

    2. "Numero total de letras:" no imprime ningún total. Imprimes el título pero luego solo el desglose por letra. Te falta sumar.

    3. Si llamas a run() dos veces, los resultados se duplican. Los mapas son campos y nunca se vacían. Mejor que cada método cree su propio mapa y lo devuelva, o que los limpies al empezar.

    4. Etiquetas equivocadas: en Main pone "Ejercicio 5: Matriz ordenada" (copiado del ejercicio 4) y en run hay un "cc" suelto en "Part 1 ---cc".

    Código más simple

    5. checkLettersStarting es innecesariamente complicado. El contador i y el break solo sirven para coger el primer carácter. Basta con:

    java
    for (String word : textList) {
    if (word.isEmpty()) continue;
    char first = Character.toLowerCase(word.charAt(0));
    numOfWordByLetter.merge(first, 1, Integer::sum);
    }

    6. getWords sobra. Copiar elemento a elemento se hace con this.textList.addAll(lista), o mejor aún, recibir la lista en el constructor.

    7. El if/else de contar se reduce a merge(c, 1, Integer::sum).

    8. Los métodos 4 y 5 son idénticos salvo el mapa y el texto. Puedes hacer uno solo que reciba ambos como parámetros.

    Estilo
    "Median" no es "media": median es mediana. Usa "average".
    Mezclas calcular e imprimir en el mismo método. Es mejor que un método calcule y devuelva el mapa, y otro lo muestre.
    Modificadores inconsistentes: textList es private y los mapas no. Hazlos todos private.
    Indentación: el import tiene espacios delante y los mapas están desalineados.
    Notación científica: usa printf con %.4f para que no salga 3.48E-5. 
  */