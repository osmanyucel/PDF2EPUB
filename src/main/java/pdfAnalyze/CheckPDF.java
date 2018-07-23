package pdfAnalyze;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Vector;

import org.apache.pdfbox.pdmodel.PDDocument;

import FileOperations.WriteFile;

public class CheckPDF {
	public static void main(String[] args) throws IOException {
		Vector<Book> vb = new Vector<Book>();
		String folder = "Z:\\Book\\ATDB\\";
		File ff = new File(folder);
		String[] files = ff.list();
		for (String s : files) {
			try {
			
				File f = new File(folder + s);
			PDDocument doc = PDDocument.load(f);
			System.out.println(folder + s);
			int count = doc.getNumberOfPages();
			vb.add(new Book(s, count,f.length()));
			doc.close();
			System.out.println(count);
			
			} catch (Exception e) {
				vb.add(new Book(s, 0,0));
				e.printStackTrace();
			}
		}
		
		Collections.sort(vb);
		String s = "";
		for(Book b : vb)
		{
			System.out.println(b);
			s+=b+"\n";
		}
		WriteFile.write("books", s);
	}
}
