package vo;

enum QueryType
{
	SELECT("SELECT"), CREATE("CREATE"), INSERT("INSERT");
	private String name;
	private QueryType(String input)
	{
		name = input;
	}
	
	
}
public class Query 
{
	private QueryType type;
	
	private String tableName;

	public Query(QueryType type, String tableName) {
		super();
		this.type = type;
		this.tableName = tableName;
	}

	/**
	 * @return the type
	 */
	public QueryType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(QueryType type) {
		this.type = type;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	
	
}