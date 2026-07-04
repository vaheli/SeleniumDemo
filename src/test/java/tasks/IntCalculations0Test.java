package tasks;

import org.testng.annotations.Test;

//Задание 2
public class IntCalculations0Test {
    /*
      Исправить знак вопроса на знак арифметической операции, чтобы метод возвращал значение 1
     */
    public static double getDataTypesIntA() {
        int a;
        a = 5 % 2;
        return a;
    }
    /*
      Исправить знак вопроса на знак арифметической операции, чтобы метод возвращал значение 2
     */
    public static double getDataTypesIntB() {
        int b;
        b = 5 / 2;
        return b;
    }

//Исправить знак вопроса на знак арифметической операции, чтобы метод возвращал значение 0.5
    public static double getDataTypesDoubleA() {
        double a;
        a = 5.0 % 1.5;
        return a;
    }

    @Test
    public static void test() {
        System.out.println(getDataTypesIntA());
        System.out.println(getDataTypesIntB());
        System.out.println(getDataTypesDoubleA());
    }
}