package engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Scanner;
import java.util.Vector;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import FileOperations.WriteFile;

public class Functions {

	public static Vector<String> readLinesFromPDF(String fileName) throws Exception {
		Vector<String> parts = new Vector<String>();
		File input = new File(fileName);
		PDDocument pd = PDDocument.load(input);
		PDFTextStripper stripper = new PDFTextStripper();
		System.out.println(stripper.getSpacingTolerance());
		stripper.setSpacingTolerance((float) Float.MAX_VALUE);
		String s = stripper.getText(pd);
		pd.close();
		String[] p = s.split("\n");

		for (String st : p) {
			st = st.replaceAll("\r", "");
			st = st.replaceAll("−", "-");
			st = st.replaceAll("—", "-");
			st = st.replaceAll(" [.]", ".");
			st = st.replaceAll("[.]", ". ");
			st = st.replaceAll("«", "“");
			st = st.replaceAll("»", "”");
			while (st.contains(".  "))
				st = st.replaceAll("[.]  ", ". ");
			parts.add(st);
		}

		System.out.println("Read Parts");
		return parts;
	}

	public static void readRules() throws Exception {
		Data.rules = new Vector<Rule>();
		Reader reader = new InputStreamReader(new FileInputStream("/home/osmanyucel/Desktop/workspace/PDF2EPUB/src/main/resources/rules.p2e"), "UTF-8");
		BufferedReader fin = new BufferedReader(reader);
		String s;
		while ((s = fin.readLine()) != null) {
			System.out.println(s);
			String r = s;
			String f = r.split("-->>")[0];
			String t = r.split("-->>")[1];
			Rule rule = new Rule(f, t);
			Data.rules.add(rule);
		}
		fin.close();
	}

	public static String applyRules(String s) {
		for (Rule r : Data.rules) {
			s = s.replaceAll(r.from, r.to);
		}
		return s;
	}

	public static void initialize() throws Exception {
		readRules();
		readPageSides();
		readAllowedChars();
	}

	private static void readAllowedChars() {
		Data.allowedChars = new Vector<Character>();
		for (char c = 'a'; c <= 'z'; c++) {
			Data.allowedChars.add(c);
		}
		for (char c = 'A'; c <= 'Z'; c++) {
			Data.allowedChars.add(c);
		}
		for (char c = '0'; c <= '9'; c++) {
			Data.allowedChars.add(c);
		}
		Data.allowedChars.add('(');
		Data.allowedChars.add(')');
		Data.allowedChars.add('-');
		Data.allowedChars.add(':');

		Data.allowedChars.add('İ');
		Data.allowedChars.add(' ');
		Data.allowedChars.add('ö');
		Data.allowedChars.add(',');
		Data.allowedChars.add('Ö');
		Data.allowedChars.add('ç');
		Data.allowedChars.add('ı');
		Data.allowedChars.add('ü');
		Data.allowedChars.add('−');
		Data.allowedChars.add('Ş');
		Data.allowedChars.add('.');
		Data.allowedChars.add('ş');
		Data.allowedChars.add('/');
		Data.allowedChars.add('ğ');
		Data.allowedChars.add('"');
		Data.allowedChars.add('\'');
		Data.allowedChars.add('â');
		Data.allowedChars.add('*');
		Data.allowedChars.add(';');
		Data.allowedChars.add('î');
		Data.allowedChars.add('Ç');
		Data.allowedChars.add('?');
		Data.allowedChars.add('%');
		Data.allowedChars.add('Ü');
		Data.allowedChars.add('!');
		Data.allowedChars.add('û');
		Data.allowedChars.add('Î');
		Data.allowedChars.add('Â');
		Data.allowedChars.add('Ğ');
		Data.allowedChars.add('~');
	}

	private static void readPageSides() throws Exception {
		Data.pageSide = new Vector<String>();
		Reader reader = new InputStreamReader(new FileInputStream("/home/osmanyucel/Desktop/workspace/PDF2EPUB/src/main/resources/pageSides.p2e"), "UTF-8");
		BufferedReader fin = new BufferedReader(reader);
		String s;
		while ((s = fin.readLine()) != null) {
			System.out.println(s);
			Data.pageSide.add(s);
		}
		fin.close();
	}

	public static boolean isHeader(String s) {
		s = s.replaceAll("ve", "");
		s = s.replaceAll("Ve", "");
		if (s.contains("Bölüm") || s.contains("bölüm") || s.contains("BÖLÜM") || s.contains("KISIM"))
			return true;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) <= 'z' && s.charAt(i) >= 'a') {
				return false;
			}
		}
		return true;
	}

	public static boolean isPageNumber(String s) {

		for (String st : Data.pageSide) {
			s = s.replaceAll(st, "");
		}
		return isNumber(s.replaceAll(" ", ""));
	}

	private static boolean isNumber(String s) {
		try {
			Integer.valueOf(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isEmptyLine(String s) {

		s = s.replaceAll(" ", "");
		s = s.replaceAll("	", "");
		return s.equals("");
	}

	public static boolean isWeirdLines(String s) {

		double normalCount = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c <= 'Z' && c >= 'A')
				normalCount++;
			if (c <= 'z' && c >= 'a')
				normalCount++;
			if (c <= '9' && c >= '0')
				normalCount++;
		}
		return normalCount / s.length() < 0.5;
	}

	public static void saveResults(String folder) {
		// TODO Auto-generated method stub
		Vector<Part> parts = combineLines();
		parts = postProcess(parts);
		prepareHTMLinFolder(folder, parts);
	}

	public static Vector<Part> postProcess(Vector<Part> parts) {
		Vector<Part> editedParts = new Vector<Part>();
		for (Part p : parts) {
			editedParts.add(postProcessPart(p));
		}
		return editedParts;
	}

	public static Part postProcessPart(Part p) {
		p.content = p.content.replaceAll(" [.]", ".");
		p.content = p.content.replaceAll(" ”", "”");
		return p;
	}

	public static Vector<Part> combineLines() {
		boolean lastHeader = false;
		boolean cut = false;
		Vector<Part> partList = new Vector<Part>();
		Part p = new Part();
		p.type = Part.TYPE_PARAGRAPH;
		for (int i = 0; i < Data.lines.size(); i++) {

			String st = Data.lines.get(i);
			System.out.println(st);
			st = st.trim();
			if (st.equals("-SePeRaToR-")) {
				partList.add(p);
				p = new Part();
				p.type = Part.TYPE_SEPERATOR;
				partList.add(p);
				p = new Part();
				p.type = Part.TYPE_PARAGRAPH;
				continue;
			}

			if (st.endsWith("" + (char) 45) || st.endsWith("" + (char) 173)) {
				st = st.substring(0, st.length() - 1);
				cut = true;
			}
			boolean header = Data.headerIndexes.contains(i);
			if (header && lastHeader) {
				p.content += st;
				continue;
			} else if (header && !lastHeader) {
				lastHeader = true;
				partList.add(p);
				p = new Part();
				p.type = Part.TYPE_HEADER;
			} else if (isNewParagraph(p, st) || lastHeader) {
				lastHeader = false;
				partList.add(p);

				p = new Part();
				p.type = Part.TYPE_PARAGRAPH;
			}
			p.content += st;
			if (Data.SIIR) {
				p.capitalize();
			}
			if (!cut)
				p.content += " ";
			cut = false;

		}
		if (Data.SIIR) {
			p.capitalize();
		}
		partList.add(p);
		return partList;
	}

	public static Vector<String> readLinesFromTXT(String fileName) throws Exception {
		Vector<String> parts = new Vector<String>();
		File input = new File(fileName); // The PDF file from where you would
											// like to extract
		Scanner s = new Scanner(input);
		while (s.hasNextLine()) {
			String st = s.nextLine();
			System.out.println(st);
			if (st.startsWith("---"))
				continue;
			if (st.contains("ABC Amber"))
				continue;
			parts.add(applyRules(st.trim()));
		}
		System.out.println("Read Parts");
		return parts;
	}

	private static boolean isNewParagraph(Part p, String st) {
		if (Data.SIIR) {
			if (p.type.equals(Part.TYPE_PARAGRAPH))
				p.type = p.TYPE_POEMLINE;
			return true;
		}
		if (st.startsWith("-"))
			return true;
		if (st.length() == 0)
			return false;
		else if (st.charAt(0) <= 'z' && st.charAt(0) >= 'a') {
			return false;
		} else if (p.content.trim().endsWith(".") || p.content.trim().endsWith("?") || p.content.trim().endsWith("!")
				|| p.content.trim().endsWith("”")) {
			return true;
		} else if (p.content.trim().endsWith("\"")) {
			return true;
		} else {
			return false;
		}
	}

	public static void prepareHTMLinFolder(String folder, Vector<Part> parts) {
		File f = new File(folder.replaceAll(" ", ""));
		f.mkdir();
		String s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:epub=\"http://www.idpf.org/2007/ops\" xml:lang=\"tr\">\r\n"
				+ "<head>\r\n" + " <link href=\"stylesheet.css\" rel=\"stylesheet\" type=\"text/css\"/>\r\n"
				+ "<link href=\"page_styles.css\" rel=\"stylesheet\" type=\"text/css\"/>\r\n" + "<title>"
				+ Data.bookName + "</title>" + "</head>\r\n" + "<body>";
		int i = 1;
		int size = 0;
		for (Part p : parts) {
			// SpellChecker.checkSpelling(p);
			p.content = p.content.trim();
			if (size > 100000 && p.type == Part.TYPE_HEADER) {
				s += "</body>\r\n</html>";

				WriteFile.write(folder + "\\result" + i + ".htm", s);
				s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
						+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:epub=\"http://www.idpf.org/2007/ops\" xml:lang=\"tr\">\r\n"
						+ "<head>\r\n" + " <link href=\"stylesheet.css\" rel=\"stylesheet\" type=\"text/css\"/>\r\n"
						+ "<link href=\"page_styles.css\" rel=\"stylesheet\" type=\"text/css\"/>\r\n" + "</head>\r\n"
						+ "<body>";
				i++;
				size = 0;
			}

			s += p.getResult() + "\n";
			size += p.content.length();
		}
		s += "</body>\r\n" + "</html>";
		s = s.replaceAll(" <", "<");
		WriteFile.write(folder + "\\result" + i + ".htm", s);
	}

}
