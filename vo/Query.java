package vo;

enum QueryType
{
	SELECT("SELECT"), CREATE("CREATE"), INSERT("INSERT");
	private String name;
	private QueryType(String input)
	{
		name = input;
	}
	public String getQueryValue()
	{
		return name;
	}
}
public class Query 
{
	public static final String SELECT = "SELECT";
	public static final String CREATE = "CREATE";
	public static final String INSERT = "INSERT";
	private String type;
	private String tableName;
	public Query(String type, String tableName)
	{
		this.type = type;
		this.tableName = tableName;
	}
	public String getType() 
	{
		return type;
	}
	public void setType(String type) 
	{
		this.type = type;
	}
	public String getTableName() 
	{
		return tableName;
	}
	public void setTableName(String tableName) 
	{
		this.tableName = tableName;
	}
	public void display()
	{
		//to be overridden
	}
}