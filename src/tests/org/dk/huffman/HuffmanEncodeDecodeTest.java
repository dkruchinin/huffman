package org.dk.huffman;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HuffmanEncodeDecodeTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyTable() {
		HFreqTable htbl = new HFreqTable();
		@SuppressWarnings("unused")
		HuffmanEncoder enc = new HuffmanEncoder(htbl);
	}

	@Test
	public void testEncodeDecode() {
		String text = "Huffman test string 0123456789 ...";
		HFreqTable ftbl = new HFreqTable();
		
		for (int i = 0; i < text.length(); i++)
			ftbl.add((int) text.charAt(i));
		
		HuffmanEncoder enc = new HuffmanEncoder(ftbl);
		@SuppressWarnings("unchecked")
		Pair<Integer, Integer> codes[] = (Pair<Integer, Integer>[]) new Pair[text.length()];

		for (int i = 0; i < text.length(); i++)
			codes[i] = enc.encode(text.charAt(i));
		
		HuffmanDecoder dec = new HuffmanDecoder(ftbl);
		String decString = "";
		for (int i = 0; i < text.length(); i++)
			decString += (char)dec.decode(codes[i].getFirst(), codes[i].getSecond());
		
		assertEquals(text, decString);
	}
}
