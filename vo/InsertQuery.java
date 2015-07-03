package vo;

import java.util.List;

public class InsertQuery extends Query
{
	private List<ColumnValuePair> columnValuePair;
	public InsertQuery(QueryType type, String tableName, List<ColumnValuePair> columnValuePair) 
	{
		super(type, tableName);
		this.columnValuePair = columnValuePair;
	}
	/**
	 * @return the columnValuePair
	 */
	public List<ColumnValuePair> getColumnValuePair() 
	{
		return columnValuePair;
	}
	/**
	 * @param columnValuePair the columnValuePair to set
	 */
	public void setColumnValuePair(List<ColumnValuePair> columnValuePair) 
	{
		this.columnValuePair = columnValuePair;
	}
}