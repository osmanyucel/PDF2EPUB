package pdfAnalyze;

public class Book implements Comparable<Book> {
	int pageCount;
	String name;
	long fileSize;

	public Book(String name, int page, long fs) {
		this.name = name;
		this.pageCount = page;
		this.fileSize = fs;
	}

	@Override
	public int compareTo(Book o) {
		return Integer.compare(this.pageCount, o.pageCount);
	}

	@Override
	public String toString() {

		return this.pageCount + "\t" + this.name+ "\t" + this.fileSize ;
	}

}
