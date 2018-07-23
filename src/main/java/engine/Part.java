package engine;

public class Part {
	// <p><a href="#n1d" id="n1">1</a> Coup de grace - son darbe bitirici
	// darbe</p> // note
	// <a href="#n1" id="n1d"><sup>[1]</sup></a> // ref

	public static String TYPE_PARAGRAPH = "p";
	public static String TYPE_HEADER = "h2";
	public static String TYPE_HEADER3 = "h3";
	public static String TYPE_SEPERATOR = "sp";
	public static String TYPE_PAGEBREAK = "<div style=\"page-break-after:always;\"></div>";
	public static String TYPE_POEMLINE = "poem";

	public Part() {
		content = "";
	}

	String content;
	String type;

	public String getResult() {
		if (this.type.equals(TYPE_PAGEBREAK))
			return TYPE_PAGEBREAK;
		if (this.type.equals(TYPE_HEADER))
			return TYPE_PAGEBREAK + "\n"
					+ String.format("<%s>%s</%s>", type, content, type);
		if (this.type.equals(TYPE_SEPERATOR))
			return "<p class=seperator>*  *  *</p>";
		if (this.type.equals(TYPE_POEMLINE))
			return "<p class=\"poem\">" + content + "</p>";
		return String.format("<%s>%s</%s>", type, content, type);
	}

	public void capitalize() {
		char c = this.content.charAt(0);
		if (c == 'i')
			c = 'İ';
		else if (c <= 'z' && c >= 'a')
			c -= 32;
		else if (c == 'ö')
			c = 'Ö';
		else if (c == 'ı')
			c = 'I';
		else if (c == 'ü')
			c = 'Ü';
		else if (c == 'ş')
			c = 'Ş';
		else if (c == 'ğ')
			c = 'Ğ';
		else if (c == 'ç')
			c = 'Ç';
		this.content = c + this.content.substring(1, this.content.length());
	}
}
