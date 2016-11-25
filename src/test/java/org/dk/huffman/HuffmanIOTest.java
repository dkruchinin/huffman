package org.dk.huffman;

import static org.junit.Assert.*;

import java.io.*;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HuffmanIOTest {
	private File tmpfile;
	private HuffmanInputStream hin;
	private HuffmanOutputStream hout;
	

	@Before
	public void setUp() throws Exception {
		tmpfile = File.createTempFile("huffmanio_", null);
		hin = new HuffmanInputStream(new FileInputStream(tmpfile));
		hout = new HuffmanOutputStream(new FileOutputStream(tmpfile));
	}

	@After
	public void tearDown() throws Exception {
		hin.close();
		hout.close();
		tmpfile.delete();
	}

	@Test
	public void testEncodeDecodeEmpty() throws IOException {
		hout.flush();
		assertEquals(-1, hin.read());
	}

	@Test(expected = IOException.class)
	public void testDecodeCorrupted() throws IOException {
		String outstr = "something that is definitely not a valid huffman segment";
		FileOutputStream out = new FileOutputStream(tmpfile);
		out.write(outstr.getBytes());
		out.close();
		hin.read();
	}
	
	
	@Test
	public void testIOFlush() throws IOException {
		for (int i = 0; i < 100; i++)
			hout.write(0xab);
		for (int i = 0; i < 200; i++)
			hout.write(0xf1);
		
		hout.flush();
		assertTrue(tmpfile.length() < 300);
		int iters = 100;
		while (iters > 0 && hin.read() == 0xab)
			iters--;
		
		assertEquals(0, iters);
		iters = 200;
		while (iters > 0 && hin.read() == 0xf1)
			iters--;

		assertEquals(0, iters);
		assertEquals(-1, hin.read());
	}
	
	@Test
	public void testOIBuffering() throws IOException {
		byte buf[] = new byte[hout.getBufSize()];
		Random rnd = new Random(System.nanoTime());
		for (int i = 0; i < buf.length; i++)
			buf[i] = (byte) rnd.nextInt();
		
		// buf should be flushed and 255 should
		// be buffered.
		hout.write(buf);
		hout.write(255);
		
		int i = 0;
		byte val;
		while ((val = (byte) hin.read()) != -1) {
			assertEquals(buf[i], val);
			i++;
		}
	}
	
	@Test
	public void testReadArray() throws IOException {
		byte source[] = new byte[1024];
		Random rnd = new Random(System.nanoTime());
		int i;
		
		for (i = 0; i < 10; i++) {
			source[i] = 0xf;
			source[source.length - 1 - i] = 0xa;
		}
		for (i = 10; i < source.length - 10; i++)
			source[i] = (byte) rnd.nextInt();
		
		hout.write(source);
		hout.flush();

		byte rBytes[] = new byte[source.length];
		
		assertEquals(10, hin.read(rBytes, 0, 10));
		assertEquals(rBytes.length - 20, hin.read(rBytes, 10, rBytes.length - 20));
		assertEquals(10, hin.read(rBytes, rBytes.length - 10, 10));
		
		assertArrayEquals(source, rBytes);
	}
}
