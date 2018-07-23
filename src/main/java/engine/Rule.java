package engine;

public class Rule {
	public String from;
	public String to;

	public Rule(String f,String t)
	{
		from=f;
		to=t;
	}
	@Override
	public String toString() {
		return "\""+from+"\"->\""+to+"\"";
	}
}
