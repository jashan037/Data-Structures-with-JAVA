package LinkedList;

public class DoublyLinkedList<T> {
    private Node Head;
    private Node Tail;
    private int size = 0;

    private class Node {
        public T val;
        public Node prev;
        public Node next;

        public Node() {
            prev = null;
            next = null;
        }

        public Node(T val) {
            this.val = val;
            prev = null;
            next = null;
        }
    }

    public DoublyLinkedList() {
        Head = new Node();
        Tail = null;
    }

    public DoublyLinkedList(T val) {
        Head = new Node(val);
        Tail = null;
        size++;
    }

    // Modifications
    public void add(T val) {
        if (Tail == null) {
            Tail = new Node(val);
            Head = Tail;
        } else {
            Tail.next = new Node(val);
            Tail.next.prev = Tail;
            Tail = Tail.next;
        }
        size++;
    }

    public void insert(T val, int ind) {
        if (ind < 0 || ind > size)
            throw new IllegalArgumentException("Illegal Index " + ind);
        if (ind == 0) {
            Node nd = new Node(val);
            nd.next = Head;
            Head.prev = nd;
            Head = nd;
        } else {
            Node itr = Head;
            while (ind-- != 1)
                itr = itr.next;
            Node sv = itr.next;
            itr.next = new Node(val);
            itr.next.prev = itr;
            itr.next.next = sv;
            if (sv != null)
                sv.prev = itr.next;
        }
        size++;
    }

    public T remove() {
        if (Tail == null || Head == Tail) {
            clear();
            return null;
        }
        T ret = Tail.val;
        Node nt = Tail.prev;
        nt.next = null;
        Tail = nt;
        return ret;
    }

    public T remove(int ind) {
        if (ind < 0 || ind >= size)
            throw new IllegalArgumentException("Illegal Index " + ind);
        T ret = null;
        if (ind == 0) {
            ret = Head.val;
            if (Head.next != null)
                Head.next.prev = null;
            Head = Head.next;
        } else {
            Node itr = Head;
            while (ind-- != 1)
                itr = itr.next;
            ret = itr.next.val;
            if (itr.next.next != null)
                itr.next.next.prev = itr;
            itr.next = itr.next.next;
        }
        return ret;
    }

    public void reverse() {
        Node prev = null;
        Node nd = Head;
        Node nex;
        while (nd != null) {
            nex = nd.next;
            nd.prev = nex;
            nd.next = prev;
            prev = nd;
            nd = nex;
        }
        Head = prev;
    }

    public void clear() {
        Head = new Node();
        Tail = null;
    }

    // getters
    public int length() {
        return size;
    }

    // print
    public void print() {
        Node itr = Head;
        while (itr != null) {
            System.out.print(itr.val);
            if (itr.next != null)
                System.out.print("<->");
            itr = itr.next;
        }
        System.out.println();
    }

}