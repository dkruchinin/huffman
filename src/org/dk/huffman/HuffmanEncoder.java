package org.dk.huffman;

import java.util.*;

/**
 *  This is a Huffman encoder, the class designed to
 *  encode data using Huffman algorithm
 */
public final class HuffmanEncoder {
	protected final HFreqTable freqTable;
	protected final HuffmanTree htree;
	protected final Map<Integer, Long> codeMap;
	
	/**
	 * A constructor of Huffman encoder.
	 * Note that it expects to accept the frequency table of
	 * the data going to be encoded.
	 * @param freqTable	a frequency table of the data to encode
	 */
	public HuffmanEncoder(HFreqTable freqTable) {
		this.freqTable = freqTable;
		htree = new HuffmanTree(freqTable);
		codeMap = new HashMap<Integer, Long>();
		mapHuffmanNode(htree.getRoot(), 0, 0);
	}
	
	/**
	 * Encode one symbol.
	 * @param sym	a symbol to encode
	 * @return a Pair, containing bit string (first item) and
	 *         its length (second item).
	 * @see Pair
	 */
	public Pair<Integer, Integer> encode(int sym) {
		long code = codeMap.get(sym);
		return new Pair<Integer, Integer>((int)code, (int)(code >> 32));
	}
	
	/**
	 * Get Huffman tree
	 * @return Huffman tree of the encoder
	 * @see HuffmanTree
	 */
	public final HuffmanTree getTree() {
		return htree;
	}

	// Build a table of symbols and corresponding huffman codes by iterating
	// over the huffman tree.
	protected void mapHuffmanNode(HuffmanNode hnode, int bitString, int length) {
		if (!hnode.isLeaf()) {
			mapHuffmanNode(hnode.getLeft(),  bitString, length + 1);
			mapHuffmanNode(hnode.getRight(), bitString | (1 << length), length + 1);
		}
		else {
			// Huffman code corresponding to the symbol is encoded
			// to 64bit word. Lower 32 bits store huffman code (bit string)
			// and higher 32 bits store its length.
			long code = ((long)length << 32) | bitString;
			codeMap.put(hnode.getSymbol(), code);
		}
	}
}
