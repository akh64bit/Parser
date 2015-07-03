package vo;

enum DataTypeName 
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
public class DataType
{
	private DataTypeName name;
	private int size;
	public DataTypeName getName() 
	{
		return name;
	}
	public void setName(DataTypeName name) {
		this.name = name;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
}