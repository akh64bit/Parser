package vo;

public enum DataType 
{
	REAL("REAL"), VARCHAR("VARCHAR"), INTEGER("INTEGER"), SDO_GEOM("SDO_GEOM");
	private String name;
	private DataType(String name) 
	{
		this.name = name;
	}
	public String getDataType()
	{
		return name;
	}
}