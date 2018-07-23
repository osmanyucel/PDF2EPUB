package engine;

import java.util.Vector;

public class Data {

	public static boolean SIIR = false;
	public static boolean isFileSelected = false;
	public static boolean isFolderSelected = false;
	public static String pdfAddress="";
	public static String resultAddress="";
	
	public static Vector<Rule> rules;
	public static Vector<String> pageSide;
	
	public static Vector<Character> allowedChars;
	
	public static Vector<String> lines;
	
	public static Vector<Integer> headerIndexes;
	public static Vector<Integer> removeIndexes;
	
	public static String bookName;
	
}
