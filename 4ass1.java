import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

public class Main extends JPanel {
        private final BST<Integer> tree;
        private final JTextField jtfKey = new JTextField(4);
        private final TreeView view = new TreeView();
        private final Button jbtInsert = new Button("Insert");
        private final Button jbtDelete = new Button("Delete");
        private final Button jbtShow1 = new Button("Show Inorder");
        private final Button jbtShow2 = new Button("Show Preorder");
        private final Button jbtShow3 = new Button("Show Postorder");

        public static void main(String[] args) {
                JFrame frame = new JFrame("Main");
                DisplayBST applet = new DisplayBST();
                frame.add(applet);
                frame.setSize(1000, 500);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
        }

        static class DisplayBST extends JApplet {
                public DisplayBST() {
                        add(new Main(new BST<>(new Integer[]{45, 1, 5, 7, 43, 452})));
                }
        }

        public Main(BST<Integer> tree) {
                this.tree = tree; // Set a binary tree to be displayed
                setUI();
        }

        private void setUI() {
                this.setLayout(new BorderLayout());
                add(view, BorderLayout.CENTER);
                JPanel panel = new JPanel();
                panel.add(new JLabel("Enter a key: "));
                panel.add(jtfKey);
                panel.add(jbtInsert);
                panel.add(jbtDelete);
                panel.add(jbtShow1);
                panel.add(jbtShow2);
                panel.add(jbtShow3);
                add(panel, BorderLayout.SOUTH);

                jbtInsert.addActionListener(e -> {
                        int key = Integer.parseInt(jtfKey.getText());
                        if (tree.search(key)) { // key is in the tree already
                                JOptionPane.showMessageDialog(null, key
                                        + " is already in the tree");
                        } else {
                                tree.insert(key); // Insert a new key
                                view.repaint(); // Redisplay the tree
                        }
                });
                
                jbtDelete.addActionListener(e -> { // Process the Delete button event
                        int key = Integer.parseInt(jtfKey.getText());
                        if (!tree.search(key)) { // key is not in the tree
                                JOptionPane.showMessageDialog(null, key + " is not in the tree");
                        } else {
                                tree.delete(key); // Delete a key
                                view.repaint(); // Redisplay the tree
                        }
                });

                jbtShow1.addActionListener(e -> {
                        ArrayList<Integer> list;
                        list = tree.inorderList();
                        StringBuilder showString = new StringBuilder("Inorder is ");
                        for (Integer integer : list) {
                                showString.append(integer).append(" ");
                        }
                        JOptionPane.showMessageDialog(null, showString.toString());
                });
                jbtShow2.addActionListener(e -> {
                        ArrayList<Integer> list;
                        list = tree.preorderList();
                        StringBuilder showString = new StringBuilder("Preorder is ");
                        for (Integer integer : list) {
                                showString.append(integer).append(" ");
                        }
                        JOptionPane.showMessageDialog(null, showString.toString());
                });

                jbtShow3.addActionListener(e -> {
                        ArrayList<Integer> list;
                        list = tree.postorderList();
                        StringBuilder showString = new StringBuilder("Postorder is ");
                        for (Integer integer : list) {
                                showString.append(integer).append(" ");
                        }
                        JOptionPane.showMessageDialog(null, showString.toString());
                });
        }
        
        class TreeView extends JPanel { // Inner class TreeView for displaying a tree on a panel
                private final int radius = 20; // Tree node radius
                private final int vGap = 50; // Gap between two levels in a tree

                @Override
                protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        if (tree.getRoot() != null) {
                                // Display tree recursively
                                displayTree(g, tree.getRoot(), getWidth() / 2, 30, getWidth() / 4);
                        }
                }

                private void displayTree(Graphics g, BST.TreeNode<Integer> root, int x,
                                         int y, int hGap) {
                        g.drawOval(x - radius, y - radius, 2 * radius, 2 * radius);
                        g.drawString(root.element + "", x - 6, y + 4);
                        if (root.left != null) {
                                // Draw a line to the left node
                                connectTwoCircles(g, x - hGap, y + vGap, x, y);
                                // Draw the left subtree recursively
                                displayTree(g, root.left, x - hGap, y + vGap, hGap / 2);
                        }

                        if (root.right != null) {
                                // Draw a line to the right node
                                connectTwoCircles(g, x + hGap, y + vGap, x, y);
                                // Draw the right subtree recursively
                                displayTree(g, root.right, x + hGap, y + vGap, hGap / 2);
                        }
                }

                private void connectTwoCircles(Graphics g, int x1, int y1, int x2, int y2) {
                        double d = Math.sqrt(vGap * vGap + (x2 - x1) * (x2 - x1));
                        int x11 = (int) (x1 - radius * (x1 - x2) / d);
                        int y11 = (int) (y1 - radius * (y1 - y2) / d);
                        int x21 = (int) (x2 + radius * (x1 - x2) / d);
                        int y21 = (int) (y2 + radius * (y1 - y2) / d);
                        g.drawLine(x11, y11, x21, y21);
                }
        }

        static class BST<E extends Comparable<E>> extends AbstractTree<E> {
                protected TreeNode<E> root;
                protected int size = 0;

                public BST(E[] objects) {
                        for (E object : objects) insert(object);
                }

                private boolean search(E e) {
                        TreeNode<E> current = root; // Start from the root
                        while (current != null) {
                                if (e.compareTo(current.element) < 0) {
                                        current = current.left;
                                } else if (e.compareTo(current.element) > 0) {
                                        current = current.right;
                                } else
                                        // element matches current.element
                                        return true; // Element is found
                        }

                        return false;
                }

                private void insert(E e) {
                        if (root == null)
                                root = createNewNode(e); // Create a new root
                        else {
                                // Locate the parent node
                                TreeNode<E> parent = null;
                                TreeNode<E> current = root;
                                while (current != null)
                                        if (e.compareTo(current.element) < 0) {
                                                parent = current;
                                                current = current.left;
                                        } else if (e.compareTo(current.element) > 0) {
                                                parent = current;
                                                current = current.right;
                                        } else
                                                return; // Duplicate node not inserted

                                // Create the new node and attach it to the parent node
                                if (e.compareTo(parent.element) < 0)
                                        parent.left = createNewNode(e);
                                else
                                        parent.right = createNewNode(e);
                        }
                        size++;
                }

                protected TreeNode<E> createNewNode(E e) {
                        return new TreeNode<>(e);
                }

                public ArrayList<E> inorderList() {
                        ArrayList<E> result = new ArrayList<>();
                        inorderList(result, root);
                        return result;
                }

                protected void inorderList(ArrayList<E> result, TreeNode<E> root) {
                        if (root == null)
                                return;
                        inorderList(result, root.left);
                        result.add(root.element);
                        inorderList(result, root.right);
                }


                public ArrayList<E> preorderList() {
                        ArrayList<E> result = new ArrayList<>();
                        preorderList(result, root);
                        return result;
                }

                protected void preorderList(ArrayList<E> result, TreeNode<E> root) {
                        if (root == null)
                                return;
                        result.add(root.element);
                        preorderList(result, root.left);
                        preorderList(result, root.right);
                }

                public ArrayList<E> postorderList() {
                        ArrayList<E> result = new ArrayList<>();
                        postorderList(result, root);
                        return result;
                }

                protected void postorderList(ArrayList<E> result, TreeNode<E> root) {
                        if (root == null)
                                return;
                        postorderList(result, root.left);
                        postorderList(result, root.right);
                        result.add(root.element);
                }

                public static class TreeNode<E extends Comparable<E>> {
                        protected E element;
                        protected TreeNode<E> left;
                        protected TreeNode<E> right;

                        public TreeNode(E e) {
                                element = e;
                        }
                }

                public TreeNode<E> getRoot() {
                        return root;
                }

                private void delete(E e) {
                        // Locate the node to be deleted and also locate its parent node
                        TreeNode<E> parent = null;
                        TreeNode<E> current = root;
                        while (current != null) {
                                if (e.compareTo(current.element) < 0) {
                                        parent = current;
                                        current = current.left;
                                } else if (e.compareTo(current.element) > 0) {
                                        parent = current;
                                        current = current.right;
                                } else
                                        break; // Element is in the tree pointed at by current
                        }

                        if (current == null)
                                return; // Element is not in the tree

                        // Case 1: current has no left children
                        if (current.left == null) {
                                // Connect the parent with the right child of the current node
                                if (parent == null) {
                                        root = current.right;
                                } else {
                                        if (e.compareTo(parent.element) < 0)
                                                parent.left = current.right;
                                        else
                                                parent.right = current.right;
                                }
                        } else {
                                TreeNode<E> parentOfRightMost = current;
                                TreeNode<E> rightMost = current.left;

                                while (rightMost.right != null) {
                                        parentOfRightMost = rightMost;
                                        rightMost = rightMost.right; // Keep going to the right
                                }

                                // Replace the element in current by the element in rightMost
                                current.element = rightMost.element;

                                // Eliminate rightmost node
                                if (parentOfRightMost.right == rightMost)
                                        parentOfRightMost.right = rightMost.left;
                                else
                                        // Special case: parentOfRightMost == current
                                        parentOfRightMost.left = rightMost.left;
                        }

                        size--;
                }

                @Override
                /* Obtain an iterator. Use inorder. */
                public java.util.Iterator<E> iterator() {
                        return new InorderIterator();
                }

                // Inner class InorderIterator
                private class InorderIterator implements java.util.Iterator<E> {
                        // Store the elements in a list
                        private final java.util.ArrayList<E> list = new java.util.ArrayList<>();
                        private int current = 0; // Point to the current element in list

                        public InorderIterator() {
                                inorder(); // Traverse binary tree and store elements in list
                        }

                        private void inorder() {
                                inorder(root);
                        }

                        private void inorder(TreeNode<E> root) {
                                if (root == null)
                                        return;
                                inorder(root.left);
                                list.add(root.element);
                                inorder(root.right);
                        }

                        @Override
                        /* More elements for traversing? */
                        public boolean hasNext() {
                                return current < list.size();
                        }

                        @Override
                        /* Get the current element and move to the next */
                        public E next() {
                                return list.get(current++);
                        }

                        @Override
                        /* Remove the current element */
                        public void remove() {
                                delete(list.get(current)); // Delete the current element
                                list.clear(); // Clear the list
                                inorder(); // Rebuild the list
                        }
                }

        }

        static abstract class AbstractTree<E> implements Tree<E> {
                @Override
                /* Return an iterator for the tree */
                public java.util.Iterator<E> iterator() {
                        return null;
                }
        }

        interface Tree<E> extends Iterable<E> {


                java.util.Iterator<E> iterator();
        }
}
