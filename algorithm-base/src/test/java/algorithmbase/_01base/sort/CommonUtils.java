package algorithmbase._01base.sort;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class CommonUtils {

    private static Random random = new Random();

    public static int[] generate(int start, int end, int num) {
        int[] ints = new int[num];
        for (int i = 0; i < num; i++) {
            ints[i] = random.nextInt(end - start) + start;
        }
        return ints;
    }

    public static int[] generate(int num) {
        int[] ints = new int[num];
        for (int i = 0; i < num; i++) {
            ints[i] = random.nextInt(100);
        }
        return ints;
    }

    public static int[] generateTailOrder(int num, int tail) {
        int[] ints = new int[num];
        int max = 0;
        for (int i = 0; i < num - tail; i++) {
            ints[i] = random.nextInt(num);
            if (ints[i] > max) {
                max = ints[i];
            }
        }
        int tailIndex = num - tail;
        int[] orderTails = IntStream.range(max + 1, max + tail + 1).toArray();
        for (int orderTail : orderTails) {
            ints[tailIndex++] = orderTail;
        }
        return ints;
    }

    public static void printArray(int[] a, String desc) {
        if (desc != null) {
            System.out.println(desc + "" + Arrays.toString(a));
        } else {
            System.out.println(Arrays.toString(a));
        }
    }

    @Test
    public void test() {
        System.out.println(Arrays.toString(generate(10)));

        System.out.println(Arrays.toString(generateTailOrder(10, 5)));
    }
}
