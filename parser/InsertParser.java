package parser;

import tokenizer.Token;
import tokenizer.Tokenizer;
import vo.InsertQuery;
import vo.ColumnValuePair;
import java.util.ArrayList;

public class InsertParser
{
	private final Tokenizer tokenizer;
	private Token ttype;
        private int col_count;
        private int col_val_count;
        private boolean col_list_provided;
        private InsertQuery insertQuery;
        private ArrayList<ColumnValuePair> list;
        private ArrayList<String> colNames;
        private ArrayList<String> colValues;
	public InsertParser()
	{
		tokenizer = Tokenizer.getInstance();
                colNames = new ArrayList<>();
                colValues = new ArrayList<>();

	}
        public void resetParser()
        {
            col_count=0;
            col_val_count=0;
            col_list_provided=false;
            insertQuery=null;
            list.clear();

        }
        public String getTokenVal()
        {
            return tokenizer.getTokenValue();
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
                else
                {
                    insertQuery.setTableName(getTokenVal());
                }
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

                for(int i = 0; i < colValues.size(); i++)
                {
                    if(colNames.size()>0)
                        list.add(new ColumnValuePair(colNames.get(i), colValues.get(i)));
                    else
                        list.add(new ColumnValuePair(null , colValues.get(i)));
                }
                insertQuery.setColumnValuePair(list);

                System.out.println("Table:"+insertQuery.getTableName());
                System.out.println("Column Value:");

                for(ColumnValuePair cvp:insertQuery.getColumnValuePair())
                    System.out.println(cvp.getColumnName()+":"+cvp.getValue());

		return true;
	}
	public boolean col_name_list()
	{

                ttype = tokenizer.getToken();
		if(ttype.equals(Token.ID))
                {
                    col_count++;
                    colNames.add(getTokenVal());
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
                    colValues.add(getTokenVal());
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
