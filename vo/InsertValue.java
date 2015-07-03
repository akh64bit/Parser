package vo;

public class InsertValue 
{
	public static final String GEOM_OBJ = "GEOM_OBJ";
	public static final String STRING = "STRING";
	public static final String INTEGER = "INTEGER";
	public static final String REAL = "REAL";
	private String valueType;
	private int integer;
	private String string;
	private float real;
	private float coOrdinatees [] = new float [8]; 
	public String getValueType() {
		return valueType;
	}
	public void setValueType(String valueType) {
		this.valueType = valueType;
	}
	public int getInteger() {
		return integer;
	}
	public void setInteger(int integer) {
		this.integer = integer;
	}
	public String getString() {
		return string;
	}
	public void setString(String string) {
		this.string = string;
	}
	public float getReal() {
		return real;
	}
	public void setReal(float real) {
		this.real = real;
	}
	public void setCoOrdinates(int position, float value)
	{
		coOrdinatees[position] = value;
	}
	public float getCoOrdinates(int position)
	{
		return coOrdinatees[position];
	}
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		b.append("Value Type: ").append(valueType).append("\n");
		if(GEOM_OBJ.equalsIgnoreCase(valueType))
		{
			b.append("Co-ordinates: ");
			for(int i=0;i<8;i+=2)
			{
				b.append("(").append(coOrdinatees[i]).append(",").append(coOrdinatees[i+1]).append(") ");
			}
		}
		else if(INTEGER.equalsIgnoreCase(valueType))
		{
			b.append("Value: ").append(integer);
		}
		else if(REAL.equalsIgnoreCase(valueType))
		{
			b.append("Value: ").append(real);
		}
		else if(STRING.equalsIgnoreCase(valueType))
		{
			b.append("Value: ").append(string);
		}
		return b.toString();
	}
}