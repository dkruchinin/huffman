package org.dk.huffman;

import java.io.*;

public class HuffmanDemo {
	public static void main(String args[]) throws IOException {
		if (args.length < 1)
			usage();

		try {
			if (args[0].equals("enc"))
				doEncode(args);
			else if (args[0].equals("dec"))
				doDeocde(args);
			else if (args[0].equals("entropy"))
				calcEntropy(args);
			else
				usage();
		} catch (FileNotFoundException err) {
			System.err.println("Error: " + err.toString());
			usage();
		}

		System.exit(0);
	}

	public static void doEncode(String[] args) throws IOException {
		if (args.length < 3)
			usage();

		File inFile = new File(args[1]);
		File outFile = new File(args[2]);
		InputStream in = new FileInputStream(inFile);
		HuffmanOutputStream hout = new HuffmanOutputStream(
				new FileOutputStream(outFile));
		byte buf[] = new byte[4096];
		int len;

		while ((len = in.read(buf)) != -1)
			hout.write(buf, 0, len);

		in.close();
		hout.close();

		System.out.println("Compression: done");
		System.out.println("Original file size:     " + inFile.length());
		System.out.println("Compressed file size:   " + outFile.length());
		System.out.print("Compression efficiency: ");
		if (inFile.length() > outFile.length()) {
			System.out.format("%.2f%%\n",
				(100.0 - (((double) outFile.length() / (double) inFile.length()) * 100)));
		}
		else
			System.out.println("none");
	}

	public static void doDeocde(String[] args) throws IOException {
		if (args.length < 3)
			usage();

		File inFile = new File(args[1]);
		File outFile = new File(args[2]);
		HuffmanInputStream hin = new HuffmanInputStream(new FileInputStream(
				inFile));
		OutputStream out = new FileOutputStream(outFile);
		byte buf[] = new byte[4096];
		int len;

		while ((len = hin.read(buf)) != -1)
			out.write(buf, 0, len);

		hin.close();
		out.close();
		System.out.println("Decompression: done");
		System.out.println("Original file size:     " + inFile.length());
		System.out.println("Decompressed file size: " + outFile.length());
	}

	public static void calcEntropy(String[] args) throws IOException {
		if (args.length < 2)
			usage();

		InputStream in = new FileInputStream(args[1]);
		HFreqTable ftbl = new HFreqTable();
		int sym;

		while ((sym = in.read()) != -1)
			ftbl.add(sym);

		in.close();
		System.out.println("Entropy: " + ftbl.entropy());
	}

	public static void usage() {
		System.err.println("USAGE: HuffmanDemo enc|dec|entropy");
		System.err.println("       enc <input-file> <output-file>: " +
				"encode input file and save");
		System.err.println("                        " +
				"the results to output file");
		System.err.println("       dec <input-file> <output-file>: " +
				"decode input file and save");
		System.err.println("                        " +
				"the results to output file");
		System.err.println("       entropy <input-file>: calculate an " +
				"entropy of the symbols in input file");
		System.exit(1);
	}
}
