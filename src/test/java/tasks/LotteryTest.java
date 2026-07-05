package tasks;

import com.github.javafaker.Faker;

import java.util.Locale;
import java.util.Scanner;

public class LotteryTest {
    //TODO
/*Задание 6. Лотерея
Пользователь вводит свое имя через Scanner.
Faker генерирует случайное имя победителя.
Программа сравнивает:
Ваше имя: Анна
Победитель: Мария

Вы не выиграли
        или
Поздравляем! Вы выиграли!*/
    private static final Faker faker = new Faker(Locale.of("ru"));

     public static void main(String[] args) {
         Scanner sc = new Scanner(System.in);

         while (true) {
             System.out.print("Введите ваше имя (или 'Стоп' для выхода): ");
             String inputName = sc.nextLine();

             if (inputName.equalsIgnoreCase("Стоп")) {
                 System.out.println("Выход из программы.");
                 break;
             }

             String winnerName = faker.name().firstName();
             System.out.println("\n--- РЕЗУЛЬТАТ ЛОТЕРЕИ ---" +
                     "\nВаше имя: " + inputName +
                     "\nПобедитель: " + winnerName);

             if (inputName.equalsIgnoreCase(winnerName)) {
                 System.out.println("Поздравляем! Вы выиграли!");
             } else {
                 System.out.println("Вы не выиграли");
             }
         }
         sc.close();
    }
}
