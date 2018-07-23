import java.io.File;
import java.util.Vector;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class ExtractImages {
	public static void main(String[] args) throws Exception{
		Vector<String> parts = new Vector<String>();
		File input = new File("Z:\\Book\\ATDB\\sapiens.pdf");
		PDDocument pd = PDDocument.load(input);
		PDFTextStripper stripper = new PDFTextStripper();
		System.out.println(stripper.getSpacingTolerance());
		stripper.setSpacingTolerance((float) Float.MAX_VALUE);
		String s = stripper.getText(pd);
		pd.close();

	}
}
