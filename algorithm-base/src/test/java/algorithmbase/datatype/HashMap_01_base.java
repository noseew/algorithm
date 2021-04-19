package algorithmbase.datatype;

public class HashMap_01_base<K, V> {

    private Entry<K, V>[] datas;

    private double dilatationRatio = 0.75;

    private int initCapacity = 1 << 4;

    private int size;

    public HashMap_01_base() {
        datas = new Entry[initCapacity];
    }

    public V puv(K k, V v) {
        int hash = hash(k);
        Entry<K, V> head = datas[hash % datas.length];
        if (head == null) {
            datas[hash % datas.length] = new Entry<>(k, v, null);
        } else if (head.k.equals(k)) {
            datas[hash % datas.length] = new Entry<>(k, v, head.next);
        } else {
            Entry<K, V> pre = head, next;
            do {
//                next = pre.next;
//                if (next == null) {
//
//                }

            } while ((pre = pre.next) != null);
        }
        size++;
        return null;
    }

    public int hash(K k) {
        if (k == null) {
            return 0;
        }
        return System.identityHashCode(k);
    }

    class Entry<K, V> {

        K k;
        V val;
        Entry<K, V> next;

        public Entry(K k, V val, Entry<K, V> next) {
            this.k = k;
            this.val = val;
            this.next = next;
        }
    }
}
