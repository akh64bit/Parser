package vo;

public class FromObj 
{
	private String tableName;
	private String alias;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String toString()
	{
		return tableName+" "+alias;
	}
}