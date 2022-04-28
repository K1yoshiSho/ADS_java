import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;
public class Main {

    public static void main(String[] args) throws FileNotFoundException, URISyntaxException {
        String fileTxt = "Biology.txt"; // fileName
        Main main = new Main();
        File file = new File("."); // Our file
        file = main.getFileFromResources(fileTxt); // Вызываем наш метод getFileFromResources
        ArrayList<String> outputWords = new ArrayList<>(); // Создаем список
        String current, next, split;
        Scanner fileInput = new Scanner(file);
        while (fileInput.hasNext()) { // Метод hasNext () - это метод из интерфейса итератора,который возвращает true, если в коллекции имеется следующий элемент, иначе возвращает false и больше не будет входить в тело цикла while
            current = fileInput.next();
            next = fileInput.next();
            split = current + " " + next;
            outputWords.add(split); // Добавляем в наш список
        }
        java.util.Collections.sort(outputWords); // Сортировка списка в алфавитном порядке
        System.out.println(outputWords); // Выводим наш список
    }

    private File getFileFromResources(String fileTxt) throws URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader(); // Класс, ответственный за загрузку типов. По имени он находит и загружает в память данные, которые составляют определение типа.
        var resourceFile = classLoader.getResource(fileTxt); // getResource метод находит ресурс с указанным именем переменной. В нашем случае это fileName
        if (resourceFile == null) { // Если нет файла с таким именем, то
            System.out.println("File Not Found");
        }
        return new File(resourceFile.toURI()); // Если есть такой файл, то идет конвертация из этого объекта URL в объект URI
    }
}
