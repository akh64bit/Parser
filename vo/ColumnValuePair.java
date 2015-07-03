package vo;

public class ColumnValuePair 
{
	private String columnName;
	private String value;
	public ColumnValuePair(String columnName, String value) 
	{
		super();
		this.columnName = columnName;
		this.value = value;
	}
	public String getColumnName() 
	{
		return columnName;
	}
	public void setColumnName(String columnName) 
	{
		this.columnName = columnName;
	}
	public String getValue() 
	{
		return value;
	}
	public void setValue(String value) 
	{
		this.value = value;
	}
}