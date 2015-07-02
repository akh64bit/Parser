package parser;

import tokenizer.Token;
import tokenizer.Tokenizer;

public class CreateParser 
{
	private final Tokenizer tokenizer;
	private Token ttype;
	public CreateParser()
	{
		tokenizer = Tokenizer.getInstance();
	}
	public void readInput()
	{
		ttype = tokenizer.getToken();
//		System.out.println(tokenizer.getTokenValue());
	}
	public void unget()
	{
		tokenizer.ungetToken();
	}
	public boolean parse_create()
	{
		readInput();
		if(!ttype.equals(Token.CREATE))
			return false;
		if(!choice())
			return false;
		readInput();
		if(!ttype.equals(Token.SEMICOLON))
			return false;
		return true;
	}
	public boolean choice()
	{
		readInput();
		if(ttype.equals(Token.TABLE))
			return parse_table();
		else if(ttype.equals(Token.INDEX))
			return parse_index();
		return false;
	}
	public boolean parse_table()
	{
		readInput();
		if(!ttype.equals(Token.ID))
			return false;
		readInput();
		if(!ttype.equals(Token.LPAREN))
			return false;
		if(!col_list())
			return false;
		readInput();
		if(!ttype.equals(Token.RPAREN))
			return false;
		return true;
	}
	public boolean col_list()
	{
		if(!pair())
			return false;
		readInput();
		if(ttype.equals(Token.COMMA))
			return col_list();
		else
			unget();
		return true;
	}
	public boolean pair()
	{
		readInput();
		if(!ttype.equals(Token.ID))
			return false;
		return type();
	}
	public boolean type()
	{
		readInput();
		if(ttype.equals(Token.VARCHAR))
		{
			unget();
			return varchar();
		}
		else if(!(ttype.equals(Token.INTEGER) || ttype.equals(Token.REAL) || ttype.equals(Token.SDO_GEOMV1)))
			return false;
		return true;
	}
	public boolean varchar()
	{
		readInput();
		if(!ttype.equals(Token.VARCHAR))
			return false;
		readInput();
		if(!ttype.equals(Token.LPAREN))
			return false;
		readInput();
		if(!ttype.equals(Token.VAL))
			return false;
		readInput();
		if(!ttype.equals(Token.RPAREN))
			return false;
		return true;
	}
	public boolean parse_index()
	{
		readInput();
		if(!ttype.equals(Token.ID))
			return false;
		readInput();
		if(!ttype.equals(Token.ON))
			return false;
		if(!index_column())
			return false;
		readInput();
		if(!ttype.equals(Token.INDEXTYPE))
			return false;
		readInput();
		if(!ttype.equals(Token.IS))
			return false;
		readInput();
		if(!ttype.equals(Token.SPATIAL_INDEX))
			return false;
		return true;
	}
	public boolean index_column()
	{
		readInput();
		if(!ttype.equals(Token.ID))
			return false;
		readInput();
		if(!ttype.equals(Token.LPAREN))
			return false;
		readInput();
		if(!ttype.equals(Token.ID))
			return false;
		readInput();
		if(!ttype.equals(Token.RPAREN))
			return false;
		return true;
	}
	public static void main(String[] args) 
	{
		CreateParser parser = new CreateParser();
		System.out.println(parser.parse_create());
	}
}