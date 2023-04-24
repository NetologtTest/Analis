import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static BlockingQueue<String> PotokA = new ArrayBlockingQueue<>(100);
    private static BlockingQueue<String> PotokB = new ArrayBlockingQueue<>(100);
    private static BlockingQueue<String> PotokC = new ArrayBlockingQueue<>(100);

    private static int maxSizeA = 0;
    private static int maxSizeB = 0;
    private static int maxSizeC = 0;

    public static void main(String[] args) throws InterruptedException {
        for (int z = 0; z < 10_000; z++) {
            new Thread(() -> {
                try {
                    PotokA.put(generateText("abc", 100_000));
                    PotokB.put(generateText("abc", 100_000));
                    PotokC.put(generateText("abc", 100_000));
                } catch (InterruptedException e) {
                    return;
                }
            }).start();

            new Thread(() -> {

                maxSizeA = analis('a', PotokA);

            }).start();

            new Thread(() -> {
                maxSizeB = analis('b', PotokB);


            }).start();

            new Thread(() -> {
                maxSizeC = analis('c', PotokC);

            }).start();
        }

        System.out.println(maxSizeA);
        System.out.println(maxSizeB);
        System.out.println(maxSizeC);
    }

    public static int analis(char ch, BlockingQueue<String> Potok) {
        int maxSize = 0;


        for (int i = 0; i < Potok.size(); i++) {
            for (int j = 0; j < Potok.size(); j++) {
                if (i >= j) {
                    continue;
                }
                boolean bFound = false;
                for (int k = i; k < j; k++) {
                    try {
                        if (PotokC.take().charAt(k) == ch) {
                            bFound = true;
                            break;
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (!bFound && maxSize < j - i) {
                    maxSize = j - i;
                }
            }
        }


        return maxSize;
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();

    }

}


















