package vo;

import java.util.List;

public class CreateQuery extends Query 
{
	public static final String TABLE = "TABLE";
	public static final String INDEX = "INDEX";
	private String objectType;// i'm not creating an enum class! please add one if you would prefer that 
	private List<ColumnDataPair> columnDataPair;
	private String indexName;
	private String indexCol;
	
	public CreateQuery()
	{
		super(CREATE,null);
	}
	public CreateQuery( String tableName, List<ColumnDataPair> columnDataPair) 
	{
		super(CREATE, tableName);
		this.columnDataPair = columnDataPair;
		this.objectType=TABLE;
	}
	public CreateQuery( String indexName, String tableName, String indexCol ) 
	{
		super(CREATE, tableName);
		this.objectType=INDEX;
	}
	public List<ColumnDataPair> getColumnDataPair() 
	{
		return columnDataPair;
	}
	public void setColumnDataPair(List<ColumnDataPair> columnDataPair) 
	{
		this.columnDataPair = columnDataPair;
	}
	public String getObjectType() 
	{
		return objectType;
	}
	public void setObjectType(String objectType) 
	{
		this.objectType = objectType;
	}
	public String getIndexName() 
	{
		return indexName;
	}
	public void setIndexName(String indexName) 
	{
		this.indexName = indexName;
	}
	public String getIndexCol() 
	{
		return indexCol;
	}
	public void setIndexCol(String indexCol) 
	{
		this.indexCol = indexCol;
	}
	@Override
	public void display()
	{
		System.out.println("Create Type: "+objectType);
		System.out.println("Table Name: "+getTableName());
		if(INDEX.equalsIgnoreCase(objectType))
			displayIndex();
		else
			displayTable();
	}
	public void displayIndex()
	{
		System.out.println("Index Name: "+indexName);
		System.out.println("Column Name: "+indexCol);
	}
	public void displayTable()
	{
		System.out.println("Columns:");
		for(ColumnDataPair temp: columnDataPair)
		{
			System.out.println(temp.toString());
		}
	}
}