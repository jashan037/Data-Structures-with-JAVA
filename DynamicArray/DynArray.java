package DynamicArray;

public class DynArray<T> {
    private int cap = 2;
    private int len = 0;
    Object[] arr;

    // Constructors
    public DynArray() {
        arr = new Object[cap];
    }

    public DynArray(int n) {
        if (n < 0)
            throw new IllegalArgumentException("Illegal Capacity " + n);
        cap = n;
        arr = new Object[cap];
    }

    // Cap and Size
    public int getCap() {
        return cap;
    }

    public int size() {
        return len;
    }

    public void fit() {
        if (len <= 0)
            len = 2;
        Object[] narr = new Object[len];
        for (int i = 0; i < len; i++) {
            narr[i] = arr[i];
        }
        arr = narr;
        cap = len;
    }

    // Internal Modifications
    private void doble() {
        Object[] narr = new Object[cap * 2];
        for (int i = 0; i < len; i++) {
            narr[i] = arr[i];
        }
        arr = narr;
        cap *= 2;
    }

    // Print
    public void print() {
        for (int i = 0; i < len; i++) {
            if (i != len - 1)
                System.out.print(arr[i] + " ");
            else
                System.out.print(arr[i] + "\n");
        }
    }

    // Modifications
    public void add(T val) {
        if (len == cap)
            doble();
        arr[len++] = val;
    }

    @SuppressWarnings("unchecked")
    public void add(T... val) {
        while (len + val.length - 1 >= cap)
            doble();
        for (T i : val)
            arr[len++] = i;
    }

    @SuppressWarnings("unchecked")
    public T pop() {
        return (T) arr[len--];
    }

    public void clear() {
        len = 2;
        fit();
        len = 0;
    }

    // Getters
    @SuppressWarnings("unchecked")
    public T get(int n) {
        if (n < 0 || n >= len)
            throw new IllegalArgumentException("Illegal Index " + n);
        return (T) arr[n];
    }

    public int indexOf(Object o) {
        for (int i = 0; i < len; i++) {
            if (arr[i].equals(o))
                return i;
        }
        return -1;
    }

    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    public boolean isEmpty() {
        return len == 0;
    }

}