import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class SearchOfMin {
    public static void main(String[] args) {
        float[] array = fillArray(10_000, -100, 100);

        for (int i = 0; i < 3; i++) {
            getTimes(i, array);
        }
    }

    private static float[] fillArray(int size, int min, int max) {
        // Заполняет массив случайными значениями из диапазона
        float[] randomArray = new float[size];
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            randomArray[i] = random.nextFloat(min, max); // Генерация случайного числа
        }
        return randomArray;
    }

    public static void getTimes(int variant, float[] array) {
        switch (variant) {
            case 0:
                // Вариант 0: Последовательно
                long startTime0 = System.currentTimeMillis();
                float min0 = sequentialMin(array);
                long endTime0 = System.currentTimeMillis();
                System.out.println("Последовательно:");
                System.out.println("\tМинимум: " + min0);
                System.out.println("\tВремя выполнения (мс): " + (endTime0 - startTime0));
                break;
            case 1:
                // Вариант 1: Многопоточность
                long startTime1 = System.currentTimeMillis();
                float min1 = parallelMin(array);
                long endTime1 = System.currentTimeMillis();
                System.out.println("Многопоточно:");
                System.out.println("\tМинимум: " + min1);
                System.out.println("\tВремя выполнения (мс): " + (endTime1 - startTime1));
                break;
            case 2:
                // Вариант 2: ForkJoin
                long startTime2 = System.currentTimeMillis();
                float min2 = forkJoinMin(array);
                long endTime2 = System.currentTimeMillis();
                System.out.println("ForkJoin:");
                System.out.println("\tМинимум: " + min2);
                System.out.println("\tВремя выполнения (мс): " + (endTime2 - startTime2));
                break;
        }
        return;
    }

    public static float sequentialMin(float[] array) {
        // Последовательно
        float min = array[0];
        for (float num : array) {
            if (num < min) {
                min = num;
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return min;
    }

    public static float parallelMin(float[] array) {
        // Параллельно
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<Float>> futures = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            final int start = i * (array.length / numThreads);
            final int end = (i == numThreads - 1) ? array.length : (i + 1) * (array.length / numThreads);
            futures.add(executor.submit(() -> {
                float localMin = array[start];
                for (int j = start; j < end; j++) {
                    if (localMin > array[j]) {
                        localMin = array[j];
                    }
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return localMin;
            }));
        }

        float min = array[0];
        for (Future<Float> future : futures) {
            try {
                min = future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        return min;
    }

    public static float forkJoinMin(float[] array) {
        // ForkJoin
        ForkJoinPool pool = new ForkJoinPool();
        return pool.invoke(new minTask(array, 0, array.length));
    }

    static class minTask extends RecursiveTask<Float> {
        private final float[] array;
        private final int start;
        private final int end;

        minTask(float[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Float compute() {
            if (end - start <= 1000) {
                float min = this.array[this.start];
                for (int i = start; i < end; i++) {
                    if (min > array[i]){
                        min = array[i];
                    }
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return min;
            } else {
                int mid = (start + end) / 2;
                minTask leftTask = new minTask(array, start, mid);
                minTask rightTask = new minTask(array, mid, end);

                leftTask.fork();
                float rightMin = rightTask.compute();
                float leftMin = leftTask.join();

                return Math.min(rightMin, leftMin);
            }
        }
    }
}
