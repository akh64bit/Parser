package vo;

import java.util.List;

public class CreateQuery extends Query {
	List<ColumnDataPair> columnDataPair;

	public CreateQuery(QueryType type, String tableName,
			List<ColumnDataPair> columnDataPair) {
		super(type, tableName);
		this.columnDataPair = columnDataPair;
	}

	/**
	 * @return the columnDataPair
	 */
	public List<ColumnDataPair> getColumnDataPair() {
		return columnDataPair;
	}

	/**
	 * @param columnDataPair the columnDataPair to set
	 */
	public void setColumnDataPair(List<ColumnDataPair> columnDataPair) {
		this.columnDataPair = columnDataPair;
	}
	
	

}
