package vo;

import java.util.List;
import vo.*;

public class CreateQuery extends Query {
        private String objectType;// i'm not creating an enum class! please add one if you would prefer that 
	private List<ColumnDataPair> columnDataPair;
        private String indexName;
        private String indexCol;

	public CreateQuery( String tableName,
			List<ColumnDataPair> columnDataPair) {
		super(QueryType.CREATE, tableName);
		this.columnDataPair = columnDataPair;
                this.objectType="TABLE";
	}
        
        public CreateQuery( String indexName, String tableName, String indexCol ) {
		super(QueryType.CREATE, tableName);
		this.columnDataPair = columnDataPair;
                this.objectType="INDEX";
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
        

    /**
     * @return the objectType
     */
    public String getObjectType() {
        return objectType;
    }

    /**
     * @param objectType the objectType to set
     */
    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    /**
     * @return the indexName
     */
    public String getIndexName() {
        return indexName;
    }

    /**
     * @param indexName the indexName to set
     */
    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    /**
     * @return the indexCol
     */
    public String getIndexCol() {
        return indexCol;
    }

    /**
     * @param indexCol the indexCol to set
     */
    public void setIndexCol(String indexCol) {
        this.indexCol = indexCol;
    }

}
