package algorithmbase.datatype;

public class HashMap_01_base<K, V> {

    private Entry<K, V>[] datas;

    private double dilatationRatio = 0.75;

    private int initCapacity = 1 << 3;

    private int size;

    public HashMap_01_base() {
        datas = new Entry[initCapacity];
    }

    public V get(K k) {
        int hash = hash(k);
        Entry<K, V> head = datas[hash % datas.length];
        if (head == null) {
            return null;
        } else if (head.k.equals(k)) {
            return head.val;
        } else {
            Entry<K, V> pre = head, next;
            while (pre != null) {
                next = pre.next;
                if (next == null) {
                    break;
                }
                if (next.k.equals(k)) {
                    return next.val;
                }
                pre = next;
            }
        }
        return null;
    }

    public V put(K k, V v) {
        int hash = hash(k);
        Entry<K, V> oldEntry = null;
        Entry<K, V> head = datas[hash % datas.length];
        if (head == null) {
            datas[hash % datas.length] = new Entry<>(k, v, null);
        } else if (head.k.equals(k)) {
            datas[hash % datas.length] = new Entry<>(k, v, head.next);
        } else {
            Entry<K, V> pre = head, next;
            while (pre != null) {
                next = pre.next;
                if (next == null) {
                    break;
                }
                if (next.k.equals(k)) {
                    oldEntry = next;
                    pre.next = new Entry<>(k, v, next.next);
                    return oldEntry.val;
                }
                pre = next;
            }
            pre.next = new Entry<>(k, v, null);
        }
        size++;
        ensureCapacity();
        return null;
    }

    public V remove(K k) {
        int hash = hash(k);
        Entry<K, V> head = datas[hash % datas.length];
        if (head == null) {
            return null;
        } else if (head.k.equals(k)) {
            datas[hash % datas.length] = head.next;
            size--;
            return head.val;
        } else {
            Entry<K, V> pre = head, next;
            while (pre != null) {
                next = pre.next;
                if (next == null) {
                    break;
                }
                if (next.k.equals(k)) {
                    pre.next = next.next;
                    size--;
                    return next.val;
                }
                pre = next;
            }
        }
        return null;
    }


    /**
     * 确保容量
     */
    private void ensureCapacity() {
        if ((double) size / (double) datas.length > dilatationRatio) {
            dilatation();
        }
    }

    /**
     * 扩容
     */
    private void dilatation() {
        Entry<K, V>[] newDatas = new Entry[datas.length << 1];
        for (Entry<K, V> head : datas) {
            while (head != null) {
                Entry<K, V> next = head.next;
                putNewEntry(newDatas, head);
                head = next;
            }
        }
        datas = newDatas;
    }

    private void putNewEntry(Entry<K, V>[] newDatas, Entry<K, V> entry) {
        K k = entry.k;
        V v = entry.val;

        int hash = hash(k);
        Entry<K, V> head = newDatas[hash % newDatas.length];
        if (head == null) {
            newDatas[hash % newDatas.length] = entry;
        } else {
            // 头插法
            entry.next = head;
            newDatas[hash % newDatas.length] = entry;

            // 尾插法
//            Entry<K, V> pre = head, next = pre.next;
//            while (next != null) {
//                pre = next;
//                next = pre.next;
//            }
//            pre.next = entry;
        }
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
