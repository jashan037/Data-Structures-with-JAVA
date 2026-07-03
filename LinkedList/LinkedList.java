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
        size++;
    }

    // modification
    public void add(T val) {
        if (Tail == null) {
            Head.val = val;
            Tail = Head;
            size++;
        } else {
            Tail.next = new Node(val);
            Tail = Tail.next;
            size++;
        }
    }

    public void insert(T val, int ind) {
        if (ind < 0 || ind > size)
            throw new IllegalArgumentException("Illegal Index " + ind);
        if (ind == 0) {
            Node nd = new Node(val);
            nd.next = Head.next;
            Head = nd;
        } else {
            Node itr = Head;
            while (ind != 1) {
                itr = itr.next;
                ind--;
            }
            Node sv = itr.next;
            itr.next = new Node(val);
            itr.next.next = sv;
        }
        size++;
    }

    public T pop() {
        T ret = Tail.val;
        Node itr = Head;
        while (itr.next != Tail)
            itr = itr.next;
        itr.next = null;
        Tail = itr;
        return ret;
    }

    // Prints
    public void print() {
        Node itr = Head;
        while (itr != null) {
            System.out.print(itr.val);
            if (itr.next != null)
                System.out.print(" ");
            else
                System.out.println();
            itr = itr.next;
        }
    }
}