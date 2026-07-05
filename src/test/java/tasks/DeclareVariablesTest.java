package tasks;

import org.testng.annotations.Test;

//Задание 1
public class DeclareVariablesTest {
    /*
           Необходимо объявить и инициализировать глобальные переменные, указанные в тексте response,
           чтобы получить следующий текст:
           Dear Sasha, thank you for supporting our business!
           Cups of coffee bought: 1.
           Cost of 1 cup of coffee: $99.9.
           Your total is: $99.9.
           Tip is included? true.

           P.S.: не забудьте про перенос строки, все поля должны иметь модификатор доступа public
     */
    public String customerName = "Sasha";
    public int cupsOfCoffee = 10;
    public double costOfCoffee = 99.9;
    public boolean tip = true;
    public double totalCostOfCoffee = costOfCoffee * cupsOfCoffee;

    public String declareVariables() {
        String response;

        response = "Dear " + customerName + ", thank you for supporting our business!\n"
                + "Cups of coffee bought: " + cupsOfCoffee + ".\nCost of 1 cup of coffee: $"
                + costOfCoffee + ".\nYour total is: $" + totalCostOfCoffee + ".\nTip is included? " + tip + ".";

        return response;
    }

    @Test
    public void test() {
        System.out.println(declareVariables());
    }
}
