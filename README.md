huffman
=======

This is a huffman encoder, huffman decoder, huffman input and output streams written in java. Nothing special, this project
has purely educational purpose. Not a rocket science, but I hope somebody will find it useful.

Just to remind what huffman coding is: it's an entropy encoding algorithm for loseless data compression.


HuffmanDemo
=======
HuffmanDemo program demonstrates the basic principles of usage of Huffman input and output streams (see its source code for
more details):

    % java -classpath . org.dk.huffman.HuffmanDemo
    USAGE: HuffmanDemo enc|dec|entropy
       enc <input-file> <output-file>: encode input file and save
                        the results to output file
       dec <input-file> <output-file>: decode input file and save
                        the results to output file
       entropy <input-file>: calculate an entropy of the symbols in input file
       
I downloaded a magnificent Charles Dickens book "A tale of two cities" from the project gutenberg (http://www.gutenberg.org/ebooks/98)
to demonstrate how HuffmanDemo works (Think about it as a little compensation of lack of classic literature in computer science...)

So, as you can see from the program output above, HuffmanDemo can do several things:

Entropy
-------

It can calculate an entropy rate of the data we're going to compress (http://en.wikipedia.org/wiki/Entropy_(information_theory)).
In other words, the measurement of randomntess of the data or an optimal number of bits required to store one symbol of the data.
Note that loseless compression algorithm can not compress data into more than entropy rate multiplied by the length of data bits.

Obviously, an english text (A tale of two cities) has low entropy => it can be compressed quite efficiently. According to what
HuffmanDemo says, it can be compressed almost up to 50% of its original size.

    % java -classpath bin org.dk.huffman.HuffmanDemo entropy pg98.txt
    Entropy: 4.54
    
On the other hand, digital images and texts which are mostly already compressed have high entropy => very low compression rate:

    % java -classpath bin org.dk.huffman.HuffmanDemo entropy pg98.txt.gz
    Entropy: 8.00
    
    % java -classpath bin org.dk.huffman.HuffmanDemo entropy photo.jpeg
    Entropy: 7.87


Compression and Decompression
-----------------------------

Compression:

    % java -classpath bin org.dk.huffman.HuffmanDemo enc pg98.txt pg98.huffman
    Compression: done
    Original file size:     792927
    Compressed file size:   455349
    Compression efficiency: 42.57%

And decompression:

    % java -classpath bin org.dk.huffman.HuffmanDemo dec pg98.huffman pg98.dec.txt
    Decompression: done
    Original file size:     455349
    Decompressed file size: 792927
    
    % diff -Naur pg98.txt pg98.dec.txt
    % echo $?
    0

Huffman input and output streams
=======

Huffman input and output streams perform transparent data encoding and decoding using Huffman algorithm. Basically they work in
almost the same way as ZipInputStream and ZipOutputStream. Easy peasy:

```java
// Encoding
InputStream in = new FileInputStream(inFile);
HuffmanOutputStream hout = new HuffmanOutputStream(new FileOutputStream(outFile));
byte buf[] = new byte[4096];
int len;

while ((len = in.read(buf)) != -1)
  hout.write(buf, 0, len);

in.close();
hout.close();
```

```java
// Decoding
HuffmanInputStream hin = new HuffmanInputStream(new FileInputStream(inFile));
OutputStream out = new FileOutputStream(outFile);
byte buf[] = new byte[4096];
int len;

while ((len = hin.read(buf)) != -1)
  out.write(buf, 0, len);

hin.close();
out.close();
```

If you don't need streams or if you want to encode/decode, say, integers instead of bytes, feel free to use lower level abstractions.
Namely, HuffmanEncoder and HuffmanDecoder. To see how to use them, see the source code of HuffmanOutputStream and HuffmanInputStream
respectively.
