package edu.smith.cs.csc212.lists;

import me.jjfoley.adt.ListADT;
import me.jjfoley.adt.errors.BadIndexError;

/**
 * A Singly-Linked List is a list that has only knowledge of its very first
 * element. Elements after that are chained, ending with a null node.
 * 
 * @author jfoley
 *
 * @param <T> - the type of the item stored in this list.
 */
public class SinglyLinkedList<T> extends ListADT<T> {
	/**
	 * The start of this list. Node is defined at the bottom of this file.
	 */
	Node<T> start = null;

	@Override
	public T removeFront() {
		checkNotEmpty();
		T removedValue = this.start.value;
		this.start = this.start.next;
		return removedValue;
	}

	@Override
	public T removeBack() {
		checkNotEmpty();
		T removedValue = null;
		boolean singleItemList = false;
		for (Node<T> currentNode = this.start; currentNode != null; currentNode = currentNode.next) {
			if (currentNode.next == null) {
				removedValue = currentNode.value;
				singleItemList = true;
			} else if (currentNode.next.next == null) {
				removedValue = currentNode.next.value;
				currentNode.next = null;
			}
		}
		if (singleItemList) {
			this.start = null;
		}
		return removedValue;
	}

	@Override
	public T removeIndex(int index) {
		checkNotEmpty();
		//System.out.println("index to remove is: " + index);
		int listSize = this.size();
		if (index >=  listSize|| index < 0) {
			throw new BadIndexError(index);
		}
		T removedValue = null;
		if (index == listSize-1) {
			return removeBack();
		} else if (listSize == 1 || index == 0) {
			return removeFront();
		}
		
		Node<T> currentNode = this.start;
		for (int i = 0; i <= index; i++) {
			if (currentNode.next != null) {
				if (i == index-1) {
					removedValue = currentNode.next.value;
					currentNode.next = currentNode.next.next;
					break;
				}
				currentNode = currentNode.next;
			}
		}
		return removedValue;
	}

	@Override
	public void addFront(T item) {
		this.start = new Node<T>(item, this.start);
	}

	@Override
	public void addBack(T item) {
		for (Node<T> currentNode = this.start; currentNode != null; currentNode = currentNode.next) {
			if (currentNode.next == null) {
				currentNode.next = new Node<T>(item, currentNode.next);
				break;
			}
		}
		if (this.start == null) {
			this.start = new Node<T>(item, this.start);
		}
	}

	@Override
	public void addIndex(int index, T item) {
		int startSize = this.size();
		if (index > startSize || index < 0) {
			throw new BadIndexError(index);
		}
		if (index == 0) {
			addFront(item);
			return;
		} else if (index == startSize) {
			addBack(item);
			return;
		} 
		
		Node<T> currentNode = this.start;
		for (int i = 0; i <= index; i++) {
			if (i+1 == index) {
				//System.out.println("Replacing " + currentNode.next.value + " with " + item);
				currentNode.next = new Node<T>(item, currentNode.next);
				break;
			}
			if (currentNode.next != null) {
				currentNode = currentNode.next;
			}
		}
	}

	@Override
	public T getFront() {
		checkNotEmpty();
		return this.start.value;
	}

	@Override
	public T getBack() {
		checkNotEmpty();
		T retrievedValue = null;
		for (Node<T> currentNode = this.start; currentNode != null; currentNode = currentNode.next) {
			if (currentNode.next == null) {
				retrievedValue = currentNode.value;
			}
		}
		return retrievedValue;
	}

	@Override
	public T getIndex(int index) {
		checkNotEmpty();
		int at = 0;
		for (Node<T> n = this.start; n != null; n = n.next) {
			if (at++ == index) {
				return n.value;
			}
		}
		throw new BadIndexError(index);
	}

	@Override
	public void setIndex(int index, T value) {
		checkNotEmpty();
		int at = 0;
		for (Node<T> n = this.start; n != null; n = n.next) {
			if (at++ == index) {
				n.value = value;
				return;
			}
		}
		throw new BadIndexError(index);
	}

	@Override
	public int size() {
		int count = 0;
		for (Node<T> n = this.start; n != null; n = n.next) {
			count++;
		}
		return count;
	}

	@Override
	public boolean isEmpty() {
		return this.start == null;
	}

	/**
	 * The node on any linked list should not be exposed. Static means we don't need
	 * a "this" of SinglyLinkedList to make a node.
	 * 
	 * @param <T> the type of the values stored.
	 */
	private static class Node<T> {
		/**
		 * What node comes after me?
		 */
		public Node<T> next;
		/**
		 * What value is stored in this node?
		 */
		public T value;

		/**
		 * Create a node with no friends.
		 * 
		 * @param value - the value to put in it.
		 * @param next - the successor to this node.
		 */
		public Node(T value, Node<T> next) {
			this.value = value;
			this.next = next;
		}
	}

}
