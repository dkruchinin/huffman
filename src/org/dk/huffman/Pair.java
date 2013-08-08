package org.dk.huffman;

public final class Pair<TypeOne, TypeTwo> {
	protected final TypeOne first;
	protected final TypeTwo second;
	
	public Pair(TypeOne first, TypeTwo second) {
		this.first = first;
		this.second = second;
	}
	
	public TypeOne getFirst() {
		return first;
	}
	
	public TypeTwo getSecond() {
		return second;
	}
}
