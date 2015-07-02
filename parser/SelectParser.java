package parser;

import tokenizer.Token;
import tokenizer.Tokenizer;

public class SelectParser 
{
	private final Tokenizer tokenizer;
	private Token ttype;
	public SelectParser()
	{
		tokenizer = Tokenizer.getInstance();
	}
	public boolean parse_select()
	{
		ttype = tokenizer.getToken();
		if(!ttype.equals(Token.SELECT))
			return false;
		if(!select_list())
			return false;
		ttype = tokenizer.getToken();
		if(!ttype.equals(Token.FROM))
			return false;
		if(!from_list())
			return false;
		ttype = tokenizer.getToken();
		if(ttype.equals(Token.WHERE))
		{
			tokenizer.ungetToken();
			if(!where_clause())
				return false;
			ttype = tokenizer.getToken();
			if(!ttype.equals(Token.SEMICOLON))
				return false;
		}
		else if(!ttype.equals(Token.SEMICOLON))
			return false;
		return true;
	}
	public boolean select_list()
	{
		if(!select_opt())
			return false;
		ttype = tokenizer.getToken();
		if(ttype.equals(Token.COMMA))
			return select_list();
		else
			tokenizer.ungetToken();
		return true;
	}
	public boolean select_opt()
	{
		ttype = tokenizer.getToken();
		if(ttype.equals(Token.AREA))
		{
			tokenizer.ungetToken();
			if(!area())
				return false;
		}
		else if(ttype.equals(Token.DISTANCE))
		{
			tokenizer.ungetToken();
			if(!distance())
				return false;
		}
		else if(ttype.equals(Token.INTERSECTION))
		{
			tokenizer.ungetToken();
			if(!intersection())
				return false;
		}
		else if(!ttype.equals(Token.ID))
			return false;
		return true;
	}
	public boolean area()
	{
		ttype = tokenizer.getToken();
		if(!ttype.equals(Token.AREA))
			return false;
		ttype = tokenizer.getToken();
		if(!ttype.equals(Token.LPAREN))
			return false;
		ttype = tokenizer.getToken();
		if(!ttype.equals(Token.ID))
			return false;
		ttype = tokenizer.getToken();
		if(!ttype.equals(Token.COMMA))
			return false;
		ttype = tokenizer.getToken();
		if(!ttype.equals(Token.VAL))
			return false;
		ttype = tokenizer.getToken();
		if(!ttype.equals(Token.RPAREN))
			return false;
		return true;
	}
	public boolean distance()
	{
		ttype = tokenizer.getToken();
		if(!ttype.equals(Token.DISTANCE))
			return false;
		ttype = tokenizer.getToken();
		if(!ttype.equals(Token.LPAREN))
			return false;
		ttype = tokenizer.getToken();
		if(!ttype.equals(Token.ID))
			return false;
		ttype = tokenizer.getToken();
		if(!ttype.equals(Token.COMMA))
			return false;
		ttype = tokenizer.getToken();
		if(!ttype.equals(Token.ID))
			return false;
		ttype = tokenizer.getToken();
		if(!ttype.equals(Token.COMMA))
			return false;
		ttype = tokenizer.getToken();
		if(!ttype.equals(Token.VAL))
			return false;
		ttype = tokenizer.getToken();
		if(!ttype.equals(Token.RPAREN))
			return false;
		return true;
	}
	public boolean intersection()
	{
		ttype = tokenizer.getToken();
		if(!ttype.equals(Token.INTERSECTION))
			return false;
		ttype = tokenizer.getToken();
		if(!ttype.equals(Token.LPAREN))
			return false;
		ttype = tokenizer.getToken();
		if(!ttype.equals(Token.ID))
			return false;
		ttype = tokenizer.getToken();
		if(!ttype.equals(Token.COMMA))
			return false;
		ttype = tokenizer.getToken();
		if(!ttype.equals(Token.ID))
			return false;
		ttype = tokenizer.getToken();
		if(!ttype.equals(Token.COMMA))
			return false;
		ttype = tokenizer.getToken();
		if(!ttype.equals(Token.VAL))
			return false;
		ttype = tokenizer.getToken();
		if(!ttype.equals(Token.RPAREN))
			return false;
		return true;
	}
	public boolean from_list()
	{
		if(!from_id())
			return false;
		ttype = tokenizer.getToken();
		if(ttype.equals(Token.COMMA))
			return from_list();
		else
			tokenizer.ungetToken();
		return true;
	}
	public boolean from_id()
	{
		ttype = tokenizer.getToken();
		if(!ttype.equals(Token.ID))
			return false;
		ttype = tokenizer.getToken();
		if(!ttype.equals(Token.ID))
			return false;
		return true;
	}
	public boolean where_clause()
	{
		ttype = tokenizer.getToken();
		if(!ttype.equals(Token.WHERE))
			return false;
		if(!where_list())
			return false;
		return true;
	}
	public boolean where_list()
	{
		if(!cond())
			return false;
		ttype = tokenizer.getToken();
		if(ttype.equals(Token.AND) || ttype.equals(Token.OR))
			return where_list();
		else
			tokenizer.ungetToken();
		return true;
	}
	public boolean cond()
	{
		ttype = tokenizer.getToken();
		if(!ttype.equals(Token.ID))
			return false;
		if(!rel_op())
			return false;
		ttype = tokenizer.getToken();
		if(!(ttype.equals(Token.ID) || ttype.equals(Token.VAL)))
			return false;
		return true;
	}
	public boolean rel_op()
	{
		ttype = tokenizer.getToken();
		if(!(ttype.equals(Token.EQUAL_TO) || ttype.equals(Token.GREATER) || ttype.equals(Token.LESS)
				|| ttype.equals(Token.GREATER_EQUAL) || ttype.equals(Token.LESS_EQUAL)))
			return false;
		return true;
	}
}