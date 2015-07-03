package vo;

public class ColumnDataPair 
{
	private DataType dataType;
	private String columnName;
	public ColumnDataPair(DataType dataType, String columnName) 
	{
		super();
		this.dataType = dataType;
		this.columnName = columnName;
	}
	public DataType getDataType() 
	{
		return dataType;
	}
	public void setDataType(DataType dataType) 
	{
		this.dataType = dataType;
	}
	public String getColumnName() 
	{
		return columnName;
	}
	public void setColumnName(String columnName) 
	{
		this.columnName = columnName;
	}
}