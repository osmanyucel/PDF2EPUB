public class Note
{
	int id;
	String note;
	String fileName;

	public Note(int id,String note, String fileName)
	{
		this.id = id;
		this.note=note;
		this.fileName = fileName;
	}

	public String getRef()
	{
		return String.format("<a href=\"notes.htm#n%d\" id=\"n%dd\"><sup>[%d]</sup></a>", id,id,id);
	}
	public String getNote()
	{
		return String.format("<p><a href=\""+fileName+"#n%dd\" id=\"n%d\">%d</a> %s</p>",id,id,id,note);
	}
	@Override
	public String toString()
	{
		// TODO Auto-generated method stub
		return String.format("%3d %s",id,note);
	}
}
