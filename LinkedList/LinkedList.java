package LinkedList;

public class LinkedList<T> {
    private Node Head;
    private Node Tail;
    private int size = 0;

    // Node Class
    private class Node {
        public T val;
        public Node next;

        public Node() {
        }

        public Node(T val) {
            this.val = val;
        }
    }

    // constructors
    public LinkedList() {
        Head = new Node();
        Tail = null;
    }

    public LinkedList(T val) {
        Head = new Node(val);
        Tail = Head;
    }

    // modification
    public void add(T val) {
        if (Tail == null)
            Tail = new Node(val);
        else {
            Tail.next = new Node(val);
            Tail = Tail.next;
        }
    }

}