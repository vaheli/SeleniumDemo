package tasks.employees;

import org.testng.annotations.Test;

public class EmployeesTest {
    String femaleName = "Наталья";
    String maleName = "Александр";
    String femaleRole = "заместитель директора по маркетингу";
    String maleRole = "Директор по маркетингу";
    Female female;
    Male male;

    public EmployeesTest() {
        this.female = new Female();
        this.male = new Male();
    }

    public String employeesToPrint() {
        return "Вчера наша компания пополнилась новыми сотрудниками. " + male.getName(maleName) + " нанят на" +
                "должность " + male.getRole(maleRole) + ", а " + female.getName(femaleName) + " нанята в должности: " +
                female.getRole(femaleRole) + ".";
    }

    @Test
    public void employeesToPrintTest() {
        System.out.println(employeesToPrint());
    }

    //TODO Задание 4
 /*Есть класс Employees, объявите в нем строковые переменные (name, role). Создайте 2 новых объекта
  класса (male, female), присвойте им имена (Александр, Наталья), Назначьте им должности
  соответственно (директор по маркетингу, заместитель директора по маркетингу) и верните строку,
  используя Переменные: Вчера наша компания пополнилась новыми сотрудниками. Александр нанят на
  должность Директора по маркетингу, а Наталья нанята в должности: заместитель директора по маркетингу.
  */
}
