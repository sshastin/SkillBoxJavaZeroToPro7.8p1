import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

public class Main {
    private static String staffFile = "staff.txt";
    private static String dateFormat = "dd.MM.yyyy";

    public static void main(String[] args) throws ParseException {
        ArrayList<Employee> staff = loadStaffFromFile();
        Stream<Employee> employeeStream = staff.stream();
        employeeStream.forEach(System.out::println);
        employeeStream.map(e -> e.getSalary()).filter(s -> s >= 100_000).reduce((s1, s2) -> s1 + s2).ifPresent(System.out::println);
        /**
         * Задаем границы даты
         */

        String stringDateAfter = "01.01.2017";
        String stringDateBefore = "01.01.2018";
        String pattern = "dd.MM.yyyy";
        /**
         * Парсим даты из String в Date
         */

        Date dateAfter = new SimpleDateFormat(pattern).parse(stringDateAfter);
        Date dateBefore = new SimpleDateFormat(pattern).parse(stringDateBefore);


        /**
         * Пояснение больше для себя:
         * Сперва фильтруем работников из потока по дате начала работы после определенной даты(01.01.2017),
         * затем по дате начала работы до определенной даты(01.01.2018), далее находим в отфильтрованном потоке максимальное значение
         * по ЗП сотрудника через референс метод класса, выводим на экран опять же через референс метод класса.
         */

        employeeStream.filter(e -> e.getWorkStart().after(dateAfter))
                .filter(e -> e.getWorkStart().before(dateBefore))
                .max(Comparator.comparing(Employee::getSalary))
                .ifPresent(System.out::println);
    }

    private static ArrayList<Employee> loadStaffFromFile() {
        ArrayList<Employee> staff = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(staffFile));
            for (String line : lines) {
                String[] fragments = line.split("\t");
                if (fragments.length != 3) {
                    System.out.println("Wrong line: " + line);
                    continue;
                }
                staff.add(new Employee(
                        fragments[0],
                        Integer.parseInt(fragments[1]),
                        (new SimpleDateFormat(dateFormat)).parse(fragments[2])
                ));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return staff;
    }
}