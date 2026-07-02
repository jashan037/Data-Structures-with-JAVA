package DynamicArray;

public class DynArray<T> {
    private int cap = 2;
    private int len = 0;
    Object[] arr;

    public DynArray() {
        arr = new Object[cap];
    }

    public DynArray(int n) {
        cap = n;
        arr = new Object[cap];
    }

    private void doble() {
        Object[] narr = new Object[cap * 2];
        for (int i = 0; i < len; i++) {
            narr[i] = arr[i];
        }
        arr = narr;
        cap *= 2;
    }

    public void print() {
        for (int i = 0; i < len; i++) {
            if (i != len - 1)
                System.out.print(arr[i] + " ");
            else
                System.out.print(arr[i] + "\n");
        }
    }

    public void add(T val) {
        if (len == cap)
            doble();
        arr[len++] = val;
    }
}