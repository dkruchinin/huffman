package org.dk.huffman;

import java.io.*;

/**
 * This is a pseudo stream class designed to read
 * data bit by bit from the parent input stream.
 * 
 * Note that data is actually read by 8 bit blocks,
 * BitInputStream just splits the blocks to a
 * sequence of bits.
 */
class BitInputStream {
	protected InputStream in;
	protected int buf;
	protected int bitsInBuf;
	
	/**
	 * Constructor of BitInputStream is very similar
	 * to FilteredInputStream.
	 * @see FilteredInputStream
	 */
	public BitInputStream(InputStream in) {
		this.in = in;
		buf = bitsInBuf = 0;
	}
	
	/**
	 * Read one bit from the stream.
	 * @return next bit value or -1 if EOF is reached.
	 */
	public int readBit() throws IOException {
		if (bitsInBuf == 0) {
			refreshBuffer();
			if (buf == -1) {
				buf = bitsInBuf = 0;
				return -1;
			}
		}
		
		int ret = (buf >> (8 - bitsInBuf)) & 0x1;
		bitsInBuf--;
		return ret;
	}
	
	/**
	 * Closes the parent InputStream.
	 * @see InputStream.close
	 * @throws IOException
	 */
	public void close() throws IOException {
		in.close();
	}
	
	protected void refreshBuffer() throws IOException {
		buf = in.read();
		bitsInBuf = 8;
	}
}
