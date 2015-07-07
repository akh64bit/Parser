package parser;

import tokenizer.Token;
import tokenizer.Tokenizer;
import vo.InsertQuery;
import vo.ColumnValuePair;
import vo.InsertValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import tests.TableOperations;

public class InsertParser
{
	private final Tokenizer tokenizer;
	private Token ttype;
	private boolean col_list_provided;
	private InsertQuery insertQuery;
	private ArrayList<ColumnValuePair> list;
	private ArrayList<String> colNames;
	private ArrayList<InsertValue> colValues;
        private TableOperations tableOperations;
	public InsertParser()
	{
		tokenizer = Tokenizer.getInstance();
		colNames = new ArrayList<String>();
		colValues = new ArrayList<InsertValue>();
		insertQuery = new InsertQuery();
		col_list_provided = false;
		list = new ArrayList<ColumnValuePair>();
                //tableOperations = new TableOperations();
                tableOperations = TableOperations.getInstance();
	}
	public InsertQuery getQueryValues()
	{
		return insertQuery;
	}
	public void readInput()
	{
		ttype = tokenizer.getToken();
		//System.out.println(tokenizer.getTokenValue());
	}
	public void unget()
	{
		tokenizer.ungetToken();
	}
	public String getTokenVal()
	{
		return tokenizer.getTokenValue();
	}
	public boolean parse_insert()
	{
		readInput();
		if(!ttype.equals(Token.INSERT))
			return false;
		readInput();
		if(!ttype.equals(Token.INTO))
			return false;
		readInput();
		if(!ttype.equals(Token.ID))
			return false;
		insertQuery.setTableName(getTokenVal());
		readInput();
		if(ttype.equals(Token.LPAREN))
		{
			col_list_provided=true;
			if(!col_name_list())
				return false;
			readInput();
			if(!ttype.equals(Token.RPAREN))
				return false;
		}
		else
			tokenizer.ungetToken();
		readInput();
		if(!ttype.equals(Token.VALUES))
			return false;
		readInput();
		if(!ttype.equals(Token.LPAREN))
			return false;
		if(!col_val_list())
			return false;
		readInput();
		if(!ttype.equals(Token.RPAREN))
			return false;
		readInput();
		if(!ttype.equals(Token.SEMICOLON))
			return false;
		if(col_list_provided)
		{
			if(colNames.size() != colValues.size())
				return false;
			Iterator<String> colNIterator = colNames.iterator();
			for(InsertValue temp : colValues)
			{
				ColumnValuePair obj = new ColumnValuePair(colNIterator.next(), temp);
				list.add(obj);
			}
		}
		else
		{
			for(InsertValue temp : colValues)
			{
				ColumnValuePair obj = new ColumnValuePair(null, temp);
				list.add(obj);
			}
		}
		insertQuery.setColumnValuePair(list);
		return true;
	}
	public boolean col_name_list()
	{
		readInput();
		if(!ttype.equals(Token.ID))
			return false;
		colNames.add(getTokenVal());
		readInput();
		if(ttype.equals(Token.COMMA))
			return col_name_list();
		else
			unget();
		return true;
	}
	public boolean col_val_list()
	{
		InsertValue value = new InsertValue();
		readInput();
		boolean flag = true;
		if(ttype.equals(Token.VAL))
		{
			try
			{
				int temp = Integer.parseInt(getTokenVal());
				value.setInteger(temp);
				value.setValueType(InsertValue.INTEGER);
				flag = false;
			}
			catch(Exception e) {/*Not Integer*/}
			if(flag)
			{
				try
				{
					float temp = Float.parseFloat(getTokenVal());
					value.setReal(temp);
					value.setValueType(InsertValue.REAL);
					flag = false;
				}
				catch(Exception e) {/*Not Real*/}
			}
			if(flag)
			{
				if(getTokenVal().matches("^'([0-9]|[A-z])+(.([0-9]|[A-z])+)?'$"))
				{
					value.setString(getTokenVal().replace("'",""));
					value.setValueType(InsertValue.STRING);
					flag = false;
				}
			}
			if(flag)
				return false;
		}
		else if(ttype.equals(Token.SDO_GEOM) || ttype.equals(Token.SDO_GEOMV1))
		{
			value.setValueType(InsertValue.GEOM_OBJ);
			if(!geom_object(value))
				return false;
		}
		else
			return false;
		colValues.add(value);
		readInput();
		if(ttype.equals(Token.COMMA))
			return col_val_list();
		else
			unget();
		return true;
	}
	public boolean geom_object(InsertValue value)
	{
		readInput();
		if(!ttype.equals(Token.LPAREN))
			return false;
		if(!getCoordinates(value))
			return false;
		readInput();
		if(!ttype.equals(Token.RPAREN))
			return false;
		return true;
	}
	public boolean getCoordinates(InsertValue value)
	{
		for(int i=0;i<7;i++)
		{
			readInput();
			if(ttype.equals(Token.VAL))
			{
				try
				{
					value.setCoOrdinates(i, Float.parseFloat(getTokenVal()));
				}
				catch(Exception e)
				{
					return false;
				}
			}
			else
				return false;
			readInput();
			if(!ttype.equals(Token.COMMA))
				return false;
		}
		readInput();
		if(ttype.equals(Token.VAL))
		{
			try
			{
				value.setCoOrdinates(7, Float.parseFloat(getTokenVal()));
			}
			catch(Exception e)
			{
				return false;
			}
		}
		else
			return false;
		return true;
	}
        public boolean insertValues()
        {
            InsertQuery insertQuery = getQueryValues();
            List<ColumnValuePair> listCVP = insertQuery.getColumnValuePair();
            Map<String, String> mapColValPair = new HashMap<String, String>();
            List<String> listVals = new ArrayList<String>();
            for(ColumnValuePair cvp : listCVP)
            {
                String value=""; 
                switch(cvp.getValue().getValueType())
                {
                    case "STRING":
                        value = cvp.getValue().getString();
                        break;
                    case "INTEGER":
                        value = String.valueOf(cvp.getValue().getInteger());
                        break;
                    case "REAL":
                        value = String.valueOf(cvp.getValue().getReal());
                        break;
                    case "GEOM_OBJ":
                        value="";
                        int i;
                        for(i=0;i<7;i++)
                        {
                            value+=String.valueOf(cvp.getValue().getCoOrdinates(i));
                            value+=",";
                        }
                        value+=String.valueOf(cvp.getValue().getCoOrdinates(i));
                        break;
                }
                if(cvp.getColumnName() == null)
                    listVals.add(value); 
                else
                    mapColValPair.put(cvp.getColumnName(), value);
            }  
            boolean retVal=false;
            if(listVals.size()>0)
                retVal=tableOperations.insertIntoTable(insertQuery.getTableName(), listVals);
            else if(mapColValPair.size()>0)
                retVal=tableOperations.insertIntoTable(insertQuery.getTableName(), mapColValPair);
            
            return retVal;

        }
	public static void main(String[] args) 
	{
		InsertParser parser = new InsertParser();
		boolean result = parser.parse_insert();
		System.out.println(result?"Syntax Correct":"Syntax Error");
		if(result)
			parser.insertQuery.display();
	}
}