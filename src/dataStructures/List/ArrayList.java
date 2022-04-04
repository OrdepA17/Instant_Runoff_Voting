package dataStructures.List;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayList<E> implements List<E>{

	private E[] elements;
	private int capacity;
	private int size = 0;
	
	private static final int DEFAULT_CAPACITY = 25;
	
	@SuppressWarnings("hiding")
	private class ArrayListIterator<E> implements Iterator<E>{
		int currentPosition = 0;
		
		public ArrayListIterator() {
			currentPosition = 0;
		}
		@Override
		public boolean hasNext() {
			return currentPosition < size();
		}

		@SuppressWarnings("unchecked")
		@Override
		public E next() {
			if(hasNext()) {
				E elm = (E) elements[currentPosition];
				currentPosition++;
				return elm;
			}
			throw new NoSuchElementException();
		}
		
	}
	
	
	@SuppressWarnings("unchecked")
	public ArrayList() {
		
		capacity = DEFAULT_CAPACITY;
		elements = (E[]) new Object[capacity];
		
	}
	
	public ArrayList(ArrayList<E> toCopy) {
		elements = toCopy.elements;
		capacity = toCopy.capacity;
		size = toCopy.size;
	}

	@Override
	public boolean add(E elm) {
		
		add(size,elm);
		return true;
		
	}
	
	@SuppressWarnings("unchecked")
	private void reAllocate() {
		E[] newElements = (E[]) new Object[capacity * 2];
		
		for (int i = 0; i < capacity; i++) 
			newElements[i] = elements[i];
		
		elements = newElements;
	}

	@Override
	public void add(int index, E elm) {
		indexOutOfBound(index);
			
		if(size >= capacity) reAllocate();
		
		for(int i = size; i > index; i--) 
			elements[i] = elements[i-1];
		
		elements[index] = elm;
		size++;
	}

	@Override
	public boolean remove(E elm) {
		int pos = -1;
		
		for(int i = 0; i < capacity; i++) {
			if(elements[i].equals(elm)) {
				pos = i;
				break;
			}
		}
		
		if(pos >= 0) {
			for(int i = pos; i < capacity - 1; i++) {
				elements[i] = elements[i+1];
			}
			capacity--;
			elements[capacity] = null;
			return true;
		}
		
		return false;
	}

	@Override
	public E remove(int index) {
		indexOutOfBound(index);
		
		E value = elements[index];
		for(int i = index + 1 ; i < size ; i++) {
			elements[i - 1] = elements[i];
		}
		size--;
		return value;
	}

	@Override
	public int removeAll(E elm) {
		//Inefficient!
		int copiesRemoved = 0;
		while(remove(elm)) copiesRemoved++;
		return copiesRemoved;
	}

	@Override
	public void clear() {
		for (int i = 0; i < capacity; i++) {
			elements[i] = null;
		}
		size = 0;
	}

	@Override
	public boolean contains(E elm) {
		for(int i = 0; i < capacity; i++) {
			if(elements[i].equals(elm)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public E first() {
		return elements[0];
	}

	@Override
	public E last() {
		if(isEmpty()) return null;
		return elements[capacity - 1];
	}

	@Override
	public E get(int index) {
		indexOutOfBound(index);
		
		return elements[index];
	}

	@Override
	public E set(int index, E elm) {
		indexOutOfBound(index);
		E value = elements[index];
		elements[index] = elm;
		return value;
	}

	@Override
	public int firstIndexOf(E elm) {
		for(int i = 0; i < capacity; i++) {
			if(elements[i].equals(elm)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public int lastIndexOf(E elm) {
		for(int i = capacity - 1; i >= 0; i--) {
			if(elements[i].equals(elm)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public Iterator<E> iterator() {
		return new ArrayListIterator<>();
	}
	private void indexOutOfBound(int index) {
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException(index);
		}
	}
	
	public String toString() {
		String value = "[";
		for (int i = 0; i < size; i++) {
			if (i == size - 1) {
				value += elements[i];
			}
			else {
				value += elements[i] + ",";
			}
		}
		value += "]";
		return value;
	}
	
	
}
