package org.dk.huffman;

import static org.junit.Assert.*;

import java.io.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HFreqTableTest {
	private HFreqTable ftbl;
	private static String text = new String("Only the fool would take trouble to verify " +
			"that his sentence was composed of ten a's, three b's, four c's, four d's, " +
			"forty-six e's, sixteen f's, four g's, thirteen h's, fifteen i's, two k's, " +
			"nine l's, four m's, twenty-five n's, twenty-four o's, five p's, sixteen r's," +
			" forty-one s's, thirty-seven t's, ten u's, eight v's, eight w's, four x's, eleven" +
			" y's, twenty-seven commas, twenty-three apostrophes, seven hyphens " +
			"and, last but not least, a single !");

	@Before
	public void setUp() throws Exception {
		ftbl = new HFreqTable();
	}

	@After
	public void tearDown() throws Exception {
		ftbl = null;
	}
	
	@Test
	public void testEmptyTable() {
		assertEquals(0, ftbl.getNumSymbols());
		assertEquals(0.0, ftbl.entropy(), 0.0);
	}

	@Test
	public void testAddOneSymbol() {
		ftbl.add((int)'A');
		assertEquals(1, ftbl.getNumSymbols());
		assertEquals(0.0, ftbl.entropy(), 0.0);
	}
	
	@Test 
	public void testAdd() {
		fillTable();

		char[] symbols = text.toCharArray();
		java.util.Arrays.sort(symbols);
		int off = 0;

		while (off < text.length()) {
			char sym = symbols[off];
			int freq;
			for (freq = 1; off + freq< text.length() && sym == symbols[off + freq]; freq++);
			assertEquals(freq, ftbl.getFreq((int)sym));
			off += freq;
		}
	}

	@Test
	public void testSaveRestore() throws IOException {
		fillTable();
		File tmpf = File.createTempFile("hfreqtable_", null);
		DataOutputStream out = new DataOutputStream(new FileOutputStream(tmpf));
		ftbl.save(out);
		out.close();
		
		DataInputStream in = new DataInputStream(new FileInputStream(tmpf));
		HFreqTable restoredFtbl = HFreqTable.restore(in);
		in.close();

		tmpf.delete();
		assertEquals(ftbl, restoredFtbl);
	}

	private void fillTable() {
		for (int i = 0; i < text.length(); i++)
			ftbl.add(text.charAt(i));
	}

}
