package vo;

public class SelectObj 
{
	public static final String INTERSECTION = "SDO_GEOM.SDO_INTERSECTION";
	public static final String AREA = "SDO_GEOM.SDO_AREA";
	public static final String DISTANCE = "SDO_GEOM.SDO_DISTANCE";
	public static final String FIELD = "FIELD";
	
	private String selType;
	private String value;
	private int colId;
	private String colType;
	private GeoFunction function;
	public String getSelType() 
	{
		return selType;
	}
	public void setSelType(String selType) 
	{
		this.selType = selType;
	}
	public String getValue() 
	{
		return value;
	}
	public void setValue(String value) 
	{
		this.value = value;
	}
	public GeoFunction getFunction() 
	{
		return function;
	}
	public void setFunction(GeoFunction function) 
	{
		this.function = function;
	}
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		b.append("Selection Type: ").append(selType).append("; ");
		if(FIELD.equalsIgnoreCase(selType))
			b.append("Value: ").append(value).append(";");
		else
			b.append(function.toString());
		return b.toString();
	}
	public int getColId() {
		return colId;
	}
	public void setColId(int colId) {
		this.colId = colId;
	}
	public String getColType() {
		return colType;
	}
	public void setColType(String colType) {
		this.colType = colType;
	}
}