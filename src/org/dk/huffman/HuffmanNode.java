package org.dk.huffman;

/**
 * This is a class representing a single node
 * in the Huffman tree.
 */
class HuffmanNode implements Comparable<HuffmanNode> {
	private int sym;
	private int freq;
	private boolean leaf;
	private HuffmanNode left;
	private HuffmanNode right;
	private HuffmanNode parent;
	
	/**
	 * Make a leaf huffman node.
	 * @param sym	a symbol	
	 * @param freq	its frequency
	 */
	public HuffmanNode(int sym, int freq) {
		this.sym = sym;
		this.freq = freq;
		leaf = true;
	}
	
	/**
	 * Make a non-leaf huffman node.
	 * Note: non-leaf nodes don't have symbols, but they
	 * have frequencies.
	 * @param left	left subtree
	 * @param right	right subtree
	 */
	public HuffmanNode(HuffmanNode left, HuffmanNode right) {
		freq = left.getFreq() + right.getFreq();
		leaf = false;
		this.left = left;
		this.right = right;
	}
	
	/**
	 * Set parent of the given node
	 * @param parent	a parent node
	 */
	public void setParent(HuffmanNode parent) {
		this.parent = parent;
	}
	
	/**
	 * @return true if the node is leaf, false otherwise
	 */
	public boolean isLeaf() {
		return leaf;
	}
	
	/**
	 * Get symbol stored by a leaf node
	 * Note: if operation is called on non-leaf node
	 * UnsupportedOperationException will be thrown.
	 * 
	 * @return a symbol stored in this leaf node
	 */
	public int getSymbol() {
		if (!isLeaf())
			throw new UnsupportedOperationException("Node is not leaf!");

		return sym;
	}
	
	/**
	 * @return frequency stored by this node
	 */
	public int getFreq() {
		return freq;
	}
	
	/**
	 * @return left subtree
	 */
	public HuffmanNode getLeft() {
		return left;
	}
	
	/**
	 * @return right subtree
	 */
	public HuffmanNode getRight() {
		return right;
	}
	
	/**
	 * @return	parent node
	 */
	public HuffmanNode getParent() {
		return parent;
	}
	
	@Override
	public String toString() {
		String ret = "[F: " + freq + "; L: " + leaf;
		if (leaf)
			ret += String.format("; C: %c", sym);
		
		return ret + "]";
	}

	@Override
	public int compareTo(HuffmanNode node) {
		if (freq > node.freq)
			return 1;
		else if (freq < node.freq)
			return -1;
		else
			return 0;
	}
}
