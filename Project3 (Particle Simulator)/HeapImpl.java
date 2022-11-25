class HeapImpl<T extends Comparable<? super T>> implements Heap<T> {
	private static final int INITIAL_CAPACITY = 128;
	private T[] _storage;
	private int _numElements;

	@SuppressWarnings("unchecked")
	public HeapImpl () {
		_storage = (T[]) new Comparable[INITIAL_CAPACITY];
		_numElements = 0;
	}

	@SuppressWarnings("unchecked")
	public void add (T data) {
		// TODO: implement me
	}

	public T removeFirst () {
		// TODO: implement me
		return null;
	}

	public int size () {
		return _numElements;
	}
}
