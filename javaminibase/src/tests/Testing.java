package tests;

import iterator.CondExpr;
import iterator.FileScan;
import iterator.FldSpec;
import iterator.JoinsException;
import iterator.PredEvalException;
import iterator.RelSpec;
import iterator.UnknowAttrType;
import iterator.WrongPermat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bufmgr.PageNotReadException;
import global.AttrOperator;
import global.AttrType;
import global.RID;
import global.SdoGeom;
import global.SystemDefs;
import heap.FieldNumberOutOfBoundException;
import heap.Heapfile;
import heap.InvalidTupleSizeException;
import heap.InvalidTypeException;
import heap.Tuple;

public class Testing
{
	public Testing()
	{
		String dbpath = ".minibase.jointestdb"; 
	    String logpath = ".joinlog";

	    String remove_cmd = "/bin/rm -rf ";
	    String remove_logcmd = remove_cmd + logpath;
	    String remove_dbcmd = remove_cmd + dbpath;
	    String remove_joincmd = remove_cmd + dbpath;

	    try 
	    {
	    	Runtime.getRuntime().exec(remove_logcmd);
	    	Runtime.getRuntime().exec(remove_dbcmd);
	    	Runtime.getRuntime().exec(remove_joincmd);
	    }
	    catch (IOException e) 
	    {
	    	System.err.println (""+e);
	    }

		SystemDefs sysdef = new SystemDefs( dbpath, 1000, 50, "Clock" );
		
		
		AttrType types[] = new AttrType[4];
		types[0] = new AttrType(AttrType.attrString); //Table Name
		types[1] = new AttrType(AttrType.attrString); //Column Name
		types[2] = new AttrType(AttrType.attrString); //Column Type
		types[3] = new AttrType(AttrType.attrInteger); //Column Id
		
		short sizes[] = new short [3];
	    for(int i=0; i<3; ++i)
	    {
	    	sizes[i] = 100; //first elt. is 30
	    }
	    
	    Tuple t = new Tuple();
	    try 
	    {
	    	t.setHdr((short) 4,types, sizes);
	    }
	    catch (Exception e) 
	    {
	    	System.err.println("*** error in Tuple.setHdr() ***");
	    	e.printStackTrace();
	    	return;
	    }
	    
	    Heapfile f = null;
	    try 
	    {
	    	f = new Heapfile("metadata.in");
	    }
	    catch (Exception e) 
	    {
	    	System.err.println("*** error in Heapfile constructor ***");
	    	e.printStackTrace();
	    	return;
	    }
	}

	public FileScan getTableType(String tableName)
	{
		FldSpec [] projection = new FldSpec[4];
	    for(int i=0; i<4; ++i)
	    {
	    	projection[i] = new FldSpec(new RelSpec(RelSpec.outer), i+1);
	    }
	   
	    AttrType [] types = new AttrType[4];
	    types[0] = new AttrType(AttrType.attrString);
	    types[1] = new AttrType(AttrType.attrString);
	    types[2] = new AttrType(AttrType.attrString);
	    types[3] = new AttrType(AttrType.attrInteger);
	    
	    short sizes[] = new short [3];
	    for(int i=0; i<3; ++i)
	    {
	    	sizes[i] = 100; //first elt. is 30
	    }
	    
	    CondExpr expr[] = new CondExpr[2];
	    expr[0] = new CondExpr();
	    expr[0].next  = null;
	    expr[0].op    = new AttrOperator(AttrOperator.aopEQ);
	    expr[0].type1 = new AttrType(AttrType.attrSymbol);
	    expr[0].type2 = new AttrType(AttrType.attrString);
	    expr[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),1);
	    expr[0].operand2.string = new String (tableName);
	   
	    FileScan am = null;
	    try 
	    {
	      am  = new FileScan("metadata.in", types, sizes, 
					  (short)4, (short)4,
					  projection, expr);
	    }
	    catch (Exception e) 
	    {
	       System.err.println (""+e);
	    }
	    return am;
	}
	
	boolean createTable(String tableName, List<String> colNames, List<String> colTypes)
	{
		AttrType types[] = new AttrType[4];
		types[0] = new AttrType(AttrType.attrString); //Table Name
		types[1] = new AttrType(AttrType.attrString); //Column Name
		types[2] = new AttrType(AttrType.attrString); //Column Type
		types[3] = new AttrType(AttrType.attrInteger); //Column Id
		short sizes[] = new short [3];
	    for(int i=0; i<3; ++i)
	    {
	    	sizes[i] = 100; //first elt. is 30
	    }
		
	    Tuple t = new Tuple();
	    try 
	    {
	    	t.setHdr((short) 4,types, sizes);
	    }
	    catch (Exception e) 
	    {
	    	System.err.println("*** error in Tuple.setHdr() ***");
	    	e.printStackTrace();
	    	return false;
	    }
	    
	    Heapfile f = null;
	    try 
	    {
	    	f = new Heapfile("metadata.in");
	    }
	    catch (Exception e) 
	    {
	    	System.err.println("*** error in Heapfile constructor ***");
	    	e.printStackTrace();
	    	return false;
	    }
	    
	    int size = t.size();
	    t = new Tuple(size);
	    try 
	    {
	    	t.setHdr((short) 4,types, sizes);
	    }
	    catch (Exception e) 
	    {
	    	System.err.println("*** error in Tuple.setHdr() ***");
	    	e.printStackTrace();
	    	return false;
	    }
	    
	    try 
	    {
			for(int i=0; i<colNames.size(); ++i)
			{
				t.setStrFld(1, tableName);
				t.setStrFld(2, colNames.get(i));
				t.setStrFld(3, colTypes.get(i));
				t.setIntFld(4, i);
				RID rid = f.insertRecord(t.returnTupleByteArray());
			}
		} 
	    
	    
	    catch (Exception e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	    
	    return true;
	}

	boolean insertIntoTable(String tableName, List<String> colNames, List<String> values)
	{
		FileScan fs = getTableType(tableName);
		//Tuple countTuple;
		int count = 0;
		int numString = 0;;
		
		Tuple t;
		List<String> colList = new ArrayList<String>();
		List<String> typeList = new ArrayList<String>();
		List<Integer> idList = new ArrayList<Integer>();
		String tn=null;
		try 
		{
			while((t=fs.get_next()) != null)
			{
				
				tn = t.getStrFld(1);
				String cn = t.getStrFld(2);
				String ct = t.getStrFld(3);
				if(ct.matches("VARCHAR"))
					numString++;
				Integer cid = t.getIntFld(4);
				System.out.print(tn);
				System.out.print(cn);
				System.out.print(ct);
				System.out.println(cid);
				
				colList.add(cn);
				typeList.add(ct);
				idList.add(cid);
				
				count++;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		AttrType [] types = new AttrType[count];
		
		for (int i = 0; i<typeList.size();i++)
		{
			if(typeList.get(i).matches("INTEGER"))
			{
				types[i] = new AttrType(AttrType.attrInteger);
			}
			else if (typeList.get(i).matches("REAL"))
			{
				types[i] = new AttrType(AttrType.attrReal);
			}
			else if (typeList.get(i).matches("VARCHAR"))
			{
				types[i] = new AttrType(AttrType.attrString);
			}
			else if(typeList.get(i).matches("SDOGEOM"))
			{
				types[i] = new AttrType(AttrType.attrGeom);
			}
		}
			
	       
		//Code from here
		short sizes[] = new short [numString];
	    for(int i=0; i<numString; ++i)
	    {
	    	sizes[i] = 100; //first elt. is 30
	    }
		
	    t = new Tuple();
	    try 
	    {
	    	t.setHdr((short) typeList.size(),types, sizes);
	    }
	    catch (Exception e) 
	    {
	    	System.err.println("*** error in Tuple.setHdr() ***");
	    	e.printStackTrace();
	    	return false;
	    }
	    
	    Heapfile f = null;
	    try 
	    {
	    	f = new Heapfile(tn +".in");
	    }
	    catch (Exception e) 
	    {
	    	System.err.println("*** error in Heapfile constructor ***");
	    	e.printStackTrace();
	    	return false;
	    }
	    
	    int size = t.size();
	    t = new Tuple(size);
	    try 
	    {
	    	t.setHdr((short) typeList.size(),types, sizes);
	    }
	    catch (Exception e) 
	    {
	    	System.err.println("*** error in Tuple.setHdr() ***");
	    	e.printStackTrace();
	    	return false;
	    }
	    
	    try 
	    {
			for(int i=0; i<colNames.size(); ++i)
			{
				switch (types[i].attrType)
				{
				case AttrType.attrString:
					t.setStrFld(i+1, values.get(i));
					break;
				case AttrType.attrInteger:
					t.setIntFld(i+1, Integer.parseInt(values.get(i)));
					break;
				case AttrType.attrReal:
					t.setFloFld(i+1, Float.parseFloat(values.get(i)));
					break;
				case AttrType.attrGeom:
					String val[] = values.get(i).split(",");
	    			int x1 = Integer.parseInt(val[0]); int y1 = Integer.parseInt(val[1]);
	    			int x2 = Integer.parseInt(val[2]); int y2 = Integer.parseInt(val[3]);
	    			int x3 = Integer.parseInt(val[4]); int y3 = Integer.parseInt(val[5]);
	    			int x4 = Integer.parseInt(val[6]); int y4 = Integer.parseInt(val[7]);
	    			t.setSdoGeomFld(i+1, new SdoGeom(x1, y1, x2, y2, x3, y3, x4, y4));
	    			break;
				}
			}
			RID rid = f.insertRecord(t.returnTupleByteArray());
		} 
	    
	    
	    catch (Exception e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	    
	    //////////////////////
	    //////////////////////  Useless.......
	    
	   
	    return true;
	}
	
	public FileScan select (String tableName, CondExpr[] expr)
	{
		FileScan fs = getTableType(tableName);
		//Tuple countTuple;
		int count = 0;
		int numString = 0;;
		
		Tuple t;
		List<String> colList = new ArrayList<String>();
		List<String> typeList = new ArrayList<String>();
		List<Integer> idList = new ArrayList<Integer>();
		String tn=null;
		try 
		{
			while((t=fs.get_next()) != null)
			{
				
				tn = t.getStrFld(1);
				String cn = t.getStrFld(2);
				String ct = t.getStrFld(3);
				if(ct.matches("VARCHAR"))
					numString++;
				Integer cid = t.getIntFld(4);
				System.out.print(tn);
				System.out.print(cn);
				System.out.print(ct);
				System.out.println(cid);
				
				colList.add(cn);
				typeList.add(ct);
				idList.add(cid);
				
				count++;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		AttrType [] types = new AttrType[count];
		
		for (int i = 0; i<typeList.size();i++)
		{
			if(typeList.get(i).matches("INTEGER"))
			{
				types[i] = new AttrType(AttrType.attrInteger);
			}
			else if (typeList.get(i).matches("REAL"))
			{
				types[i] = new AttrType(AttrType.attrReal);
			}
			else if (typeList.get(i).matches("VARCHAR"))
			{
				types[i] = new AttrType(AttrType.attrString);
			}
			else if(typeList.get(i).matches("SDOGEOM"))
			{
				types[i] = new AttrType(AttrType.attrGeom);
			}
		}
			
	       
		//Code from here
		short sizes[] = new short [numString];
	    for(int i=0; i<numString; ++i)
	    {
	    	sizes[i] = 100; //first elt. is 30
	    }
	    
	    
		FldSpec [] projection = new FldSpec[typeList.size()];
		    for(int i=0; i<typeList.size(); ++i)
		    {
		    	projection[i] = new FldSpec(new RelSpec(RelSpec.outer), i+1);
		    }
		    		
		    for(int i=0; i<numString; ++i)
		    {
		    	sizes[i] = 100; //first elt. is 30
		    }
		    
		    FileScan am = null;
		    try 
		    {
		      am  = new FileScan(tableName+".in", types, sizes, 
						  (short)typeList.size(), (short)typeList.size(),
						  projection, expr);
		    }
		    catch (Exception e) 
		    {
		       System.err.println (""+e);
		    }
		    
		    try {
				while ((t=am.get_next()) != null)
				{
					t.print(types);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return null;
	}
	public static void main(String args[])
	{
		Testing test = new Testing();
		List<String> cn = new ArrayList<String>();
		List<String> ct = new ArrayList<String>();
		List<String> v1 = new ArrayList<String>();
		List<String> v2 = new ArrayList<String>();
		List<String> v3 = new ArrayList<String>();
		List<String> v4 = new ArrayList<String>();
		List<String> v5 = new ArrayList<String>();
		cn.add("Name"); cn.add("Age"); cn.add("salary");
		ct.add("VARCHAR"); ct.add("INTEGER"); ct.add("INTEGER");
		v1.add("nav1"); v1.add("26"); v1.add("1000");
		v2.add("nav2"); v2.add("27"); v2.add("2000");
		v3.add("nav3"); v3.add("28"); v3.add("3000");
		v4.add("nav4"); v4.add("29"); v4.add("4000");
		v5.add("nav5"); v5.add("22"); v5.add("5000");
		test.createTable("Navneet", cn, ct);
		test.createTable("Ak", cn, ct);
		test.insertIntoTable("Navneet", cn, v1);
		test.insertIntoTable("Ak", cn, v1);
		test.insertIntoTable("Ak", cn, v2);
		test.insertIntoTable("Ak", cn, v3);
		test.insertIntoTable("Ak", cn, v4);
		test.insertIntoTable("Ak", cn, v5);
		
		CondExpr[] expr = new CondExpr[3];
		CondExpr expr1 = new CondExpr();
		
		expr[0] = new CondExpr();
		expr[1] = new CondExpr();
		expr[2] = null;
		
		expr[0].next = expr1;
		expr[0].op = new AttrOperator(AttrOperator.aopEQ);
		expr[0].type1 = new AttrType(AttrType.attrSymbol);
		expr[0].type2 = new AttrType(AttrType.attrString);
		expr[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),1);
		expr[0].operand2.string = "nav2";
		
		expr1.next = null;
		expr1.op = new AttrOperator(AttrOperator.aopEQ);
		expr1.type1 = new AttrType(AttrType.attrSymbol);
		expr1.type2 = new AttrType(AttrType.attrString);
		expr1.operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),1);
		expr1.operand2.string = "nav5";
		
		expr[1].next = null;
		expr[1].op = new AttrOperator(AttrOperator.aopLT);
		expr[1].type1 = new AttrType(AttrType.attrSymbol);
		expr[1].type2 = new AttrType(AttrType.attrInteger);
		expr[1].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),3);
		expr[1].operand2.integer = 5001;
		
		
		System.out.println("Selecting...");
		test.select("Ak", expr);
		test.select("Navneet", null);
		
	}
	
}
