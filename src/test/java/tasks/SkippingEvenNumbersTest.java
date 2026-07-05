package tasks;

import org.testng.annotations.Test;

public class SkippingEvenNumbersTest {
// TODO Задание 5.
/*Пропуск четных
Вывести только нечетные числа от 1 до 100 через continue.
 */
    public void oddNumbers() {
        int i;
        for (i = 1; i <= 100; i++) {
            if (i % 2 == 0) {
                continue;
            }
            System.out.println(i);
        }
    }

    @Test
    public void printOddNumbersTest() {
        oddNumbers();
    }
}
