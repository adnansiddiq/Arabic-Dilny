package com.yahia.libs.InternetConnections;


/*i have used this 
 JavaScript tool to generate this java Tool ;)
 * this tool used to convert the arabic UTF-8 encoding To Percent encoding to be used in URLs, Feel free to use this too but u must mention that it's
 * belongs to http://www.mrxprt.com
 */
/**
 * @author MrXprt
 *	Yahia Ragae , http://www.mrxprt.com , mr.xprt@gmail.com
 */



/*
 How to use ?! 
 
 //Creat Object From This Class 
 
	UTFEncoder uEncoder=new UTFEncoder(stringTobeEncoded);
 //and get the Encoded String using
 
	String encoded=uEncoder.encod();
 
 */
public class UTFEncoder {
	 
	
    public UTFEncoder() { 
    }
	
    public String encod(String n) {
        String encoded = n;
		
        char[] c = encoded.toCharArray();
        encoded = "";
        // The position in the Unicode table tells us how many bytes are needed.
        // Note that if we talk about first, second, etc. in the following, we are
        // counting from left to right:
        //
        //   Position in   |  Bytes needed   | Binary representation
        //  Unicode table  |   for UTF-8     |       of UTF-8
        // ----------------------------------------------------------
        //     0 -     127 |    1 byte       | 0XXX.XXXX
        //   128 -    2047 |    2 bytes      | 110X.XXXX 10XX.XXXX
        //  2048 -   65535 |    3 bytes      | 1110.XXXX 10XX.XXXX 10XX.XXXX
        // 65536 - 2097151 |    4 bytes      | 1111.0XXX 10XX.XXXX 10XX.XXXX 10XX.XXXX
		
        for (int x = 0; x < c.length; x++) {
            char charcode = c[x];
            // Position 0 - 127 is equal to percent-encoding with an ASCII character encoding:
            if (charcode < 128) {
                encoded = encoded + gethex(charcode);
				
                //System.out.println("if (charcode < 128) ");
            }
			
            // Position 128 - 2047: two bytes for UTF-8 character encoding.
            if (charcode > 127 && charcode < 2048) {
                // First UTF byte: Mask the first five bits of charcode with binary 110X.XXXX:
                encoded = encoded + gethex((charcode >> 6) | 0xC0);
                // Second UTF byte: Get last six bits of charcode and mask them with binary 10XX.XXXX:
                encoded = encoded + gethex((charcode & 0x3F) | 0x80);
				
                //System.out.println("if (charcode > 127 && charcode < 2048)");
            }
			
            // Position 2048 - 65535: three bytes for UTF-8 character encoding.
            if (charcode > 2047 && charcode < 65536) {
                // First UTF byte: Mask the first four bits of charcode with binary 1110.XXXX:
                encoded = encoded + gethex((charcode >> 12) | 0xE0);
                // Second UTF byte: Get the next six bits of charcode and mask them binary 10XX.XXXX:
                encoded = encoded + gethex(((charcode >> 6) & 0x3F) | 0x80);
                // Third UTF byte: Get the last six bits of charcode and mask them binary 10XX.XXXX:
                encoded = encoded + gethex((charcode & 0x3F) | 0x80);
                //System.out.println("if (charcode > 2047 && charcode < 65536) ");
            }
			
            // Position 65536 - : four bytes for UTF-8 character encoding.
            if (charcode > 65535) {
                // First UTF byte: Mask the first three bits of charcode with binary 1111.0XXX:
                encoded = encoded + gethex((charcode >> 18) | 0xF0);
                // Second UTF byte: Get the next six bits of charcode and mask them binary 10XX.XXXX:
                encoded = encoded + gethex(((charcode >> 12) & 0x3F) | 0x80);
                // Third UTF byte: Get the last six bits of charcode and mask them binary 10XX.XXXX:
                encoded = encoded + gethex(((charcode >> 6) & 0x3F) | 0x80);
                // Fourth UTF byte: Get the last six bits of charcode and mask them binary 10XX.XXXX:
                encoded = encoded + gethex((charcode & 0x3F) | 0x80);
                //System.out.println("if (charcode > 65535) ");
            }
        }
        //System.out.println("Encoded = " + encoded);
        return encoded;
    }
    String hexchars = "0123456789ABCDEFabcdef";
	
    public String gethex(int dec) {
        return "%" + hexchars.charAt(dec >> 4) + hexchars.charAt(dec & 0xF) + "";
    }
}
