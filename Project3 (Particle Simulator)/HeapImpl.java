import java.util.Arrays;

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
		if(_numElements >= _storage.length){
			addExtraSpace();
		}
		_storage[_numElements] = data;
		_numElements++;
		trickleUp();
		return;
	}

	public T removeFirst () {
		if (size() == 0) {
			System.out.println("Removing Nothing From Heap");
		}

		T data = _storage[0];
		_storage[0] = _storage[size() - 1];
		_numElements--;
		trickleDown();
		return data;
	}

	private void trickleUp () {
		int position = size() - 1;
		while (hasParent(position) && comparableToParent(position)) {
			swapPositions(getParentIndex(position), position);
			position = getParentIndex(position);
		}
	}

	private boolean comparableToParent (int position) {
		int x = _storage[position].compareTo(_storage[getParentIndex(position)]);
		return x > 0;
	}

	private boolean compareGreaterThan (int position1, int position2) {
		int x = _storage[position1].compareTo(_storage[position2]);
		return x > 0;
	}

	private void trickleDown () {
		int position = 0;
		while(hasLeftChild(position)){
			int largerChildPosition = getLeftChildIndex(position);
			if (hasRightChild(position) && compareGreaterThan(getRightChildIndex(position), getLeftChildIndex(position))){
				largerChildPosition = getRightChildIndex(position);
			}

			if (compareGreaterThan(position, largerChildPosition)){
				break;
			} else {
				swapPositions(position, largerChildPosition);
				position = largerChildPosition;
			}

		}
	}

	public int size () {
		return _numElements;
	}

	private int getParentIndex(int position) {
		return (position - 1)/2;
	}

	private int getLeftChildIndex(int position) {
		return (2 * position + 1);
	}

	private int getRightChildIndex(int position) {
		return (2 * position + 2);
	}

	private boolean hasLeftChild(int position){
		return getLeftChildIndex(position) < size();
	}
	private boolean hasRightChild(int position) {
		return getRightChildIndex(position) < size();
	}
	private boolean hasParent(int position) {
		return getParentIndex(position) >= 0;
	}

	private void swapPositions(int p1, int p2){
		T temp = _storage[p1];
		_storage[p1] = _storage[p2];
		_storage[p2] = temp;
	}

	private void addExtraSpace() {
		_storage = Arrays.copyOf(_storage, size() * 2);
	}
}
