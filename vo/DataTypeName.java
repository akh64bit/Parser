package vo;

public enum DataTypeName
{
	REAL("REAL"), VARCHAR("VARCHAR"), INTEGER("INTEGER"), SDO_GEOM("SDO_GEOM");
	private String name;
	private DataTypeName(String name)
	{
		this.name = name;
	}
	public String getDataType()
	{
		return name;
	}
}
