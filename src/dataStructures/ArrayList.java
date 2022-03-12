package dataStructures;

import java.util.Iterator;

public class ArrayList<E> implements List<E> {

	private E[] elements;
	private int currentSize;
	private static final int DEFAULT_SIZE=25;


@SuppressWarnings("unchecked")
public ArrayList(int initialCapacity) {
	if(initialCapacity < 1)
		throw new IllegalArgumentException("Size must be at least 1");
	
	currentSize = 0;
	elements=(E[]) new Object[initialCapacity];
		
}

public ArrayList() {
	this(DEFAULT_SIZE);
}

@Override
public Iterator<E> iterator() {
	
	return null;
}

@Override
public void add(E elm) {
	
	if (currentSize == elements.length) {
		reAllocate();
	}
	elements[currentSize] = elm;
	currentSize++;
	
	
}

@SuppressWarnings("unchecked")
private void reAllocate() {
	E[] newElements= (E[]) new Object[currentSize *2];
	for (int i = 0; i < currentSize; i++) 
		newElements[i]=elements[i];
	
	elements= newElements;
}

@Override
public void add(int index, E elm) {

	if(index < 0 || index > currentSize)
		throw new ArrayIndexOutOfBoundsException("Index must be between 0 and size() - 1");
	
	if (index==currentSize) {
		add(elm);
	}
	
	if(currentSize == elements.length) reAllocate();
	
	for(int i = currentSize; i >= index; i--) {
		elements[i]=elements[i-1];
	}
	
	elements[index]=elm;
	currentSize++;
	
}

@Override
public boolean remove(E elm) {

	int pos = -1;
	for(int i = 0; i < currentSize; i++) {
		if(elements[i].equals(elm)) {
			pos = i;
			break;
		}
	}
	
	if(pos >= 0) {
		for(int i = pos; i < currentSize -1; i++) {
			elements[i] = elements[i+1];
		}
		currentSize--;
		elements[currentSize]=null;
		return true;	
	}
	
	return false;
}

@Override
public boolean remove(int index) {
	
	if (index < 0 || index >= currentSize) {
		throw new ArrayIndexOutOfBoundsException("Index must be between 0 and size()");
	}
		for(int i = index; i<currentSize -1; i++) {
				elements[i] = elements[i+1];
			}
			currentSize--;
			elements[currentSize]=null;
			return true;
		
	
}

@Override
public int removeAll(E elm) {
	// Inefficient
	int copiesRemoved = 0;
	while(remove(elm)) copiesRemoved++;
	return copiesRemoved;
	
}

@Override
public void clear() {
	for( int i = 0; i < currentSize; i++) {
		elements[i] = null;
	}
	currentSize = 0;
}

@Override
public boolean contains(E elm) {
	for(int i = 0; i < currentSize; i++) {
		if(elements[i].equals(elm)) {
			return true;
		}
	}
	return false;
}

@Override
public int size() {
	
	return currentSize;
}

@Override
public E first() {

	return elements[0];
}
@Override
public E last() {
	if(isEmpty()) return null;
	return elements[currentSize-1];
}

@Override
public E get(int index) {
	if (index < 0 || index >= currentSize) {
		throw new ArrayIndexOutOfBoundsException("Index must be between 0 and size()");
	}
	return elements[index];
}

@Override
public E set(int index, E elm) {
	if (index < 0 || index >= currentSize) {
		throw new ArrayIndexOutOfBoundsException("Index must be between 0 and size()");
	}
	 return elements[index] = elm;
}

@Override
public int firstIndexOf(E elm) {
	for(int i = 0; i < currentSize; i++) {
		if(elements[i].equals(elm)) {
			return i;
		}
	}
	return -1;
}

@Override
public int lastIndexOf(E elm) {
	for(int i = currentSize - 1; i >= 0; i--) {
		if(elements[i].equals(elm)) {
			return i;
		}
	}
	return -1;
}


@Override
public boolean isEmpty() {
	return size() ==0;
}


}

