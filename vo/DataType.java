package vo;

public class DataType
{
	public static final String REAL = "REAL";
	public static final String VARCHAR = "VARCHAR";
	public static final String INTEGER = "INTEGER";
	public static final String SDO_GEOM = "SDO_GEOM";
	private String name;
	private int size;
	public String getName()
	{
		return name;
	}
	public void setName(String name) 
	{
		this.name = name;
	}
	public int getSize() 
	{
		return size;
	}
	public void setSize(int size) 
	{
		this.size = size;
	}
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		b.append("Type Name: ").append(name);
		if(VARCHAR.equalsIgnoreCase(name))
			b.append("\nSize: ").append(size);
		return b.toString();
	}
}