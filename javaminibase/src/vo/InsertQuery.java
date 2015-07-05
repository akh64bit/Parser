package vo;

import java.util.List;

public class InsertQuery extends Query
{
	private List<ColumnValuePair> columnValuePair;
	public InsertQuery()
	{
		super(INSERT,null);
	}
	public InsertQuery(String tableName, List<ColumnValuePair> columnValuePair) 
	{
		super(INSERT, tableName);
		this.columnValuePair = columnValuePair;
	}
	public List<ColumnValuePair> getColumnValuePair() 
	{
		return columnValuePair;
	}
	public void setColumnValuePair(List<ColumnValuePair> columnValuePair) 
	{
		this.columnValuePair = columnValuePair;
	}
	@Override
	public void display()
	{
		System.out.println("Query Type: "+getType());
		System.out.println("Table Name: "+getTableName());
		for(ColumnValuePair temp: columnValuePair)
		{
			System.out.println(temp.toString());
		}
	}
}