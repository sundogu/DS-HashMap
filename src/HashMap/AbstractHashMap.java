package HashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class AbstractHashMap<K, V> implements Map<K, V> {
    // Class Variables //
    protected int occupied = 0;
    protected int prime = 109345121;
    protected int capacity;
    protected float loadf;
    protected long scale, shift;

    private static int INIT_CAP = 100;
    private static float INIT_LOAD = 0.5f;

    // Constructors //
    public AbstractHashMap(int capacity, float loadf) {
        this.loadf = loadf;
        this.capacity = capacity;

        Random r = new Random();
        scale = r.nextInt(prime - 1) + 1;
        shift = r.nextInt(prime);

        initMap();
    }

    public AbstractHashMap(int capacity) {
        this(capacity, INIT_LOAD);
    }

    public AbstractHashMap() {
        this(INIT_CAP);
    }

    // Methods //
    @Override
    public int size() {
        return occupied;
    }

    @Override
    public boolean isEmpty() {
        return occupied == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return bucketGet(hashValue(key), key) != null;
    }

    @Override
    public V get(Object key) {
        return bucketGet(hashValue(key), key);
    }

    @Override
    public V put(K key, V value) {
        V temp = bucketPut(hashValue(key), key, value);
        if (occupied > capacity * loadf) {
            resize(2 * capacity - 1);
        }

        return temp;
    }

    @Override
    public V remove(Object key) {
        return bucketRemove(hashValue(key), key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        m.forEach(this::put);
    }

    // Helper Methods //
    private int hashValue(Object key) {
        return (int) ((Math.abs(key.hashCode() * scale + shift) % prime) % capacity);
    }

    private void resize(int newCap) {
        List<Entry<K, V>> temp = new ArrayList<>(entrySet());
        capacity = newCap;
        initMap();
        occupied = 0;
        temp.forEach(e -> put(e.getKey(), e.getValue()));
    }

    protected abstract void initMap();

    protected abstract V bucketGet(int hash, Object key);

    protected abstract V bucketPut(int hash, K key, V value);

    protected abstract V bucketRemove(int hash, Object key);
}
