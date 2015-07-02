package parser;

import tokenizer.Token;
import tokenizer.Tokenizer;

//Comment Entry
public class CreateParser 
{
	private final Tokenizer tokenizer;
	private Token ttype;
	public CreateParser()
	{
		tokenizer = Tokenizer.getInstance();
	}
        
	
        public boolean parse_create()
	{
		ttype = tokenizer.getToken();
		if(!ttype.equals(Token.CREATE))
			return false;
                ttype = tokenizer.getToken();
                if(!ttype.equals(Token.TABLE))
			return false;
                ttype = tokenizer.getToken();
                if(!ttype.equals(Token.ID))
			return false;
                ttype = tokenizer.getToken();
                if(!ttype.equals(Token.LPAREN))
			return false;
		if(!col_decl_list())
			return false;
		ttype = tokenizer.getToken();
		if(!ttype.equals(Token.RPAREN))
			return false;                
                
		return true;
	}
	public boolean col_decl_list()
	{
		ttype = tokenizer.getToken();
		if(ttype.equals(Token.ID))
                {
                    ttype = tokenizer.getToken();
                    if(ttype.equals(Token.INTEGER) || ttype.equals(Token.REAL) || ttype.equals(Token.VARCHAR) || ttype.equals(Token.SDO_GEOM))
                    {
                        ttype = tokenizer.getToken();
                        if(ttype.equals(Token.COMMA))
                        {
                            if(!col_decl_list())
                                return false;
                        }
                        else
                            tokenizer.ungetToken();
                        return true;
                    }
                    else
                        return false;
                }
		else
                    return false;
		///return true;
	}
}
