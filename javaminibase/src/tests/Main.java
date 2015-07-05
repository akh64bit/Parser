package tests;

import global.AttrOperator;
import global.AttrType;
import global.RID;
import global.SdoGeom;
import global.SystemDefs;
import global.TupleOrder;
import heap.FieldNumberOutOfBoundException;
import heap.Heapfile;
import heap.InvalidTupleSizeException;
import heap.InvalidTypeException;
import heap.Tuple;
import iterator.CondExpr;
import iterator.FileScan;
import iterator.FldSpec;
import iterator.JoinsException;
import iterator.PredEvalException;
import iterator.RelSpec;
import iterator.SortMerge;
import iterator.UnknowAttrType;
import iterator.WrongPermat;

import java.io.IOException;
import java.util.Vector;

import bufmgr.PageNotReadException;


/*class Sailor
{
	public int    sid;
	public String sname;
	public int    rating;
	public double age;
	  
	public Sailor (int _sid, String _sname, int _rating,double _age) 
	{
	    sid    = _sid;
	    sname  = _sname;
	    rating = _rating;
	    age    = _age;
	}
}

	//Define the Boat schema
class Boats
{
	public int    bid;
	public String bname;
	public String color;
	  
	public Boats (int _bid, String _bname, String _color) 
	{
		bid   = _bid;
	    bname = _bname;
	    color = _color;
	}
}

	//Define the Reserves schema
class Reserves
{
	  public int    sid;
	  public int    bid;
	  public String date;
	  
	  public Reserves (int _sid, int _bid, String _date) 
	  {
		  sid  = _sid;
		  bid  = _bid;
		  date = _date;
	  }
}*/

public class Main
{
//	private boolean OK = true;
//	private boolean FAIL = false;
//	private Vector<Sailor> sailors;
//	private Vector<Boats> boats;
//	private Vector<Reserves> reserves;
	
	private AttrType[] getAttrFromString(String colTypeList[])
	{
		AttrType [] types = new AttrType[colTypeList.length];
		int numString=0;
		for(int i=0; i<colTypeList.length; ++i)
		{
			if(colTypeList[i].matches("INTEGER"))
			{
				types[i] = new AttrType(AttrType.attrInteger);
			}
			else if (colTypeList[i].matches("REAL"))
			{
				types[i] = new AttrType(AttrType.attrReal);
			}
			else if (colTypeList[i].matches("VARCHAR"))
			{
				types[i] = new AttrType(AttrType.attrString);
				numString++;
			}
			else if (colTypeList[i].matches("SDOGEOM"))
			{
				types[i] = new AttrType(AttrType.attrGeom);
			}
		}
		return types;
	}

	boolean createTable(String tableName, String colNameList[], String colTypeList[])
	{
		AttrType [] types = new AttrType[colNameList.length];
		int numString=0;
		for(int i=0; i<colTypeList.length; ++i)
		{
			if(colTypeList[i].matches("INTEGER"))
			{
				types[i] = new AttrType(AttrType.attrInteger);
			}
			else if (colTypeList[i].matches("REAL"))
			{
				types[i] = new AttrType(AttrType.attrReal);
			}
			else if (colTypeList[i].matches("VARCHAR"))
			{
				types[i] = new AttrType(AttrType.attrString);
				numString++;
			}
			else if (colTypeList[i].matches("SDOGEOM"))
			{
				types[i] = new AttrType(AttrType.attrGeom);
			}
			i++;
		}
		
		short sizes[] = new short [numString];
	    for(int i=0; i<numString; ++i)
	    {
	    	sizes[i] = 30; //first elt. is 30
	    }
	    
	    Heapfile f = null;
	    try 
	    {
	    	f = new Heapfile(tableName + ".in");
	    }
	    catch (Exception e) 
	    {
	    	System.err.println("*** error in Heapfile constructor ***");
	    	e.printStackTrace();
	    	return false;
	    }
	    return true;
	}
	
	boolean insertIntoTable(String tableName, String colNameList[], String colTypeList[], String values[])
	{
		AttrType [] types = new AttrType[colNameList.length];
		int numString=0;
		for(int i=0; i<colTypeList.length; ++i)
		{
			if(colTypeList[i].matches("INTEGER"))
			{
				types[i] = new AttrType(AttrType.attrInteger);
			}
			else if (colTypeList[i].matches("REAL"))
			{
				types[i] = new AttrType(AttrType.attrReal);
			}
			else if (colTypeList[i].matches("VARCHAR"))
			{
				types[i] = new AttrType(AttrType.attrString);
				numString++;
			}
			else if (colTypeList[i].matches("SDOGEOM"))
			{
				types[i] = new AttrType(AttrType.attrGeom);
			}
		}
		
		short sizes[] = new short [numString];
	    for(int i=0; i<numString; ++i)
	    {
	    	sizes[i] = 30; //first elt. is 30
	    }
	    
	    Tuple t = new Tuple();
	    try 
	    {
	    	t.setHdr((short) colNameList.length,types, sizes);
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
	    	f = new Heapfile(tableName + ".in");
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
	    	t.setHdr((short) colNameList.length,types, sizes);
	    }
	    catch (Exception e) 
	    {
	    	System.err.println("*** error in Tuple.setHdr() ***");
	    	e.printStackTrace();
	    	return false;
	    }
	    
	    try 
	    {
	    	for(int i=0; i<colTypeList.length; ++i)
	    	{
	    		switch (types[i].attrType)
	    		{
	    		case AttrType.attrInteger:
	    			t.setIntFld(i+1, Integer.parseInt(values[i]));
	    			break;
	    		case AttrType.attrReal:
	    			t.setFloFld(i+1, Float.parseFloat(values[i]));
	    			break;
	    		case AttrType.attrString:
	    			t.setStrFld(i+1, values[i]);
	    			break;
	    		case AttrType.attrGeom:
	    			String val[] = values[i].split(",");
	    			int x1 = Integer.parseInt(val[0]); int y1 = Integer.parseInt(val[1]);
	    			int x2 = Integer.parseInt(val[2]); int y2 = Integer.parseInt(val[3]);
	    			int x3 = Integer.parseInt(val[4]); int y3 = Integer.parseInt(val[5]);
	    			int x4 = Integer.parseInt(val[6]); int y4 = Integer.parseInt(val[7]);
	    			t.setSdoGeomFld(i+1, new SdoGeom(x1, y1, x2, y2, x3, y3,x4, y4));
	    			break;
	    		}
	    	}  
	    }
	    catch (Exception e) 
	    {
	    	System.err.println("*** Heapfile error in Tuple.setStrFld() ***");
	    	e.printStackTrace();
	    	return false;
	    }
	    
	    try 
	    {
	    	RID rid = f.insertRecord(t.returnTupleByteArray());
	    }
	    catch (Exception e) 
	    {
	    	System.err.println("*** error in Heapfile.insertRecord() ***");
	    	e.printStackTrace();
	    	return false;
	    }
	    return true;
	}
	
	public FileScan selectAll(String tableName, String colNameList[], String colTypeList[])
	{
		FldSpec [] projection = new FldSpec[colNameList.length];
	    for(int i=0; i<colNameList.length; ++i)
	    {
	    	projection[i] = new FldSpec(new RelSpec(RelSpec.outer), i+1);
	    }
	    
	    AttrType [] types = new AttrType[colTypeList.length];
		int numString=0;
		for(int i=0; i<colTypeList.length; ++i)
		{
			if(colTypeList[i].matches("INTEGER"))
			{
				types[i] = new AttrType(AttrType.attrInteger);
			}
			else if (colTypeList[i].matches("REAL"))
			{
				types[i] = new AttrType(AttrType.attrReal);
			}
			else if (colTypeList[i].matches("VARCHAR"))
			{
				types[i] = new AttrType(AttrType.attrString);
				numString++;
			}
			else if(colTypeList[i].matches("SDOGEOM"))
			{
				types[i] = new AttrType(AttrType.attrGeom);
			}
		}
		
		short sizes[] = new short [numString];
	    for(int i=0; i<numString; ++i)
	    {
	    	sizes[i] = 30; //first elt. is 30
	    }
	    
	    FileScan am = null;
	    try 
	    {
	      am  = new FileScan(tableName+".in", types, sizes, 
					  (short)colTypeList.length, (short)colTypeList.length,
					  projection, null);
	    }
	    catch (Exception e) 
	    {
	       System.err.println (""+e);
	    }
	    return am;
	}
	
	public SortMerge join(String tableName1, String colNameList1[], String colTypeList1[], String joinColumn1, 
			String tableName2, String colNameList2[], String colTypeList2[], String joinColumn2, 
			int outerCol[], int innerCol[], CondExpr expr[])
	{
		FileScan fs1 = selectAll(tableName1, colNameList1, colTypeList1);
		FileScan fs2 = selectAll(tableName2, colNameList2, colTypeList2);
		
		AttrType type1[] = getAttrFromString(colTypeList1);
		AttrType type2[] = getAttrFromString(colTypeList2);
		
		short size1[] = {30};
		short size2[] = {30};
		
		int projSize = outerCol.length + innerCol.length;
		FldSpec [] proj_list = new FldSpec[projSize];
		
		for(int i=0; i<outerCol.length; ++i)
		{
			proj_list[i] = new FldSpec(new RelSpec(RelSpec.outer), outerCol[i]);
		}
		
		for(int i=0; i<innerCol.length; ++i)
		{
			proj_list[i+outerCol.length] = new FldSpec(new RelSpec(RelSpec.innerRel), innerCol[i]);
		}
	    
		AttrType [] jtype = new AttrType[2];
	    jtype[0] = new AttrType (AttrType.attrString);
	    jtype[1] = new AttrType (AttrType.attrString);
	    
	    TupleOrder ascending = new TupleOrder(TupleOrder.Ascending);
	    SortMerge sm = null;
	    
	    try {
	        sm = new SortMerge(type1, 4, size1,
	  			 type2, 4, size2,
	  			 2, 4, 
	  			 2, 4, 
	  			 10,
	  			 fs1, fs2, 
	  			 false, false, ascending,
	  			 expr, proj_list, proj_list.length);
	      }
	      catch (Exception e) {
	        System.err.println("*** join error in SortMerge constructor ***"); 
	        System.err.println (""+e);
	        e.printStackTrace();
	      }
		return sm;
	}
	public Main()
	{

	    
//	    String dbpath = "/tmp/"+System.getProperty("user.name")+".minibase.jointestdb"; 
//	    String logpath = "/tmp/"+System.getProperty("user.name")+".joinlog";

		String dbpath = ".minibase.jointestdb"; 
	    String logpath = ".joinlog";

	    String remove_cmd = "/bin/rm -rf ";
	    String remove_logcmd = remove_cmd + logpath;
	    String remove_dbcmd = remove_cmd + dbpath;
	    String remove_joincmd = remove_cmd + dbpath;

//	    try 
//	    {
//	    	Runtime.getRuntime().exec(remove_logcmd);
//	    	Runtime.getRuntime().exec(remove_dbcmd);
//	    	Runtime.getRuntime().exec(remove_joincmd);
//	    }
//	    catch (IOException e) 
//	    {
//	    	System.err.println (""+e);
//	    }

	    SystemDefs sysdef = new SystemDefs( dbpath, 1000, 50, "Clock" );
	    String c[] = {"name", "age", "salary", "location"};
	    String t[] = {"VARCHAR", "INTEGER", "INTEGER", "SDOGEOM"};
	    createTable("Navneet", c, t);
	    String v1[] = {"Nav1", "16", "4000", "1,1,2,2,1,1"};
	    String v2[] = {"Nav2", "26", "2000", "1,1,2,2,2,2"};
	    String v3[] = {"Nav3", "36", "3000", "1,1,2,2,3,3"};
	    String v4[] = {"Nav4", "46", "4000", "1,1,2,2,4,4"};
	    createTable("Jha", c, t);
	    String w1[] = {"Nav1", "16", "4000", "1,1,2,2,1,1"};
	    String w2[] = {"Nav2", "26", "2000", "1,1,2,2,2,2"};
	    String w3[] = {"Nav3", "36", "3000", "1,1,2,2,3,3"};
	    String w4[] = {"Nav4", "46", "4000", "1,1,2,2,4,4"};
//	    insertIntoTable("Navneet", c, t, v1);
//	    insertIntoTable("Navneet", c, t, v2);
//	    insertIntoTable("Navneet", c, t, v3);
//	    insertIntoTable("Navneet", c, t, v4);
//	    insertIntoTable("Jha", c, t, w1);
//	    insertIntoTable("Jha", c, t, w2);
//	    insertIntoTable("Jha", c, t, w3);
//	    insertIntoTable("Jha", c, t, w4);
	    
	    int outerCol[] = {1,2,3,4};
	    int innerCol[] = {1,2,3,4};
	    
	    CondExpr expr[] = new CondExpr[2];
	    expr[0] = new CondExpr();
	    
	    expr[0].next  = null;
	    expr[0].op    = new AttrOperator(AttrOperator.aopEQ);
	    expr[0].type1 = new AttrType(AttrType.attrSymbol);
	    expr[0].type2 = new AttrType(AttrType.attrSymbol);
	    expr[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),2);
	    expr[0].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),2);

	    expr[1] = null;
	    SortMerge sm = join("Navneet", c, t, null, "Jha", c, t, null, outerCol, innerCol, expr);
	    
	    Tuple tuple;
	    
	    FileScan am = selectAll("Navneet", c, t);
	    
//	    FldSpec [] Sprojection = new FldSpec[4];
//	    Sprojection[0] = new FldSpec(new RelSpec(RelSpec.outer), 1);
//	    Sprojection[1] = new FldSpec(new RelSpec(RelSpec.outer), 2);
//	    Sprojection[2] = new FldSpec(new RelSpec(RelSpec.outer), 3);
//	    Sprojection[3] = new FldSpec(new RelSpec(RelSpec.outer), 4);
//	    
	    AttrType [] types = new AttrType[t.length];
		int numString=0;
		for(int i=0; i<t.length; ++i)
		{
			if(t[i].matches("INTEGER"))
			{
				types[i] = new AttrType(AttrType.attrInteger);
			}
			else if (t[i].matches("REAL"))
			{
				types[i] = new AttrType(AttrType.attrReal);
			}
			else if (t[i].matches("VARCHAR"))
			{
				types[i] = new AttrType(AttrType.attrString);
				numString++;
			}
			else if(t[i].matches("SDOGEOM"))
			{
				types[i] = new AttrType(AttrType.attrGeom);
			}
		}
//		
//		short sizes[] = new short [numString];
//	    for(int i=0; i<numString; ++i)
//	    {
//	    	sizes[i] = 30; //first elt. is 30
//	    }
	    
	    
//	    FileScan am = null;
//	    try 
//	    {
//	      am  = new FileScan("Navneet.in", types, sizes, 
//					  (short)4, (short)4,
//					  Sprojection, null);
//	    }
//	    catch (Exception e) 
//	    {
//	       System.err.println (""+e);
//	    }
	    
		AttrType types11[] = new AttrType[2*types.length];
		for (int i=0; i<types.length; ++i)
			types11[i] = types[i];
		for (int i=0; i<types.length; ++i)
			types11[i+types.length] = types[i];
		
		
		try 
		{
	        while ((tuple = sm.get_next()) != null) 
	        {
	        	tuple.print(types11);
            }
		}
	      catch (Exception e) {
	        System.err.println (""+e);
	         e.printStackTrace();
	         
	      }
	    Tuple s;
	    try {
			while ((s = am.get_next()) != null)
	    	s.print(types);
		} catch (JoinsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTupleSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PageNotReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PredEvalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknowAttrType e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FieldNumberOutOfBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WrongPermat e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//	    expr[0].next  = null;
//	    expr[0].op    = new AttrOperator(AttrOperator.aopEQ);
//	    expr[0].type1 = new AttrType(AttrType.attrSymbol);
//	    expr[0].type2 = new AttrType(AttrType.attrSymbol);
//	    expr[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),1);
//	    expr[0].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),1);
//
//	    expr[1].op    = new AttrOperator(AttrOperator.aopEQ);
	}
	
	public static void main(String args[])
	{
		new Main();
		System.out.println("Hello");
	}
}
