import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
        static String fileName = "Monday.txt"; // читаем файл в строку с помощью класса Files
        public static void main(String[] args) throws Exception {

                String fileContent = readUsingFiles(fileName);
                System.out.println("\nThe original string is: " + fileContent);
                byte[] bytes = fileContent.getBytes(StandardCharsets.US_ASCII);
                List bytesList = new ArrayList<>();   // Convert bytes to ASCII
                for (byte aByte : bytes) {
                        int ASCII = aByte;
                        bytesList.add(ASCII);
                }

                System.out.println("ASCII is: " + bytesList.toString());
                System.out.println("\nThe Huffman code is stored in " + "'" + fileName + ".counts'");
                FileOutputStream out = new FileOutputStream("Monday.txt.counts");
                ObjectOutputStream oos = new ObjectOutputStream (out);
                oos.writeObject(bytesList.toString());
                oos.close();

                FileInputStream fin = new FileInputStream("Monday.txt.counts");
                ObjectInputStream oit = new ObjectInputStream(fin);
                System.out.print("Content from " + "'" + fileName + ".counts': " + oit.readObject());
                oit.close();
                buildHuffmanTree(fileContent);

        }

        private static String readUsingFiles(String fileName) throws IOException { // Читаем файл в строку с помощью класса Files
                return new String(Files.readAllBytes(Paths.get(fileName)));
        }

        public static void encode(Node root, String str,
                                  Map<Character, String> huffmanCode) { // Обход дерева Хаффмана и хранение кодов Хаффмана в карте.
                if (root == null) return;

                if (isLeaf(root)) // Находим узел листа
                        huffmanCode.put(root.ch, str.length() > 0 ? str : "1");

                encode(root.left, str + '0', huffmanCode);
                encode(root.right, str + '1', huffmanCode);
        }

        public static int decode(Node root, int index, StringBuilder sb) { // Обход дерева Хаффмана и декодирование закодированной строки
                if (root == null) return index;

                if (isLeaf(root)) { // Находим узел листа
                        System.out.print(root.ch);
                        return index;
                }
                index++;
                root = (sb.charAt(index) == '0') ? root.left : root.right;
                index = decode(root, index, sb);
                return index;
        }

        public static boolean isLeaf(Node root) { // Функция для проверки, содержит ли дерево Хаффмана только один узел
                return root.left == null && root.right == null;
        }

        public static void buildHuffmanTree(String text) throws IOException, ClassNotFoundException { // Строит дерево Хаффмана и декодирует заданный входной текст
                if (text == null || text.length() == 0) return; // Базовый случай: пустая строка

                Map<Character, Integer> freq = new HashMap<>(); // Считаем частоту появления каждого символа и сохраняем их в Map
                for (char symbol: text.toCharArray()) {
                        freq.put(symbol, freq.getOrDefault(symbol, 0) + 1);
                }

                // Cоздаем приоритетную очередь для хранения живых узлов дерева Хаффмана.
                // Элемент с наивысшим приоритетом имеет самую низкую частоту

                PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(l -> l.freq));

                // Создаем листовой узел для каждого символа и добавляем его в очередь приоритетов.

                freq.forEach((key, value) -> pq.add(new Node(key, value)));

                while (pq.size() != 1) { // Пока в очереди не будет более одного узла удаляем два узла с наивысшим приоритетом из очереди

                        Node left = pq.poll();
                        Node right = pq.poll();

                        int sum = left.freq + right.freq;
                        // Создаем новый внутренний узел с этими двумя узлами в качестве children и с частотой, равной сумме частот обоих узлов.
                        pq.add(new Node(null, sum, left, right)); // Добавляем новый узел в очередь приоритетов.
                }

                Node root = pq.peek(); // `root` хранит указатель на корень дерева Хаффмана

                Map<Character, String> huffmanCode = new HashMap<>(); // Обход дерева Хаффмана и хранение кодов Хаффмана в Map
                encode(root, "", huffmanCode);

                // Print encoded string
                StringBuilder sb = new StringBuilder();
                char[] charArray = text.toCharArray();
                for (int i = 0; i < charArray.length; i++) {
                        char c = charArray[i];
                        sb.append(huffmanCode.get(c));
                }

                System.out.println("\n\nThe encoded file is stored in " + "'" + fileName + ".new'");
                System.out.println("Huffman Codes are: " + huffmanCode); // Выводим коды Хаффмана
                FileOutputStream out = new FileOutputStream("Monday.txt.new");
                ObjectOutputStream oos = new ObjectOutputStream (out);
                oos.writeObject(sb);

                FileInputStream fin = new FileInputStream("Monday.txt.new");
                ObjectInputStream oit = new ObjectInputStream(fin);
                System.out.print("Content from " + "'" + fileName + ".new': " + oit.readObject());
                oit.close();

                System.out.print("\n\nThe decoded string is: ");

                if (isLeaf(root)) { // Особый случай: Для ввода типа a, aa, aaa и т.д.
                        while (root.freq-- > 0) {
                                System.out.print(root.ch);
                        }
                } else {
                        // Снова пройдемся по дереву Хаффмана и на этот раз, декодируем закодированную строку
                        int index = -1;
                        while (index < sb.length() - 1) {
                                index = decode(root, index, sb);
                        }
                }
        }
}

class Node { // Узел дерева
        Character ch;
        Integer freq;
        Node left = null, right = null;

        Node(Character ch, Integer freq) {
                this.ch = ch;
                this.freq = freq;
        }

        public Node(Character ch, Integer freq, Node left, Node right) {
                this.ch = ch;
                this.freq = freq;
                this.left = left;
                this.right = right;
        }
}
