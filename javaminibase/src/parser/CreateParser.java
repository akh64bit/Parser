package parser;

import tokenizer.Token;
import tokenizer.Tokenizer;
import vo.CreateQuery;
import vo.ColumnDataPair;
import vo.DataType;
import java.util.ArrayList;
import java.util.List;
import tests.TableOperations;

public class CreateParser
{
	private final Tokenizer tokenizer;
	private Token ttype;
	private CreateQuery createQuery;
	private ArrayList<ColumnDataPair> list;
	private TableOperations tableOperations;
	public CreateQuery getQueryValues()
	{
		return createQuery;
	}
	public CreateParser()
	{
		tokenizer = Tokenizer.getInstance();
		createQuery = new CreateQuery();
		//tableOperations = new TableOperations();
		tableOperations = TableOperations.getInstance();
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

	public ArrayList<ColumnDataPair> getColTypeList()
	{
		return list;
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
		createQuery.setObjectType(CreateQuery.TABLE);
		list = new ArrayList<ColumnDataPair>();
		createQuery.setColumnDataPair(list);
		readInput();
		if(!ttype.equals(Token.ID))
			return false;
		createQuery.setTableName(getTokenVal());
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
		ColumnDataPair columnDataPair = new ColumnDataPair(new DataType(), getTokenVal());// don't have the type yet! will be set in the type method!!
		return type(columnDataPair);
	}
	public boolean type(ColumnDataPair columnDataPair)
	{
		readInput();
		DataType dt = columnDataPair.getDataType();
		if(ttype.equals(Token.VARCHAR))
		{
			unget();
			dt.setName(DataType.VARCHAR);
			if(!varchar(dt))
				return false;
		}
		else if(ttype.equals(Token.INTEGER))
			dt.setName(DataType.INTEGER);
		else if(ttype.equals(Token.REAL))
			dt.setName(DataType.REAL);
		else if(ttype.equals(Token.SDO_GEOM) || ttype.equals(Token.SDO_GEOMV1))
			dt.setName(DataType.SDO_GEOM);
		else
			return false;
		list.add(columnDataPair);
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
			try
			{
				dt.setSize(Integer.parseInt(getTokenVal()));
			}
			catch(Exception e)
			{
				return false;
			}
		}
		readInput();
		if(!ttype.equals(Token.RPAREN))
			return false;
		return true;
	}
	public boolean parse_index()
	{
		createQuery.setObjectType(CreateQuery.INDEX);
		readInput();
		if(!ttype.equals(Token.ID))
			return false;
		createQuery.setIndexName(getTokenVal());
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
		createQuery.setTableName(getTokenVal());
		readInput();
		if(!ttype.equals(Token.LPAREN))
			return false;
		readInput();
		if(!ttype.equals(Token.ID))
			return false;
		createQuery.setIndexCol(getTokenVal());
		readInput();
		if(!ttype.equals(Token.RPAREN))
			return false;
		return true;
	}
	public boolean createTable()
	{
		CreateQuery createQuery = getQueryValues();
		List<ColumnDataPair> list = createQuery.getColumnDataPair();
		List<String> colNames = new ArrayList<String>();
		List<String> colTypes = new ArrayList<String>();
		for(ColumnDataPair cdp : list)
		{
			colNames.add(cdp.getColumnName());
			colTypes.add(cdp.getDataType().getName());
		}
		return tableOperations.createTable(createQuery.getTableName(), colNames, colTypes);
	}
	public static void main(String[] args)
	{
		CreateParser parser = new CreateParser();
		boolean result = parser.parse_create();
		System.out.println(result?"Correct Syntax":"Syntax Error");
		if(result)
			parser.createQuery.display();
	}
}