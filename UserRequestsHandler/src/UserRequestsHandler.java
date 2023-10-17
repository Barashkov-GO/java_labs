import java.util.Scanner;
import java.util.concurrent.*;

public class UserRequestsHandler {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        while (true) {
            System.out.print("Введите число (или 'q' для выхода): ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();

            if (input.equals("q")) {
                break;
            }

            try {
                int number = Integer.parseInt(input);
                Future<Integer> result = executorService.submit(new SquareTask(number));

                try {
                    int squaredNumber = result.get();
                    System.out.println("Результат: " + squaredNumber);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            } catch (NumberFormatException e) {
                System.out.println("Неверный ввод. Пожалуйста, введите целое число.");
            }
        }

        executorService.shutdown();
    }

    static class SquareTask implements Callable<Integer> {
        private final int number;

        public SquareTask(int number) {
            this.number = number;
        }

        @Override
        public Integer call() {
            try {
                // Имитация обработки запроса с задержкой от 1 до 5 секунд
                int delay = ThreadLocalRandom.current().nextInt(1000, 5000);
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return number * number;
        }
    }
}
