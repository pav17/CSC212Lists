package edu.smith.cs.csc212.lists;

import me.jjfoley.adt.ListADT;
import me.jjfoley.adt.errors.BadIndexError;
import me.jjfoley.adt.errors.EmptyListError;
import me.jjfoley.adt.errors.TODOErr;

/**
 * This is a data structure that has an array inside each node of an ArrayList.
 * Therefore, we only make new nodes when they are full. Some remove operations
 * may be easier if you allow "chunks" to be partially filled.
 * 
 * @author jfoley
 * @param <T> - the type of item stored in the list.
 */
public class ChunkyArrayList<T> extends ListADT<T> {
	/**
	 * How big is each chunk?
	 */
	private int chunkSize;
	/**
	 * Where do the chunks go?
	 */
	private GrowableList<FixedSizeList<T>> chunks;

	/**
	 * Create a ChunkedArrayList with a specific chunk-size.
	 * @param chunkSize - how many items to store per node in this list.
	 */
	public ChunkyArrayList(int chunkSize) {
		this.chunkSize = chunkSize;
		chunks = new GrowableList<>();
	}
	
	private FixedSizeList<T> makeChunk() {
		return new FixedSizeList<>(chunkSize);
	}

	@Override
	public T removeFront() {
		checkNotEmpty();
		T removedValue = null;
		FixedSizeList<T> front = chunks.getFront();
		removedValue = front.removeFront();
		if (front.isEmpty()) {
			chunks.removeFront();
		}
		return removedValue;
	}

	@Override
	public T removeBack() {
		checkNotEmpty();
		T removedValue = null;
		FixedSizeList<T> back = chunks.getBack();
		removedValue = back.removeBack();
		if (back.isEmpty()) {
			chunks.removeBack();
		}
		return removedValue;
	}

	@Override
	public T removeIndex(int index) {
		checkNotEmpty();
		int listSize = size();
		if (index >= listSize || index < 0) {
			throw new BadIndexError(index);
		} else if (index == 0) {
			return removeFront();
		} else if (index == listSize-1) {
			return removeBack();
		}
		
		T removedValue = null;
		int counter = 0;
		boolean found = false;
		for (int i = 0; i < chunks.size(); i++) {
			FixedSizeList<T> currentSubList = chunks.getIndex(i);
			if (counter + this.chunkSize < index) {
				counter = counter + this.chunkSize;
				continue;
			}
			for (int y = 0; y < currentSubList.size(); y++) {
				if (counter == index) {
					removedValue = currentSubList.removeIndex(y);
					found = true;
					break;
				}
				counter++;
			}
			if (found) {
				if (currentSubList.isEmpty()) {
					chunks.removeIndex(i);
				}
				break;
			}
		}
		return removedValue;
	}

	@Override
	public void addFront(T item) {
		if (chunks.isEmpty()) {
			chunks.addFront(makeChunk());
		}
		
		FixedSizeList<T> front = chunks.getFront();
		if (front.isFull()) {
			front = makeChunk();
			chunks.addFront(front);
		}
		front.addFront(item);
	}

	@Override
	public void addBack(T item) {
		if (chunks.isEmpty()) {
			chunks.addBack(makeChunk());
		}
		
		FixedSizeList<T> back = chunks.getBack();
		if (back.isFull()) {
			back = makeChunk();
			chunks.addBack(back);
		}
		back.addBack(item);
	}

	@Override
	public void addIndex(int index, T item) {
		checkNotEmpty();
		int listSize = size();
		if (index > listSize || index < 0) {
			throw new BadIndexError(index);
		} else if (isEmpty()) {
			addFront(item);
			return;
		} else if (index == listSize) {
			addBack(item);
			return;
		}

		int counter = 0;
		boolean found = false;
		for (int i = 0; i < chunks.size(); i++) {
			FixedSizeList<T> currentSubList = chunks.getIndex(i);
			if (counter + currentSubList.size() < index) {
				counter = counter + currentSubList.size();
				continue;
			}
			for (int y = 0; y < this.chunkSize; y++) {
				if (counter == index) {
					if (y < currentSubList.size()) {
						if (currentSubList.isFull()) {
							if (chunks.size() <= i+1 || chunks.getIndex(i+1).size() == this.chunkSize) {
								FixedSizeList<T> newSubList = makeChunk();
								chunks.addIndex(i+1, newSubList);
							}
							if (chunks.size() > i+1 && currentSubList.size() == this.chunkSize && chunks.getIndex(i+1).size() < this.chunkSize) {
								chunks.getIndex(i+1).addFront(currentSubList.removeBack());
								currentSubList.addIndex(y, item);
							} else {
								FixedSizeList<T> newSubList = makeChunk();
								newSubList.addFront(item);
								chunks.addIndex(i+1, newSubList);
							}
						} else {
							currentSubList.addIndex(y, item);
						}
					} else {
						currentSubList.addBack(item);
					}
					found = true;
					break;
					
				}
				counter++;
			}
			if (found) {
				break;
			}
		}
	}
	
	@Override
	public T getFront() {
		return this.chunks.getFront().getFront();
	}

	@Override
	public T getBack() {
		return this.chunks.getBack().getBack();
	}


	@Override
	public T getIndex(int index) {
		if (this.isEmpty()) {
			throw new EmptyListError();
		}
		int start = 0;
		for (FixedSizeList<T> chunk : this.chunks) {
			// calculate bounds of this chunk.
			int end = start + chunk.size();
			
			// Check whether the index should be in this chunk:
			if (start <= index && index < end) {
				return chunk.getIndex(index - start);
			}
			
			// update bounds of next chunk.
			start = end;
		}
		throw new BadIndexError(index);
	}
	
	@Override
	public void setIndex(int index, T value) {
		checkNotEmpty();
		int listSize = size();
		if (index >= listSize || index < 0) {
			throw new BadIndexError(index);
		}
		
		int counter = 0;
		boolean found = false;
		for (int i = 0; i < chunks.size(); i++) {
			FixedSizeList<T> currentSubList = chunks.getIndex(i);
			if (counter + this.chunkSize < index) {
				counter = counter + this.chunkSize;
				continue;
			}
			for (int y = 0; y < currentSubList.size(); y++) {
				if (counter == index) {
					currentSubList.setIndex(y, value);
					found = true;
					break;
				}
				counter++;
			}
			if (found) {
				break;
			}
		}
	}

	@Override
	public int size() {
		int total = 0;
		for (FixedSizeList<T> chunk : this.chunks) {
			total += chunk.size();
		}
		return total;
	}

	@Override
	public boolean isEmpty() {
		return this.chunks.isEmpty();
	}
}