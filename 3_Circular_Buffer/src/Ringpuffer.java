import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

public class Ringpuffer<T> implements Serializable, Cloneable, Queue<T> {
    private final int capacityInc = 10;
    private ArrayList<T> elements;
    private int writePos;
    private int readPos;
    private int size;               // Anzahl tatsächlich verwalteter Elemente
    private int capacity;           // Max. Anzahl der verwalteten Elemente
    private final boolean fixedCapacity;
    private final boolean discarding;

    public Ringpuffer(int capacity, boolean fixedCapacity, boolean discarding) {
        if (capacity >= 0) {
            this.capacity = capacity;
        } else {
            throw new IllegalArgumentException("Ihre Kapazität ist negativ!");
        }
        this.writePos = 0;
        this.readPos = 0;
        this.size = 0;
        this.fixedCapacity = fixedCapacity;
        this.discarding = discarding;
        this.elements = new ArrayList<T>(capacity);
        for(int i = 0; i < capacity; i++) {
            this.elements.add(null);
        }
    }

    /**
     * Returns the number of elements in this collection.  If this collection
     * contains more than {@code Integer.MAX_VALUE} elements, returns
     * {@code Integer.MAX_VALUE}.
     *
     * @return the number of elements in this collection
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns {@code true} if this collection contains the specified element.
     * More formally, returns {@code true} if and only if this collection
     * contains at least one element {@code e} such that
     * {@code Objects.equals(o, e)}.
     *
     * @param o element whose presence in this collection is to be tested
     * @return {@code true} if this collection contains the specified
     * element
     * @throws ClassCastException   if the type of the specified element
     *                              is incompatible with this collection
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null and this
     *                              collection does not permit null elements
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size(); i++) {
            int x = (readPos + i) % capacity;
            if (this.elements.get(x).equals(o)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Returns an iterator over the elements in this collection.  There are no
     * guarantees concerning the order in which the elements are returned
     * (unless this collection is an instance of some class that provides a
     * guarantee).
     *
     * @return an {@code Iterator} over the elements in this collection
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return !isEmpty();
            }

            @Override
            public T next() {
                return Ringpuffer.this.remove();
            }
        };
    }

    /**
     * Returns an array containing all of the elements in this collection.
     * If this collection makes any guarantees as to what order its elements
     * are returned by its iterator, this method must return the elements in
     * the same order. The returned array's {@linkplain Class#getComponentType
     * runtime component type} is {@code Object}.
     *
     * <p>The returned array will be "safe" in that no references to it are
     * maintained by this collection.  (In other words, this method must
     * allocate a new array even if this collection is backed by an array).
     * The caller is thus free to modify the returned array.
     *
     * @return an array, whose {@linkplain Class#getComponentType runtime component
     * type} is {@code Object}, containing all of the elements in this collection
     * @apiNote This method acts as a bridge between array-based and collection-based APIs.
     * It returns an array whose runtime type is {@code Object[]}.
     * Use {@link #toArray(Object[]) toArray(T[])} to reuse an existing
     * array, or use {@link "#toArray(IntFunction)}" to control the runtime type
     * of the array.
     */
    @Override
    public Object[] toArray() {
        Object[] obj = new Object[size()];
        if(isEmpty()) {
            System.out.println("Der Ringpuffer ist leer.");
            return obj;
        }
        for (int i = 0; i < size(); i++) {
            int x = (readPos + i) % capacity;
            obj[i] = this.elements.get(x);
        }
        return obj;
    }

    /**
     * Returns an array containing all of the elements in this collection;
     * the runtime type of the returned array is that of the specified array.
     * If the collection fits in the specified array, it is returned therein.
     * Otherwise, a new array is allocated with the runtime type of the
     * specified array and the size of this collection.
     *
     * <p>If this collection fits in the specified array with room to spare
     * (i.e., the array has more elements than this collection), the element
     * in the array immediately following the end of the collection is set to
     * {@code null}.  (This is useful in determining the length of this
     * collection <i>only</i> if the caller knows that this collection does
     * not contain any {@code null} elements.)
     *
     * <p>If this collection makes any guarantees as to what order its elements
     * are returned by its iterator, this method must return the elements in
     * the same order.
     *
     * @param a the array into which the elements of this collection are to be
     *          stored, if it is big enough; otherwise, a new array of the same
     *          runtime type is allocated for this purpose.
     * @return an array containing all of the elements in this collection
     * @throws ArrayStoreException  if the runtime type of any element in this
     *                              collection is not assignable to the {@linkplain Class#getComponentType
     *                              runtime component type} of the specified array
     * @throws NullPointerException if the specified array is null
     * @apiNote This method acts as a bridge between array-based and collection-based APIs.
     * It allows an existing array to be reused under certain circumstances.
     * Use {@link #toArray()} to create an array whose runtime type is {@code Object[]},
     * or use {@link "#toArray(IntFunction)}" to control the runtime type of
     * the array.
     *
     * <p>Suppose {@code x} is a collection known to contain only strings.
     * The following code can be used to dump the collection into a previously
     * allocated {@code String} array:
     *
     * <pre>
     *     String[] y = new String[SIZE];
     *     ...
     *     y = x.toArray(y);</pre>
     *
     * <p>The return value is reassigned to the variable {@code y}, because a
     * new array will be allocated and returned if the collection {@code x} has
     * too many elements to fit into the existing array {@code y}.
     *
     * <p>Note that {@code toArray(new Object[0])} is identical in function to
     * {@code toArray()}.
     */
    @Override
    public <T1> T1[] toArray(T1[] a) throws NullPointerException {
        if (isEmpty()) {
            System.out.println("Der Ringpuffer ist leer.");
            return a;
        } else if (a == null) {
            throw new NullPointerException("Das mitgegebene Array ist null.");
        } else if (a.length >= size()) {
            for (int i = 0; i < size(); i++) {
                int x = (readPos + i) % capacity;
                a[i] = (T1) this.elements.get(x);
            }
        } else {
            Array[] arr = new Array[size()];
            toArray(arr);
        }
        return a;
    }

    /**
     * Inserts the specified element into this queue if it is possible to do so
     * immediately without violating capacity restrictions, returning
     * {@code true} upon success and throwing an {@code IllegalStateException}
     * if no space is currently available.
     *
     * @param t the element to add
     * @return {@code true} (as specified by {@link Collection#add})
     * @throws IllegalStateException    if the element cannot be added at this
     *                                  time due to capacity restrictions
     * @throws ClassCastException       if the class of the specified element
     *                                  prevents it from being added to this queue
     * @throws NullPointerException     if the specified element is null and
     *                                  this queue does not permit null elements
     * @throws IllegalArgumentException if some property of this element
     *                                  prevents it from being added to this queue
     */
    @Override
    public boolean add(T t) throws IllegalStateException, ClassCastException, NullPointerException, IllegalArgumentException {
        boolean status = offer(t);
        if(!status) {
            throw new IllegalStateException("Der Ringpuffer ist voll, denn discarding false und fixedCapacity ist true");
        } else {
            return true;
        }
    }

    public void ausgabeDesRingpuffers() {
        for (int i = 0; i < capacity; i++) {
            System.out.print(this.elements.get(i) + " ");
        }
        System.out.println("\n");
    }

    /**
     * Removes a single instance of the specified element from this
     * collection, if it is present (optional operation).  More formally,
     * removes an element {@code e} such that
     * {@code Objects.equals(o, e)}, if
     * this collection contains one or more such elements.  Returns
     * {@code true} if this collection contained the specified element (or
     * equivalently, if this collection changed as a result of the call).
     *
     * @param o element to be removed from this collection, if present
     * @return {@code true} if an element was removed as a result of this call
     * @throws ClassCastException            if the type of the specified element
     *                                       is incompatible with this collection
     *                                       (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException          if the specified element is null and this
     *                                       collection does not permit null elements
     *                                       (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws UnsupportedOperationException if the {@code remove} operation
     *                                       is not supported by this collection
     */
    @Override
    public boolean remove(Object o) throws ClassCastException, NullPointerException, UnsupportedOperationException {

        throw new UnsupportedOperationException();

        /*
        if (o == null) {
            throw new NullPointerException("Das übergebene Objekt ist null.");
        } else if (isEmpty()) {
            System.out.println("Der Ringpuffer ist leer. Es gibt nichts zu entfernen.");
        }

        for (int i = 0; i < size(); i++) {
            if (readPos == capacity) {
                readPos = 0;
            }
            if (this.elements.get(readPos).equals(o)) {
                System.out.println("Mindestens ein Element gefunden und logisch entfernt.");
                size--;
                readPos++;
                return true;
            }
            readPos++;
        }
        System.out.println("Element nicht gefunden.");
        return false;
*/
    }

    /**
     * Returns {@code true} if this collection contains all of the elements
     * in the specified collection.
     *
     * @param c collection to be checked for containment in this collection
     * @return {@code true} if this collection contains all of the elements
     * in the specified collection
     * @throws ClassCastException   if the types of one or more elements
     *                              in the specified collection are incompatible with this
     *                              collection
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified collection contains one
     *                              or more null elements and this collection does not permit null
     *                              elements
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>),
     *                              or if the specified collection is null.
     * @see #contains(Object)
     */
    @Override
    public boolean containsAll(Collection<?> c) throws ClassCastException, NullPointerException {
        if (c.size() != size()) {
            System.out.println("Die Collections haben nicht die gleiche Anzahl an Objekten!");
            return false;
        } else if (c.isEmpty()) {
            System.out.println("Die übergebene Collection ist leer.");
        }

        boolean existiert = false;

        for (int i = 0; i < c.size(); i++) {
            for(int k = 0; k < size(); k++) {
                int x = (readPos + k) % capacity;
                if(c.toArray()[i].equals(this.elements.get(x))){
                    existiert = true;
                }
            }
            if(!existiert) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds all of the elements in the specified collection to this collection
     * (optional operation).  The behavior of this operation is undefined if
     * the specified collection is modified while the operation is in progress.
     * (This implies that the behavior of this call is undefined if the
     * specified collection is this collection, and this collection is
     * nonempty.)
     *
     * @param c collection containing elements to be added to this collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws UnsupportedOperationException if the {@code addAll} operation
     *                                       is not supported by this collection
     * @throws ClassCastException            if the class of an element of the specified
     *                                       collection prevents it from being added to this collection
     * @throws NullPointerException          if the specified collection contains a
     *                                       null element and this collection does not permit null elements,
     *                                       or if the specified collection is null
     * @throws IllegalArgumentException      if some property of an element of the
     *                                       specified collection prevents it from being added to this
     *                                       collection
     * @throws IllegalStateException         if not all the elements can be added at
     *                                       this time due to insertion restrictions
     * @see #add(Object)
     */
    @Override
    public boolean addAll(Collection<? extends T> c) throws ClassCastException, NullPointerException, IllegalArgumentException, IllegalStateException {
        if (c.isEmpty()) {
            System.out.println("Ihre Collection ist leer!");
            return false;
        }

        for (int i = 0; i < c.size(); i++) {
            add((T) c.toArray()[i]); // TODO: Fragen was es hier genau mit dem Unchecked Cast auf sich hat?
        }
        return true;
    }

    /**
     * Removes all of this collection's elements that are also contained in the
     * specified collection (optional operation).  After this call returns,
     * this collection will contain no elements in common with the specified
     * collection.
     *
     * @param c collection containing elements to be removed from this collection
     * @return {@code true} if this collection changed as a result of the
     * call
     * @throws UnsupportedOperationException if the {@code removeAll} method
     *                                       is not supported by this collection
     * @throws ClassCastException            if the types of one or more elements
     *                                       in this collection are incompatible with the specified
     *                                       collection
     *                                       (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException          if this collection contains one or more
     *                                       null elements and the specified collection does not support
     *                                       null elements
     *                                       (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>),
     *                                       or if the specified collection is null
     * @see #remove(Object)
     * @see #contains(Object)
     */
    @Override
    public boolean removeAll(Collection<?> c) throws UnsupportedOperationException, ClassCastException, NullPointerException {

        throw new UnsupportedOperationException();

        /*
        boolean veraenderung = false;
        for (int i = 0; i < c.size(); i++) {
            if(remove(c.toArray()[i])) {
                veraenderung = true;
            }
        }
        return veraenderung;
         */
    }

    /**
     * Retains only the elements in this collection that are contained in the
     * specified collection (optional operation).  In other words, removes from
     * this collection all of its elements that are not contained in the
     * specified collection.
     *
     * @param c collection containing elements to be retained in this collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws UnsupportedOperationException if the {@code retainAll} operation
     *                                       is not supported by this collection
     * @throws ClassCastException            if the types of one or more elements
     *                                       in this collection are incompatible with the specified
     *                                       collection
     *                                       (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException          if this collection contains one or more
     *                                       null elements and the specified collection does not permit null
     *                                       elements
     *                                       (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>),
     *                                       or if the specified collection is null
     * @see #remove(Object)
     * @see #contains(Object)
     */
    @Override
    public boolean retainAll(Collection<?> c) throws UnsupportedOperationException, ClassCastException, NullPointerException {

        throw new UnsupportedOperationException();

        /*
        boolean veraenderung = false;
        for (int i = 0; i < c.size(); i++) {
            if (!contains(c.toArray()[i])) {
                remove(c.toArray()[i]);
                veraenderung = true;
            }
        }
        return veraenderung;
         */
    }

    /**
     * Removes all of the elements from this collection (optional operation).
     * The collection will be empty after this method returns.
     *
     * @throws UnsupportedOperationException if the {@code clear} operation
     *                                       is not supported by this collection
     */
    @Override
    public void clear() throws UnsupportedOperationException {
        size = 0;
        readPos = 0;
        writePos = 0;
    }

    /**
     * Inserts the specified element into this queue if it is possible to do
     * so immediately without violating capacity restrictions.
     * When using a capacity-restricted queue, this method is generally
     * preferable to {@link #add}, which can fail to insert an element only
     * by throwing an exception.
     *
     * @param t the element to add
     * @return {@code true} if the element was added to this queue, else
     * {@code false}
     * @throws ClassCastException       if the class of the specified element
     *                                  prevents it from being added to this queue
     * @throws NullPointerException     if the specified element is null and
     *                                  this queue does not permit null elements
     * @throws IllegalArgumentException if some property of this element
     *                                  prevents it from being added to this queue
     */
    @Override
    public boolean offer(T t) throws ClassCastException, NullPointerException, IllegalArgumentException {
        if (t == null) {
            throw new NullPointerException("Das übergebene Objekt ist null.");
        }

        if (size() < capacity) {
            this.elements.set(writePos, t);
            size++;
            writePos = (writePos + 1) % this.capacity;
        } else if (discarding) {
            this.elements.set(writePos, t);
            writePos = (writePos + 1) % this.capacity;
            readPos = (readPos + 1) % this.capacity;
        } else if (fixedCapacity) {
            throw new IllegalStateException("Es können keine weiteren Elemente hinzugefügt werden! Prüfen Sie Ihre Kapazität bitte.");
        } else {
            capacity += capacityInc;
            ArrayList<T> newAL = new ArrayList<>(capacity);
            erweitereAlteArrayList(newAL);
            add(t);
        }
        return true;
    }

    private void erweitereAlteArrayList(ArrayList<T> newAL) {
        for(int i = 0; i < capacity; i++) {
            newAL.add(null);
        }

        for (int k = 0; k < this.elements.size(); k++) {
            int x = (readPos + k) % capacity;
            newAL.set(k, this.elements.get(x));
        }
        readPos = 0;
        writePos = size();
        this.elements = newAL;
    }

    /**
     * Retrieves and removes the head of this queue.  This method differs
     * from {@link #poll() poll()} only in that it throws an exception if
     * this queue is empty.
     *
     * @return the head of this queue
     * @throws "NoSuchElementException" if this queue is empty
     */
    @Override
    public T remove() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("Der Ringpuffer ist leer... Element kann nicht gelöscht werden.");
        }
        return poll();
    }

    /**
     * Retrieves and removes the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    @Override
    public T poll() {
        if (isEmpty()) {
            return null;
        }
        T zuLoeschen = this.elements.get(readPos);
        readPos = (readPos + 1) % capacity;
        size--;
        return zuLoeschen;
    }

    /**
     * Retrieves, but does not remove, the head of this queue.  This method
     * differs from {@link #peek peek} only in that it throws an exception
     * if this queue is empty.
     *
     * @return the head of this queue
     * @throws "NoSuchElementException" if this queue is empty
     */
    @Override
    public T element() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("Der Ringpuffer ist leer... Element kann nicht gelöscht werden.");
        }
        return this.elements.get(readPos);
    }

    /**
     * Retrieves, but does not remove, the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    @Override
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return this.elements.get(readPos);
    }

    /**
     * Umsetzung des Interfaces "Cloneable".
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void toStringReadPos() {
        System.out.println("Leseposition: " + this.readPos);
    }

    public void toStringWritePos() {
        System.out.println("Schreibposition: " + this.writePos);
    }
}
