package org.dk.huffman;

import java.io.*;	

public class HuffmanDemo {
	public static void main(String args[]) throws IOException {
		if (args.length != 1) {
			System.err.println("Usage: HuffmanDemo <filename>");
			System.exit(1);
		}
		
		InputStream in = new FileInputStream(args[0]);
		HuffmanOutputStream hout = new HuffmanOutputStream(new FileOutputStream("outfile"));
		byte buf[] = new byte[1024];
		int len;
		
		while ((len = in.read(buf)) != -1) {
			hout.write(buf, 0, len);
		}

		in.close();
		hout.close();
		System.out.println("done");
		System.out.println("decoding");
		
		HuffmanInputStream hin = new HuffmanInputStream(new FileInputStream("outfile"));
		OutputStream out = new FileOutputStream("orig");
		while ((len = hin.read(buf)) != -1) {
			out.write(buf, 0, len);
		}

		hin.close();
		out.close();
		System.exit(0);
	}
}
