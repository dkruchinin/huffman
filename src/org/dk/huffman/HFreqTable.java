package org.dk.huffman;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


/**
 * This is Huffman frequency table, a class designed
 * for calculation of frequency of symbols in text and
 * their entropy.
 * 
 * Objects of this class can be saved to DataOutputStream
 * and restored from DataInputStream.
 */
public class HFreqTable {
	// NOTE: freqTable can not be just any Map, we really
	// care about the order of keys here.
	protected LinkedHashMap<Integer, Integer> freqTable;
	protected int numSyms;
	
	/**
	 * The constructor of Huffman frequency table.
	 */
	public HFreqTable() {
		freqTable = new LinkedHashMap<Integer, Integer>();
		numSyms = 0;
	}
	
	protected HFreqTable(LinkedHashMap<Integer, Integer> map, int numSyms) {
		freqTable = map;
		this.numSyms = numSyms; 
	}
	
	/**
	 * Add a code to the table, increase its frequency
	 * if the code already exists.
	 * 
	 * @param code 	a code to add
	 */
	public void add(int code) {
		int freq = 0;
		if (freqTable.containsKey(code))
			freq = freqTable.get(code);
	
		freqTable.put(code, freq + 1);
		numSyms++;
	}
	
	/**
	 * Get frequency of the given code
	 * @param code	a code which frequency should be returned
	 * @return a frequency of the code or -1 if the code does not exist
	 */
	public int getFreq(int code) {
		if (!freqTable.containsKey(code))
			return -1;
		
		return freqTable.get(code);
	}
	
	/**
	 * Get a set of codes and their frequencies.
	 * @return a set of codes and their frequencies
	 * 
	 * @see Set
	 * @see Map.Entry
	 */
	public Set<Map.Entry<Integer,Integer>> entrySet() {
		return freqTable.entrySet();
	}
	
	/**
	 * @return the total number of symbols added to the table.
	 */
	public int getNumSymbols() {
		return numSyms;
	}
	
	/**
	 * Dump the table to the standard output.
	 */
	public void dump() {
		System.out.println("=== Frequency Table ====");
		System.out.println("Code     Frequency");
		for (Map.Entry<Integer, Integer> entry : entrySet()) {
			System.out.format("%04d     %9d\n", entry.getKey(), entry.getValue());
		}
	}
	
	/**
	 * Save the table in binary form to the given stream.
	 * @param out	a DataOutputStream the table will be stored to
	 * @see DataOutputStream
	 */
	public void save(DataOutputStream out) throws IOException {
		// Format:
		// <Integer>: total number of symbols in the table
		// <Integer>: the size of hash table used by the frequency table
		// <Hash table> as a set of <Integer>: <Integer> pairs
		// Total size: 8 * (1 + freqTable.size())
		out.writeInt(numSyms);
		out.writeInt(freqTable.size());
		for (Map.Entry<Integer, Integer> entry : freqTable.entrySet()) {
			out.writeInt(entry.getKey());
			out.writeInt(entry.getValue());
		}
	}
	
	/**
	 * Restore the frequency table from the given stream.
	 * @return restored frequency table
	 * @see DataInputStream
	 */
	public static HFreqTable restore(DataInputStream in) throws IOException {
		try {
			int numSyms, tableSize;

			// we are not trying to be super careful while reading the table,
			// so if it is corrupted really bad things can happen...
			numSyms = in.readInt();
			tableSize = in.readInt();
			LinkedHashMap<Integer, Integer> freqTable = new LinkedHashMap<Integer, Integer>();
			for (int symsRead = 0; symsRead < tableSize; symsRead++)
				freqTable.put(in.readInt(), in.readInt());
			
			return new HFreqTable(freqTable, numSyms);
		} catch (EOFException eoerr) {
			throw new IOException("Malformed frequency table");
		}
	}
	
	/**
	 * Calculate the entropy of the sequence of codes added to the table.
	 * I.e. the number of bits of unique information this sequence of codes
	 * (for example a text) stores.
	 * @return the calculated value of entropy.
	 */
	public double entropy() {
		// p(x[i]) = probability of code x[i]
		// entropy = -SUM from i to n [p(x[i]) * log2(p(x[i]))]
		double entropy = 0.0;
		for (Integer freq : freqTable.values()) {
			double probability = (double)freq / (double)numSyms;
			entropy += probability * (Math.log(probability) / Math.log(2));
		}
		
		return (-1 * entropy);
	}
	
	@Override
	public boolean equals(Object obj) {
		HFreqTable ftbl = (HFreqTable) obj;
		if (numSyms != ftbl.numSyms)
			return false;

		return freqTable.equals(ftbl.freqTable);
	}
}
