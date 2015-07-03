package vo;

public class ColumnValuePair 
{
	private String columnName;
	private InsertValue value;
	public ColumnValuePair(String columnName, InsertValue value) 
	{
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
	public InsertValue getValue() 
	{
		return value;
	}
	public void setValue(InsertValue value) 
	{
		this.value = value;
	}
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		if(columnName != null)
			b.append("Column Name: ").append(columnName).append(";\n");
		b.append(value.toString());
		return b.toString();
	}
}