import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RobotDelivery {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    public static final Object lock = new Object();

    public static void main(String[] args) {
        int numThreads = 1000;

        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                String route = generateRoute("RLRFR", 100);
                int countR = countCommands(route, 'R');
                updateFrequency(countR);
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        printFrequencies();
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static int countCommands(String route, char command) {
        int count = 0;
        for (char c : route.toCharArray()) {
            if (c == command) {
                count++;
            }
        }
        return count;
    }

    public static void updateFrequency(int size) {
        synchronized (lock) {
            sizeToFreq.put(size, sizeToFreq.getOrDefault(size, 0) + 1);
        }
    }

    public static void printFrequencies() {
        int maxFreq = 0;
        int mostCommonSize = 0;

        System.out.println("Другие размеры:");

        synchronized (lock) {
            for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
                int size = entry.getKey();
                int freq = entry.getValue();

                System.out.println("- " + size + " (" + freq + " раз)");

                if (freq > maxFreq) {
                    maxFreq = freq;
                    mostCommonSize = size;
                }
            }
        }

        System.out.println("\nСамое частое количество повторений: " + mostCommonSize + " (" + maxFreq + " раз)");
    }
}
