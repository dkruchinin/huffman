package org.dk.huffman;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.dk.huffman.BitInputStream;
import org.dk.huffman.BitOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class BitStreamsTest {
	private File tmpfile;
	private BitInputStream bitIn;
	private BitOutputStream bitOut;

	@Before
	public void setUp() throws Exception {
		tmpfile = File.createTempFile("bitstream_", null);
		bitIn = new BitInputStream(new FileInputStream(tmpfile));
		bitOut = new BitOutputStream(new FileOutputStream(tmpfile));
	}

	@After
	public void tearDown() throws Exception {
		bitIn.close();
		bitOut.close();
		tmpfile.delete();
	}

	@Test
	public void testWriteBit() throws IOException {
		int toWrite = 0xDEADBEE;
		bitOut.write(toWrite, 28);
		bitOut.flush();
		
		int value = 0, numBits = 0, bit;
		while ((bit = bitIn.readBit()) != -1) {
			value |= bit << numBits;
			numBits++;
		}
		
		assertEquals(toWrite, value);
		assertEquals(32, numBits);
	}

	@Test
	public void testWriteBitFlush() throws IOException {
		bitOut.writeBit(1);
		// BitOutputStreams has granularity of 8 bits, so when the buffer
		// is flushed it writes 1 byte to the stream.
		bitOut.flush();
		
		// That's why we expect to read 1 followed by 7 zeros.
		assertEquals(1, bitIn.readBit());
		for (int i = 0; i < 7; i++) {
			assertEquals(0, bitIn.readBit());
		}
		
		// and meet EOF.
		assertEquals(-1, bitIn.readBit());
	}
	
	@Test
	public void testWriteBitNoFlush() throws IOException {
		bitOut.writeBit(1);
		// no flush no data
		assertEquals(-1, bitIn.readBit());
	}

	@Test
	public void testAutoflushFlush() throws IOException {
		// BitOutputStream flushes buffer once it exceeds 1 byte.
		for (int i = 0; i < 9; i++)
			bitOut.writeBit(1);
		
		// hence we expect to read only 8 ones.
		int numOnes = 0, bit;
		while ((bit = bitIn.readBit()) != -1) {
			assertEquals(1, bit);
			numOnes++;
		}
		
		assertEquals(8, numOnes);
	}

}
