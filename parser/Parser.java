package parser;

import tokenizer.Token;
import tokenizer.Tokenizer;

public class Parser 
{
	private static CreateParser createParser;
	private static InsertParser insertParser;
	private static SelectParser selectParser;
	private static Tokenizer tokenizer;
	private static Token ttype;
	public Parser()
	{
		tokenizer = Tokenizer.getInstance();
	}
	public boolean parse_sql()
	{
		ttype = tokenizer.getToken();
		switch(ttype)
		{
		case CREATE:
			tokenizer.ungetToken();
			createParser = new CreateParser();
			return createParser.parse_create();
		case INSERT:
			tokenizer.ungetToken();
			insertParser = new InsertParser();
			return insertParser.parse_insert();
		case SELECT:
			tokenizer.ungetToken();
			selectParser = new SelectParser();
			return selectParser.parse_select();
		default:
			System.out.println("Invalid query: Supported queries are CREATE TABLE, SELECT, INSERT.");
			return false;
		}
	}
	public static void main(String[] args) 
	{
		System.out.println(new Parser().parse_sql());
	}
}