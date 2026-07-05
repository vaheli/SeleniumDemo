package tasks;

import org.testng.annotations.Test;

//Задание 3
public class VariablePositionTest {
    /*
       Скомпилируется ли данный код?
       Если нет - сделайте правки
       Ответ да
       */
    public int replacement() {

        int a = 1;
        int b = a;

        return b;
    }

    @Test
    public void test() {
        System.out.println(replacement());
    }
}