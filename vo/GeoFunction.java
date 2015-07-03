package vo;

public class GeoFunction
{
	private String shape1;
	private String shape2;
	private double precision;
	public String getShape1() 
	{
		return shape1;
	}
	public void setShape1(String shape1) 
	{
		this.shape1 = shape1;
	}
	public String getShape2() 
	{
		return shape2;
	}
	public void setShape2(String shape2) 
	{
		this.shape2 = shape2;
	}
	public double getPrecision() 
	{
		return precision;
	}
	public void setPrecision(double precision) 
	{
		this.precision = precision;
	}
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		b.append("shape1: ").append(shape1).append("; ");
		b.append("shape2: ").append(shape2).append("; ");
		b.append("precision: ").append(precision).append(";");
		return b.toString();
	}
}