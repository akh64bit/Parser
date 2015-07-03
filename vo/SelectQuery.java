package vo;

import java.util.List;

public class SelectQuery extends Query{
	
	private List<String> projectionList;
	
	public SelectQuery(QueryType type, String tableName, List<String> projectionList) {
		super(type, tableName);
		this.projectionList = projectionList;
	}

	/**
	 * @return the projectionList
	 */
	public List<String> getProjectionList() {
		return projectionList;
	}

	/**
	 * @param projectionList the projectionList to set
	 */
	public void setProjectionList(List<String> projectionList) {
		this.projectionList = projectionList;
	}

}
