package graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Symbol table implementation.
 *
 * @author Albin Hjalmas.
 * @param <K> The type of the keys in this symbol table.
 * @param <V> The type of the elements in this symbol table.
 */
public class ST<K, V> implements Iterable, Serializable {

    private final ArrayList<K> keys;
    private final ArrayList<V> vals;

    /**
     * Constructor.
     */
    public ST() {
        keys = new ArrayList<>();
        vals = new ArrayList<>();
    }

    /**
     * Adds a key with a corresponding value.
     *
     * @param key the key.
     * @param val the value coupled to the key.
     * @precondition key != null && val != null
     */
    public void add(K key, V val) {
        keys.add(key);
        vals.add(val);
    }

    /**
     * Returns the value corresponding to the provided key.
     *
     * @param key the key.
     * @return the value coupled to the key else null.
     * @precondition key != null
     */
    public V get(K key) {
        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i).equals(key)) {
                return vals.get(i);
            }
        }

        return null;
    }

    /**
     * Removes the key and value corresponding to the provided key.
     *
     * @param key the key.
     * @return true if item was successfully deleted.
     */
    public V remove(K key) {
        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i).equals(key)) {
                keys.remove(i);
                return vals.remove(i);
            }
        }

        return null;
    }

    /**
     * Returns the number of key-value pairs in this Symbol table.
     *
     * @return
     */
    public int size() {
        return keys.size();
    }

    /**
     * Returns a Iterator object to iterate over the keys in this symbol table.
     *
     * @return
     */
    public Iterator<K> keyIterator() {
        return keys.iterator();
    }

    /**
     * @return An iterator to be used for iterating the values stored in this
     * symbol table.
     */
    @Override
    public Iterator<V> iterator() {
        return vals.iterator();
    }

    /**
     * Get the keys contained within this symbol table.
     *
     * @return the keys.
     */
    public ArrayList<K> getKeys() {
        return keys;
    }

    /**
     * Get the values contained within this symbol table.
     *
     * @return the values.
     */
    public ArrayList<V> getValues() {
        return vals;
    }
}
