package parser;

import global.AttrOperator;
import global.AttrType;
import iterator.CondExpr;
import iterator.Operand;
import tokenizer.Token;
import tokenizer.Tokenizer;
import vo.FromObj;
import vo.GeoFunction;
import vo.SelectObj;
import vo.SelectQuery;

public class SelectParser 
{
	private Tokenizer tokenizer;
	private Token ttype;
	private SelectQuery queryVO;
	public SelectParser()
	{
		tokenizer = Tokenizer.getInstance();
		queryVO = new SelectQuery();
	}
	public SelectQuery getQueryValues()
	{
		return queryVO;
	}
	public void readInput()
	{
		ttype = tokenizer.getToken();
//		System.out.println(tokenizer.getTokenValue());
	}
	public SelectQuery getQueryVO()
	{
		return queryVO;
	}
	public void unget()
	{
		tokenizer.ungetToken();
	}
	public boolean parse_select()
	{
		readInput();
		if(!ttype.equals(Token.SELECT))
			return false;
		if(!select_list())
			return false;
		readInput();
		if(!ttype.equals(Token.FROM))
			return false;
		if(!from_list())
			return false;
		readInput();
		if(ttype.equals(Token.WHERE))
		{
			tokenizer.ungetToken();
			if(!where_clause())
				return false;
			readInput();
			if(!ttype.equals(Token.SEMICOLON))
				return false;
		}
		else if(!ttype.equals(Token.SEMICOLON))
			return false;
		return true;
	}
	public boolean select_list()
	{
		if(!select_opt())
			return false;
		readInput();
		if(ttype.equals(Token.COMMA))
			return select_list();
		else
			tokenizer.ungetToken();
		return true;
	}
	public boolean select_opt()
	{
		SelectObj obj = new SelectObj();
		readInput();
		if(ttype.equals(Token.AREA))
		{
			obj.setSelType(SelectObj.AREA);
			obj.setFunction(new GeoFunction());
			tokenizer.ungetToken();
			if(!area(obj.getFunction()))
				return false;
		}
		else if(ttype.equals(Token.DISTANCE))
		{
			obj.setSelType(SelectObj.DISTANCE);
			obj.setFunction(new GeoFunction());
			tokenizer.ungetToken();
			if(!distance(obj.getFunction()))
				return false;
		}
		else if(ttype.equals(Token.INTERSECTION))
		{
			obj.setSelType(SelectObj.INTERSECTION);
			obj.setFunction(new GeoFunction());
			tokenizer.ungetToken();
			if(!intersection(obj.getFunction()))
				return false;
		}
		else if(ttype.equals(Token.ID))
		{
			obj.setSelType(SelectObj.FIELD);
			obj.setValue(tokenizer.getTokenValue());
		}
		else
			return false;
		queryVO.addSelObject(obj);
		return true;
	}
	public boolean area(GeoFunction obj)
	{
		readInput();
		if(!ttype.equals(Token.AREA))
			return false;
		readInput();
		if(!ttype.equals(Token.LPAREN))
			return false;
		readInput();
		if(!ttype.equals(Token.ID))
			return false;
		obj.setShape1(tokenizer.getTokenValue());
		readInput();
		if(!ttype.equals(Token.COMMA))
			return false;
		readInput();
		if(ttype.equals(Token.VAL))
		{
			try
			{
				obj.setPrecision(Double.parseDouble(tokenizer.getTokenValue()));
			}
			catch(Exception e) {return false;}
		}
		else
			return false;
		readInput();
		if(!ttype.equals(Token.RPAREN))
			return false;
		return true;
	}
	public boolean distance(GeoFunction obj)
	{
		readInput();
		if(!ttype.equals(Token.DISTANCE))
			return false;
		readInput();
		if(!ttype.equals(Token.LPAREN))
			return false;
		readInput();
		if(!ttype.equals(Token.ID))
			return false;
		obj.setShape1(tokenizer.getTokenValue());
		readInput();
		if(!ttype.equals(Token.COMMA))
			return false;
		readInput();
		if(!ttype.equals(Token.ID))
			return false;
		obj.setShape2(tokenizer.getTokenValue());
		readInput();
		if(!ttype.equals(Token.COMMA))
			return false;
		readInput();
		if(ttype.equals(Token.VAL))
		{
			try
			{
				obj.setPrecision(Double.parseDouble(tokenizer.getTokenValue()));
			}
			catch(Exception e) {return false;}
		}
		else
			return false;
		readInput();
		if(!ttype.equals(Token.RPAREN))
			return false;
		return true;
	}
	public boolean intersection(GeoFunction obj)
	{
		readInput();
		if(!ttype.equals(Token.INTERSECTION))
			return false;
		readInput();
		if(!ttype.equals(Token.LPAREN))
			return false;
		readInput();
		if(!ttype.equals(Token.ID))
			return false;
		obj.setShape1(tokenizer.getTokenValue());
		readInput();
		if(!ttype.equals(Token.COMMA))
			return false;
		readInput();
		if(!ttype.equals(Token.ID))
			return false;
		obj.setShape2(tokenizer.getTokenValue());
		readInput();
		if(!ttype.equals(Token.COMMA))
			return false;
		readInput();
		if(ttype.equals(Token.VAL))
		{
			try
			{
				obj.setPrecision(Double.parseDouble(tokenizer.getTokenValue()));
			}
			catch(Exception e) {return false;}
		}
		else
			return false;
		readInput();
		if(!ttype.equals(Token.RPAREN))
			return false;
		return true;
	}
	public boolean from_list()
	{
		if(!from_id())
			return false;
		readInput();
		if(ttype.equals(Token.COMMA))
			return from_list();
		else
			tokenizer.ungetToken();
		return true;
	}
	public boolean from_id()
	{
		FromObj obj = new FromObj();
		readInput();
		if(!ttype.equals(Token.ID))
			return false;
		obj.setTableName(tokenizer.getTokenValue());
		readInput();
		if(!ttype.equals(Token.ID))
			return false;
		obj.setAlias(tokenizer.getTokenValue());
		queryVO.addFromObj(obj);
		return true;
	}
	public boolean where_clause()
	{
		readInput();
		if(!ttype.equals(Token.WHERE))
			return false;
		if(!where_list())
			return false;
		return true;
	}
	public boolean where_list()
	{
		CondExpr obj = new CondExpr();
		if(!cond(obj))
			return false;
		queryVO.addCondObj(obj);
		readInput();
		if(ttype.equals(Token.AND))
			return where_list();
		else
			tokenizer.ungetToken();
		return true;
	}
	public boolean cond(CondExpr obj)
	{
		readInput();
		if(!ttype.equals(Token.ID))
			return false;
		obj.type1= new AttrType(3);
		obj.operand1 = new Operand();
		obj.operand1.string = tokenizer.getTokenValue();
		if(!rel_op(obj))
			return false;
		readInput();
		if(ttype.equals(Token.ID))
		{
			obj.type2 = new AttrType(AttrType.attrSymbol);
			obj.operand2 = new Operand();
			obj.operand2.string = tokenizer.getTokenValue();
		}
		else if(ttype.equals(Token.VAL))
		{
			boolean flag = false;
			String temp = tokenizer.getTokenValue();
			obj.operand2 = new Operand();
			try
			{
				obj.operand2.integer = Integer.parseInt(temp);
				obj.type2 = new AttrType(AttrType.attrInteger);
				flag = true;
			}
			catch (Exception e){/*Not Integer*/}
			if(!flag)
			{
				try
				{
					obj.operand2.real = Float.parseFloat(temp);
					obj.type2 = new AttrType(AttrType.attrReal);
					flag = true;
				}
				catch (Exception e){/*Not Float*/}
			}
			if(!flag)
			{
				if(temp.matches("^'[A-z]+(.[A-z]+)?'$"))
				{
					obj.operand2.string = temp.replaceAll("'","");
					obj.type2 = new AttrType(AttrType.attrString);
					flag = true;
				}
			}
			if(!flag)
				return false;
		}
		else
			return false;
		readInput();
		if(ttype.equals(Token.OR))
		{
			obj.next = new CondExpr();
			if(!cond(obj.next))
				return false;
		}
		else
			unget();
		return true;
	}
	public boolean rel_op(CondExpr obj)
	{
		readInput();
		if(ttype.equals(Token.EQUAL_TO))
			obj.op = new AttrOperator(AttrOperator.aopEQ);
		else if(ttype.equals(Token.GREATER))
			obj.op = new AttrOperator(AttrOperator.aopGT);
		else if(ttype.equals(Token.LESS))
			obj.op = new AttrOperator(AttrOperator.aopLT);
		else if(ttype.equals(Token.GREATER_EQUAL))
			obj.op = new AttrOperator(AttrOperator.aopGE);
		else if(ttype.equals(Token.LESS_EQUAL))
			obj.op = new AttrOperator(AttrOperator.aopLE);
		else
			return false;
		return true;
	}
	public static void main(String[] args) 
	{
		boolean result = false;
		SelectParser parser = new SelectParser();
		result = parser.parse_select();
		System.out.println(result?"Correct Syntax":"Syntax Error");
		if(result)
			parser.queryVO.display();
	}
}