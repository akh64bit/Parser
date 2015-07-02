package parser;

import tokenizer.Token;
import tokenizer.Tokenizer;

public class InsertParser 
{
	private final Tokenizer tokenizer;
	private Token ttype;
        private int col_count;
        private int col_val_count;
        private boolean col_list_provided;
	public InsertParser()
	{
		tokenizer = Tokenizer.getInstance();
	}
	public boolean parse_insert()
	{
		ttype = tokenizer.getToken();
		if(!ttype.equals(Token.INSERT))
			return false;
                ttype = tokenizer.getToken();
		if(!ttype.equals(Token.INTO))
			return false;
                ttype = tokenizer.getToken();
		if(!ttype.equals(Token.ID))
			return false;
                ttype = tokenizer.getToken();
                if(ttype.equals(Token.LPAREN))
                {
                    col_count=0;
                    col_list_provided=true;
                    if(!col_name_list())
                        return false;
                    ttype = tokenizer.getToken();
                    if(!ttype.equals(Token.RPAREN))
                        return false;
                }
                else
                    tokenizer.ungetToken();
		ttype = tokenizer.getToken();
		if(!ttype.equals(Token.VALUES))
                    return false;
                
                ttype = tokenizer.getToken();
                if(!ttype.equals(Token.LPAREN))
                    return false;
		if(!col_val_list())
                {
                    if(col_list_provided)
                        col_list_provided=false;
                    return false;        
                }
                if(col_list_provided)
                    col_list_provided=false;
                
                ttype = tokenizer.getToken();
                if(!ttype.equals(Token.RPAREN))
                    return false;
		return true;
	}
	public boolean col_name_list()
	{
		
                ttype = tokenizer.getToken();
		if(ttype.equals(Token.ID))
                {
                    col_count++;
                    ttype = tokenizer.getToken();
                    if(ttype.equals(Token.COMMA))
                    {    
                        if(!col_name_list())
                            return false;
                    }
                    else 
                        tokenizer.ungetToken();      
                }
                else
                    return false;
		return true;
	}
        public boolean col_val_list()
	{	
            ttype = tokenizer.getToken();
		if(ttype.equals(Token.VAL))
                {
                    col_val_count++;
                    if (col_list_provided && col_val_count>col_count)
                    {
                        System.out.println("Number of values in the value list don't match the number in the column list");
                        return false;
                    }
                    ttype = tokenizer.getToken();
                    if(ttype.equals(Token.COMMA))
                    {    
                        if(!col_val_list())
                            return false;
                    }
                    else 
                    {
                        tokenizer.ungetToken();
                        if (col_list_provided && col_count != col_val_count)
                        {
                            System.out.println("Number of values in the value list don't match the number in the column list");
                            return false;
                        }
                    }
                }
                else
                    return false;
		return true;
	}
        
}