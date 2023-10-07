import java.util.*;

public class Main {
    final static String LETTERS = "RLRFR";
    final static int LENGTH = 100;
    final static int THREADS = 1000;
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    public static final List<Thread> listThread = new ArrayList<>();

    public static void main(String[] args) {

        Thread printThread = new Thread(() -> {
            while (!Thread.interrupted()) {
                synchronized (sizeToFreq) {
                    try {
                        sizeToFreq.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                    print();
                }
            }
        });
        printThread.start();
        for (int i = 0; i < THREADS; i++) {
            listThread.add(createThread());
        }

        for (Thread thread : listThread){
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        printThread.interrupt();

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

    public static void print(){
        Map.Entry<Integer, Integer> max = sizeToFreq
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get();
        System.out.println("Текущий лидер: " + max.getKey()
                + " Встретилось " + max.getValue() + " раз");
    }

    public static Thread createThread(){
        Thread thread = new Thread(() -> {
            String route = generateRoute(LETTERS, LENGTH);//получили сгенерированную строку из 100 символов
            int frequency = (int) route.chars().filter(ch -> ch == 'R').count();//частота символа R

            synchronized (sizeToFreq) {
                if (sizeToFreq.containsKey(frequency)) {
                    sizeToFreq.put(frequency, sizeToFreq.get(frequency) + 1);
                } else {
                    sizeToFreq.put(frequency, 1);
                }
                sizeToFreq.notify();
            }
        }) ;
        return thread;
    }
}

