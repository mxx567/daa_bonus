package datastructures;

import java.util.Objects;

public class HashTable<K, V> {

    private static class Entry<K, V> {
        final K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private Entry<K, V>[] buckets;
    private int size;
    private static final double LOAD_FACTOR = 0.75;

    @SuppressWarnings("unchecked")
    public HashTable() {
        buckets = (Entry<K, V>[]) new Entry<?, ?>[16];
    }

    private int hash(K key) {
        int h = Objects.hashCode(key);

        // Mix higher bits into lower bits.
        return h ^ (h >>> 16);
    }

    private int index(K key) {
        return Math.floorMod(hash(key), buckets.length);
    }

    public void put(K key, V value) {
        int index = index(key);

        for (Entry<K, V> current = buckets[index];
             current != null;
             current = current.next) {

            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
        }

        buckets[index] = new Entry<>(key, value, buckets[index]);
        size++;

        if (size > buckets.length * LOAD_FACTOR) {
            resize();
        }
    }

    public V get(K key) {
        Entry<K, V> entry = findEntry(key);
        return entry == null ? null : entry.value;
    }

    public boolean containsKey(K key) {
        return findEntry(key) != null;
    }

    private Entry<K, V> findEntry(K key) {
        int index = index(key);
        Entry<K, V> current = buckets[index];

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current;
            }

            current = current.next;
        }

        return null;
    }

    public V remove(K key) {
        int index = index(key);
        Entry<K, V> current = buckets[index];
        Entry<K, V> previous = null;

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                if (previous == null) {
                    buckets[index] = current.next;
                } else {
                    previous.next = current.next;
                }

                size--;
                return current.value;
            }

            previous = current;
            current = current.next;
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Entry<K, V>[] oldBuckets = buckets;
        buckets = (Entry<K, V>[]) new Entry<?, ?>[oldBuckets.length * 2];

        for (Entry<K, V> head : oldBuckets) {
            Entry<K, V> current = head;

            while (current != null) {

                Entry<K, V> next = current.next;

                int newIndex = index(current.key);
                current.next = buckets[newIndex];
                buckets[newIndex] = current;

                current = next;
            }
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

}