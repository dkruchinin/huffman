package org.dk.huffman;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This is a pseudo output stream, it is designed
 * to write bits to the OutputStream.
 * 
 * Note that the BitOutputStream buffers written
 * bits to 8 bits buffer. Ones the buffer overflow
 * happens, it flushes 8 bits to parent OutputStream.
 * So the data is granulated by 8 bits, you can not
 * write less than that.
 */
class BitOutputStream {
	protected OutputStream out;
	protected int bitsWritten;
	protected int buf;

	/**
	 * The constructor of BitOutputStream is very
	 * similar to FilteredOutputStream.
	 * @see FilteredOutputStream
	 */
	public BitOutputStream(OutputStream out) {
		this.out = out;
		bitsWritten = 0;
		buf = 0;
	}
	
	/**
	 * Write specified number of bits from the word to
	 * the stream. This method just calls writeBit multiple times.
	 * 
	 * @param word	a string of bits
	 * @param nbits	number of bits to write
	 * @see writeBit
	 */
	public void write(int word, int nbits) throws IOException {
		while (nbits-- > 0) {
			writeBit(word);
			word >>= 1;
		}
	}

	/**
	 * Write one bit to the stream.
	 * @param bit 	a bit to write
	 */
	public void writeBit(int bit) throws IOException {
		buf |= (bit & 0x1) << bitsWritten;
		bitsWritten++;
		if (bitsWritten == 8)
			flushBitBuffer();
	}
	
	/**
	 * Flushe bits in the buffer (if any) to the
	 * parent stream. Note that if buffer is not
	 * empty, 8 bits will be flushed.
	 */
	public void flush() throws IOException {
		if (bitsWritten > 0)
			flushBitBuffer();

		out.flush();
	}
	
	/**
	 * Flush the buffer and close the parent stream.
	 * @see OutputStream.close
	 */
	public void close() throws IOException {
		flush();
		out.close();
	}
	
	protected void flushBitBuffer() throws IOException {
		out.write(buf);
		buf = 0;
		bitsWritten = 0;
	}
}
