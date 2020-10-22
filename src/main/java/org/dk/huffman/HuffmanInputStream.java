package org.dk.huffman;

import java.io.*;

/**
 * This is a huffman input stream, it's a successor of
 * FilterInputStream. The class performs transparent
 * data decoding (using huffman algorithm) read from the
 * parent stream.
 * 
 * Note: HuffmanInputStream expects that the data read
 * from the parent stream was written there using
 * HuffmanOutputStream.
 */
public class HuffmanInputStream extends FilterInputStream {
	protected byte[] segment;
	protected int bytesRead;
	protected boolean eof;
	protected boolean isReadFileName = false;
	protected String fileName;
	
	/**
	 * Initialize the huffman input stream.
	 * @param in	parent input stream
	 */
	public HuffmanInputStream(InputStream in) {
		super(in);
		bytesRead = 0;
		segment = null;
		eof = false;
		isReadFileName = false;
	}
	
	@Override
	public int read() throws IOException {
		if (segment == null)
			readSegment();
		if (segment == null && eof)
			return -1;
		
		int ret = segment[bytesRead++];
		if (bytesRead == segment.length) {
			bytesRead = 0;
			segment = null;
		}
		
		return (ret & 0xff);
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int rdLen;
		for (rdLen = 0; rdLen < len; rdLen++) {
			int val = read();
			if (val == -1) {
				if (rdLen == 0)
					return -1;

				break;
			}
			
			b[off + rdLen] = (byte) val;
		}
		
		return rdLen;
	}

	public String getFileName() {
		return fileName;
	}

	private String readFileName(DataInputStream dataIn) throws IOException {
		byte length = dataIn.readByte();
		byte[] data = new byte[length];

		dataIn.read(data);
		return new String(data);
	}
	
	// read next huffman segment
	protected void readSegment() throws IOException {
		DataInputStream dataIn = new DataInputStream(in);
		if(!isReadFileName) {
			fileName = readFileName(dataIn);
			isReadFileName = true;
		}
		
		try {
			// ensure that segment header is valid
			// and read it (segment size and frequency table
			int magic = dataIn.readInt();
			if (magic != HuffmanConsts.HUFFMAN_MAGIC1) {
				throw new IOException("Can't read segment header: magic1 mismatch");
			}
			
			int segSz = dataIn.readInt();
			segment = new byte[segSz];
			HFreqTable freqTable = HFreqTable.restore(dataIn);
			
			magic = dataIn.readInt();
			if (magic != HuffmanConsts.HUFFMAN_MAGIC2)
				throw new IOException("Can't read segment header: magic2 mismatch");
			
			// decode the segment
			HuffmanDecoder dec = new HuffmanDecoder(freqTable);
			BitInputStream bitin = new BitInputStream(in);
			int bytesDecoded = 0;
			while (bytesDecoded < segSz) {
				int bitString = 0, length = 0;

				// continuously read the data bit by bit constructing the
				// bit string. On every iteration check whether the constructed
				// bit string of given length is a huffman code known to decoder.
				// If so, decode it, otherwise continue the process of building
				// bit string.
				while (!dec.hasCode(bitString, length)) {
					int bit = bitin.readBit();
					if (bit == -1)
						break;
					
					bitString |= (bit << length);
					length++;
					if (length >= 32)
						throw new IOException("Huffman code is too long");
				}
				if (length != 0)
					segment[bytesDecoded++] = (byte) dec.decode(bitString, length);
				else {
					eof = true;
				}
			}
		}
		catch (EOFException eoerr) {
			eof = true;
		}
	}
}
