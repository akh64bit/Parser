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

public class InsertInto
{
	public InsertInto()
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
					  projection, null);
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
		
		Tuple t;
		AttrType [] types = new AttrType[4];
	    types[0] = new AttrType(AttrType.attrString);
	    types[1] = new AttrType(AttrType.attrString);
	    types[2] = new AttrType(AttrType.attrString);
	    types[3] = new AttrType(AttrType.attrInteger);
		
	    
	    try {
			while((t = fs.get_next()) != null)
			{
				t.print(types);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return false;
	}
	
	public static void main(String args[])
	{
	
	}
	
}
