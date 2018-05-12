import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Main {
    static int SIZE = 1500;
    static int[][] a = new int[SIZE][SIZE];
    static int[][] b = new int[SIZE][SIZE];
    static int[][] result = new int[a.length][b[0].length];
    public static void main(String[] args) {
        System.out.println("Hello World!");
        Random generator = new Random();
        for(int i = 0; i<SIZE*SIZE; ++i){
            a[i/SIZE][i%SIZE]=generator.nextInt();
            b[i/SIZE][i%SIZE]=generator.nextInt();
        }
      /*  System.out.println("Matrix a:");
        printArray(a);
        System.out.println("Matrix b:");
        printArray(b);
        System.out.println("Matrix c:");
        printArray(classicMultiply(a, b));
        printArray(asyncMultiply(a, b));*/
        long endTime, startTime = System.nanoTime();
        classicMultiply(a,b);
        endTime = System.nanoTime();
        System.out.println("Classic multiply took "+(endTime-startTime)+"nano seconds");
        startTime = System.nanoTime();
        asyncMultiply(a,b);
        endTime = System.nanoTime();
        System.out.println("Async   multiply took "+(endTime-startTime)+"nano seconds");

    }

    public static void printArray(int[][] a){
        for (int[] anA : a) {
            System.out.print("|");
            for (int j = 0; j < a[0].length; ++j) {
                System.out.print(anA[j]);
                if(j<a[0].length-1)
                    System.out.print(", ");
            }
            System.out.print("|");
            System.out.println();
        }
    }
    public static int[][] classicMultiply(int[][] a, int[][] b) {
        for (int i = 0; i < a.length; ++i)
            for (int j = 0; j < b[0].length; ++j)
                result[i][j] =calculateOneCell(a[i], b, j);
        return result;
    }
    public static int[][]  asyncMultiply(int[][] a, int[][] b) {
        ExecutorService es = Executors.newCachedThreadPool();
        for(int i = 0; i<a.length; ++i) {
            final int k = i;
            es.execute(() -> {
                for (int j = 0; j < b[0].length; ++j)
                    result[k][j] = calculateOneCell(a[k], b, j);
            });
        }
        es.shutdown();
        try {
            es.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;

    }

    public static int calculateOneCell(int[] a, int[][] b, int bColumn) {
        int result = 0;
        for (int i = 0; i<a.length; ++i) {
            result += (a[i] * b[i][bColumn]);
        }
        return result;
    }
}
