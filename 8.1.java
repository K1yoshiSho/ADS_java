class Main {
        public static void main(String[] args) {
                int[] keys = { 25, 30, 35, 40, 50, 45, 60 };
                Node root = null;
                for (int i = 0; i < keys.length; i++) {
                        int key = keys[i];
                        root = insertKey(root, key); // вставляем
                }

                root = deleteNode(root, 50); // удаляемый
                root = deleteNode(root, 40); // удаляемый
                root = deleteNode(root, 30); // удаляемый
                root = deleteNode(root, 35); // удаляемый

                inOrder(root);
        }

        public static void inOrder(Node root) { // (Left, Root, Right)
                if (root == null) return;
                inOrder(root.left);
                System.out.print(root.dataRoot + " ");
                inOrder(root.right);
        }

        // Вспомогательная функция для поиска root с минимальным значением в поддереве
        public static Node getMinKey(Node currentEl) {
                while (currentEl.left != null) {
                        currentEl = currentEl.left;
                }
                return currentEl;
        }

        public static Node insertKey(Node root, int key) {

                if (root == null) return new Node(key); // если корень равен null, создаем новый узел и возвращаем его

                if (key < root.dataRoot) root.left = insertKey(root.left, key); // если заданный ключ меньше, чем корневой узел, повторяем для левого поддерева

                else root.right = insertKey(root.right, key); // если заданный ключ больше, чем корневой узел, повторно ищем нужное поддерево

                return root;
        }

        public static Node deleteNode(Node root, int key) {

                Node parent = null, //  Хранение родителя текущего root
                        currRoot = root; // Начинаем с текущего root

                while (currRoot != null && currRoot.dataRoot != key) { // поиск ключа в BST и установка его родительского root
                        parent = currRoot; // обновляем родительский узел до текущего узла
                        if (key < currRoot.dataRoot) currRoot = currRoot.left; // если заданный ключ меньше, чем текущий узел, перейдите к левому поддереву;
                        else currRoot = currRoot.right;
                }

                if (currRoot == null) return root; // возврат, если ключ не найден в дереве

                // 1: удаляемый узел не имеет дочерних узлов
                if (currRoot.left == null && currRoot.right == null) {
                        // если удаляемый узел не является корневым узлом, то делаем его родительский левый/правый дочерний узел null
                        if (currRoot != root) {
                                if (parent.left == currRoot) parent.left = null;
                                else parent.right = null;

                        }
                        else root = null; // если у дерева есть только корневой узел, делаем его null

                }

                // 2: удаляемый узел имеет два дочерних узла
                else if (currRoot.left != null && currRoot.right != null) {
                        Node successor = getMinKey(currRoot.right); // для нахождения его последовательного узела-преемника
                        int val = successor.dataRoot; // хранить значение преемника
                        // рекурсивно удалить преемника. Преемник будет иметь не более одного ребенка (правого ребенка).
                        deleteNode(root, successor.dataRoot);
                        currRoot.dataRoot = val; // копируем значение преемника в текущий узел
                }

                // 3: удаляемый узел имеет только одного ребенка
                else {
                        // выбрать дочерний узел
                        Node child = (currRoot.left != null)? currRoot.left: currRoot.right;

                        // если удаляемый узел не является корневым узлом, установим его родителя на его дочерний узел
                        if (currRoot != root) {
                                if (currRoot == parent.left) parent.left = child;
                                else parent.right = child;
                        }

                        else root = child; // если удаляемый узел является корневым узлом, то установим корень в дочерний узел
                }
                return root;
        }
}

class Node {
        int dataRoot;
        Node left = null,
                right = null;
        Node(int dataRoot) {
                this.dataRoot = dataRoot;
        }
}
