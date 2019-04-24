package org.dk.huffman;

import java.util.*;

/**
 * This is a huffman tree class.
 * Given a frequencies of all symbols in the text
 * it builds corresponding huffman tree.
 */
class HuffmanTree {	
	protected HuffmanNode root;
	protected int depth;

	/**
	 * Given a frequency table, build Huffman tree
	 * @param freqTable	a frequency table
	 */
	public HuffmanTree(HFreqTable freqTable) {
		if (freqTable.getNumSymbols() == 0)
			throw new IllegalArgumentException("Frequency table is empty!");

		PriorityQueue<HuffmanNode> pqueue = new PriorityQueue<HuffmanNode>();
		for (Map.Entry<Integer, Integer> entry : freqTable.entrySet())
			pqueue.add(new HuffmanNode(entry.getKey(), entry.getValue()));
		
		while (pqueue.size() > 1) {
			HuffmanNode first = pqueue.poll();
			HuffmanNode second = pqueue.poll();
			pqueue.add(new HuffmanNode(first, second));
		}

		root = pqueue.peek();
	}
	
	/**
	 * @return Root node of the huffman tree
	 * @see HuffmanNode
	 */
	public HuffmanNode getRoot() {
		return root;
	}
	
	/**
	 * @return Depth of the huffman tree
	 */
	public int getDepth() {
		return subtreeDepth(root);
	}
	
	/**
	 * Dump Huffman tree as a set of bit strings
	 * to the standard output.
	 */
	public void dumpHuffmanCodes() {
		doPrintHcode(getRoot(), "");
	}
	
	protected int subtreeDepth(HuffmanNode node) {
		if (node.isLeaf())
			return 1;
		
		return Math.max(subtreeDepth(node.getLeft()), 
				subtreeDepth(node.getRight())) + 1;
	}

	protected void doPrintHcode(HuffmanNode hnode, String strcode) {
		if (!hnode.isLeaf()) {
			doPrintHcode(hnode.getLeft(), strcode + "0");
			doPrintHcode(hnode.getRight(), strcode + "1");
		}
		else
			System.out.format("%d	: %s\n", hnode.getSymbol(), strcode);
	}
}
