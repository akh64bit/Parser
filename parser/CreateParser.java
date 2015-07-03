package parser;

import tokenizer.Token;
import tokenizer.Tokenizer;
import vo.CreateQuery;
import vo.Query;
import vo.ColumnDataPair;
import vo.DataType;
import vo.DataTypeName;
import java.util.ArrayList; 


public class CreateParser 
{
	private final Tokenizer tokenizer;
	private Token ttype;
        private CreateQuery createQuery;
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
        public String getTokenVal()
        {
            return tokenizer.getTokenValue();
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
                System.out.println("Table :" + createQuery.getTableName());
                createQuery.getColumnDataPair();
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
                else
                    createQuery = new CreateQuery(getTokenVal(), new ArrayList<ColumnDataPair>());
		readInput();
		if(!ttype.equals(Token.LPAREN))
			return false;
                ArrayList<ColumnDataPair> list = new ArrayList<ColumnDataPair>();
		if(!col_list(list))
                    return false;
		readInput();
		if(!ttype.equals(Token.RPAREN))
			return false;
		return true;
                
	}
	public boolean col_list(ArrayList<ColumnDataPair> list)
	{
            ColumnDataPair columnDataPair = null;
            if(!pair(columnDataPair))
		return false;
            else
            {
                list.add(columnDataPair);
            }
            readInput();
            if(ttype.equals(Token.COMMA))
		return col_list(list);
            else
		unget();
            return true;
	}
	public boolean pair(ColumnDataPair columnDataPair)
	{
		readInput();
                
		if(!ttype.equals(Token.ID))
			return false;
                
                columnDataPair = new ColumnDataPair( null, getTokenVal());// don't have the type yet! will be set in the type method!!
		return type(columnDataPair);
	}
	public boolean type(ColumnDataPair columnDataPair)
	{
		readInput();
                
                DataType dt = new DataType();                        
                columnDataPair.setDataType(dt);                
                
		if(ttype.equals(Token.VARCHAR))
		{
			unget();     
                        dt.setName(DataTypeName.VARCHAR);
			return varchar(dt);
		}
		else if(!(ttype.equals(Token.INTEGER) || ttype.equals(Token.REAL) || ttype.equals(Token.SDO_GEOMV1)))
			return false;
                else
                {
                    if(ttype.equals(Token.INTEGER))
                        dt.setName(DataTypeName.INTEGER);
                    else if(ttype.equals(Token.REAL))
                        dt.setName(DataTypeName.REAL);
                    else if(ttype.equals(Token.SDO_GEOMV1))
                        dt.setName(DataTypeName.SDO_GEOM);
                }
		return true;
	}
	public boolean varchar(DataType dt)
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
                else
                {
                    int sz = Integer.parseInt(getTokenVal());
                    dt.setSize(sz);
                }
		readInput();
		if(!ttype.equals(Token.RPAREN))
			return false;
		return true;
	}
	public boolean parse_index()
	{
		readInput();
                String indexName;
		if(!ttype.equals(Token.ID))
			return false;
                else
                    indexName=getTokenVal();
		readInput();
		if(!ttype.equals(Token.ON))
			return false;
		if(!index_column(indexName))
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
	public boolean index_column(String indexName)
	{
		readInput();
                String tableName;
		if(!ttype.equals(Token.ID))
			return false;
                else
                    tableName=getTokenVal();
		readInput();
		if(!ttype.equals(Token.LPAREN))
			return false;
		readInput();
		if(!ttype.equals(Token.ID))
			return false;
                else
                    createQuery = new CreateQuery(indexName, tableName, getTokenVal());
                
		readInput();
		if(!ttype.equals(Token.RPAREN))
			return false;
		return true;
	}
	public static void main(String[] args) 
	{
		CreateParser parser = new CreateParser();
		System.out.println(parser.parse_create());
                System.out.println();
	}
}