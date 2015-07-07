package vo;

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