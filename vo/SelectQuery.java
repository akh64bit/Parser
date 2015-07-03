package vo;

import iterator.sm_join_assign_src.CondExpr;
import java.util.ArrayList;
import java.util.List;

public class SelectQuery extends Query
{
	private List<SelectObj> select_list;
	private List<FromObj> from_list;
	private List<CondExpr> cond_list;
	public SelectQuery()
	{
		super(QueryType.SELECT, null);
		select_list = new ArrayList<SelectObj>();
		cond_list = new ArrayList<CondExpr>();
		from_list = new ArrayList<FromObj>();
	}
	public void addSelObject(SelectObj obj)
	{
		select_list.add(obj);
	}
	public void addCondObj(CondExpr expr)
	{
		cond_list.add(expr);
	}
	public void addFromObj(FromObj obj)
	{
		from_list.add(obj);
	}
	public void display()
	{
		System.out.println("Select List: ");
		for(SelectObj obj: select_list)
		{
			System.out.println(obj.toString());
		}
		System.out.println("From List: ");
		for(FromObj temp: from_list)
		{
			System.out.println(temp.toString());
		}
		System.out.println("Conditions: ");
		boolean fg = false;
		for(CondExpr expr: cond_list)
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
}