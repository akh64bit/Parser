package vo;

import global.AttrType;
import iterator.CondExpr;
import java.util.ArrayList;
import java.util.List;

public class SelectQuery extends Query
{
	private List<SelectObj> selectList;
	private List<FromObj> fromList;
	private List<CondExpr> condList;
	public SelectQuery()
	{
		super(SELECT, null);
		selectList = new ArrayList<SelectObj>();
		condList = new ArrayList<CondExpr>();
		fromList = new ArrayList<FromObj>();
	}
	public void addSelObject(SelectObj obj)
	{
		getSelectList().add(obj);
	}
	public void addCondObj(CondExpr expr)
	{
		getCondList().add(expr);
	}
	public void addFromObj(FromObj obj)
	{
		getFromList().add(obj);
	}
	public String getTableName(String alias)
	{
		for(FromObj temp: fromList)
		{
			if(temp.getAlias().equalsIgnoreCase(alias))
				return temp.getTableName();
		}
		return null;
	}
	public boolean checkTableAlias(String alias, String colName)
	{
		for(FromObj temp: fromList)
		{
			if(temp.getAlias().equalsIgnoreCase(alias))
			{
				return true;
			}
		}
		return false;
	}
	public boolean checkTableAlias(String alias)
	{
		for(FromObj temp: fromList)
		{
			if(temp.getAlias().equalsIgnoreCase(alias))
				return true;
		}
		return false;
	}
	public boolean getTableColumnList()
	{
		for(SelectObj temp : selectList)
		{
			if(SelectObj.FIELD.equalsIgnoreCase(temp.getSelType()))
			{
				String fieldSplit[] = temp.getValue().split("\\.");
				if(fieldSplit.length != 2)
				{
					System.err.println("Incorrect Select Field format. Table alias not prefixed for "+temp.getValue());
					return false;
				}
				if(!checkTableAlias(fieldSplit[0],fieldSplit[1]))
				{
					System.err.println("Incorrect alias '"+fieldSplit[0]+"'. No such table found in FROM clause");
					return false;
				}
			}
			else if(SelectObj.AREA.equalsIgnoreCase(temp.getSelType()))
			{
				GeoFunction geoFn = temp.getFunction();
				String fieldSplit[] = geoFn.getShape1().split("\\.");
				if(fieldSplit.length != 2)
				{
					System.err.println("Incorrect Select Field format. Table alias not prefixed for "+geoFn.getShape1());
					return false;
				}
				if(!checkTableAlias(fieldSplit[0],fieldSplit[1]))
				{
					System.err.println("Incorrect alias '"+fieldSplit[0]+"'. No such table found in FROM clause");
					return false;
				}
			}
			else
			{
				GeoFunction geoFn = temp.getFunction();
				String fieldSplit[] = geoFn.getShape1().split("\\.");
				if(fieldSplit.length != 2)
				{
					System.err.println("Incorrect Select Field format. Table alias not prefixed for "+geoFn.getShape1());
					return false;
				}
				if(!checkTableAlias(fieldSplit[0],fieldSplit[1]))
				{
					System.err.println("Incorrect alias '"+fieldSplit[0]+"'. No such table found in FROM clause");
					return false;
				}
				String fieldSplit1[] = geoFn.getShape2().split("\\.");
				if(fieldSplit1.length != 2)
				{
					System.err.println("Incorrect Select Field format. Table alias not prefixed for "+geoFn.getShape2());
					return false;
				}
				if(!checkTableAlias(fieldSplit1[0],fieldSplit1[1]))
				{
					System.err.println("Incorrect alias '"+fieldSplit1[0]+"'. No such table found in FROM clause");
					return false;
				}
			}
		}
		return true;
	}
	public boolean getCondColList()
	{
		for (CondExpr temp: condList)
		{
			if(AttrType.attrSymbol == temp.type1.attrType)
			{
				String fieldName = temp.operand1.string;
				String fieldSplit[] = fieldName.split("\\.");
				if(fieldSplit.length != 2)
				{
					System.err.println("Incorrect Select Field format. Table alias not prefixed for "+fieldName);
					return false;
				}
				if(!checkTableAlias(fieldSplit[0]))
				{
					System.err.println("Incorrect alias '"+fieldSplit[0]+"'. No such alias found in FROM clause");
					return false;
				}
			}
			if(AttrType.attrSymbol == temp.type2.attrType)
			{
				String fieldName = temp.operand2.string;
				String fieldSplit[] = fieldName.split("\\.");
				if(fieldSplit.length != 2)
				{
					System.err.println("Incorrect Select Field format. Table alias not prefixed for "+fieldName);
					return false;
				}
				if(!checkTableAlias(fieldSplit[0]))
				{
					System.err.println("Incorrect alias '"+fieldSplit[0]+"'. No such alias found in FROM clause");
					return false;
				}
			}
		}
		return true;
	}
	@Override
	public void display()
	{
		System.out.println("Select List: ");
		for(SelectObj obj: getSelectList())
		{
			System.out.println(obj.toString());
		}
		System.out.println("From List: ");
		for(FromObj temp: getFromList())
		{
			System.out.println(temp.toString());
		}
		System.out.println("Conditions: ");
		boolean fg = false;
		for(CondExpr expr: getCondList())
		{
			if(fg)
				System.out.println("AND");
			fg = true;
			boolean flag = false;
			CondExpr temp = expr;
			while(temp!=null)
			{
				if(flag)
					System.out.println(" OR");
				System.out.print(temp.toString());
				flag = true;
				temp = temp.next;
			}
			System.out.println();
		}
	}

    /**
     * @return the selectList
     */
    public List<SelectObj> getSelectList() {
        return selectList;
    }

    /**
     * @return the fromList
     */
    public List<FromObj> getFromList() {
        return fromList;
    }

    /**
     * @return the condList
     */
    public List<CondExpr> getCondList() {
        return condList;
    }

    /**
     * @param selectList the selectList to set
     */
    public void setSelectList(List<SelectObj> selectList) {
        this.selectList = selectList;
    }

    /**
     * @param fromList the fromList to set
     */
    public void setFromList(List<FromObj> fromList) {
        this.fromList = fromList;
    }

    /**
     * @param condList the condList to set
     */
    public void setCondList(List<CondExpr> condList) {
        this.condList = condList;
    }
}