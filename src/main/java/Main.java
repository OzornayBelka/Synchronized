import java.util.*;

public class Main {
    final static String LETTERS = "RLRFR";
    final static int LENGTH = 100;
    final static int THREADS = 1000;
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) {

        for (int i = 0; i < THREADS; i++) {
            new Thread(() -> {
                String route = generateRoute(LETTERS, LENGTH);//получили сгенерированную строку из 100 символов
                int frequency = (int) route.chars().filter(ch -> ch == 'R').count();//частота символа R

                synchronized (sizeToFreq) {
                    if (sizeToFreq.containsKey(frequency)) {
                        sizeToFreq.put(frequency, sizeToFreq.get(frequency) + 1);
                    } else {
                        sizeToFreq.put(frequency, 1);
                    }
                }
            }).start();
        }
        Map.Entry<Integer, Integer> max = sizeToFreq
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get();
        System.out.println("Самое частое количество повторений " + max.getKey()
                + " Встретилось " + max.getValue() + " раз");


        System.out.println("Другие размеры: ");
        sizeToFreq
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(e -> System.out.println(" - " + e.getKey() + " (" + e.getValue() + " раз)"));

    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();//создали обьект класса
        StringBuilder route = new StringBuilder();//создали изменяемую строку
        for (int i = 0; i < length; i++) {
            //добавляем в строку символ из переданной строки (индекс этого символа:
            //обьект класса random (генерирует случайные числа) любое число в диапазоне от 0 до длинны
            //переданной строки(nextInt(int a)
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}

