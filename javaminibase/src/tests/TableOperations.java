package tests;

import global.AttrOperator;
import global.AttrType;
import global.SdoGeom;
import heap.Heapfile;
import heap.Tuple;
import iterator.CondExpr;
import iterator.FileScan;
import iterator.FldSpec;
import iterator.RelSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TableObject
{
	public String tableName;
	public int numColumns;
	public Map<Integer, String> columnId;
	public Map<String, Integer> columnName;
	public Map<String, String> columnTypes;

	public TableObject(String tn, int n, Map<Integer, String> cid, Map<String, String> ctype)
	{
		tableName = tn;
		numColumns = n;
		columnId = cid;
		columnTypes = ctype;
		
		columnName = new HashMap<String, Integer>();
		for(int i=0; i<columnId.size(); ++i)
		{
			String s = columnId.get(i);
			columnName.put(s, i);
		}
	}
}
class ColTypesAndSizes
{
	public AttrType[] colTypes;
	public short[] colSizes;
	public ColTypesAndSizes(AttrType[] ctype, short[] csize)
	{
		colTypes = ctype;
		colSizes = csize;
	}
}
public class TableOperations
{
	private static TableOperations tableOperations;
	public TableOperations()
	{
		/*String dbpath = ".minibase.jointestdb"; 
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
		new SystemDefs( dbpath, 1000, 50, "Clock" );*/
		try 
		{
			new Heapfile("metadata.in");
		}
		catch (Exception e) 
		{
			System.err.println("*** error in Heapfile constructor ***");
			e.printStackTrace();
			return;
		}
	}
	public static TableOperations getInstance() 
	{
		if (tableOperations == null) 
		{
			tableOperations = new TableOperations();
		}
		return tableOperations;
	}
	public ColTypesAndSizes getColTypeAndSize(String colTypes[])
	{
		AttrType [] types = new AttrType[colTypes.length];
		int numString = 0;
		for (int i = 0; i<colTypes.length;i++)
		{
			if(colTypes[i].matches("INTEGER"))
			{
				types[i] = new AttrType(AttrType.attrInteger);
			}
			else if (colTypes[i].matches("REAL"))
			{
				types[i] = new AttrType(AttrType.attrReal);
			}
			else if (colTypes[i].matches("VARCHAR"))
			{
				types[i] = new AttrType(AttrType.attrString);
				numString++;
			}
			else if(colTypes[i].matches("SDO_GEOM"))
			{
				types[i] = new AttrType(AttrType.attrGeom);
			}
			else
			{
				System.out.println("Column Type not found!!!!!!! for "+colTypes[i]);
			}
		}
		short sizes[] = new short [numString];
		for(int i=0; i<numString; ++i)
		{
			sizes[i] = 100; //first elt. is 30
		}
		return new ColTypesAndSizes(types, sizes);
	}
	public boolean createTable (String tableName, List<String> colNames, List<String> colTypes)
	{
		String types[] = {"VARCHAR", "VARCHAR", "VARCHAR", "INTEGER"};
		ColTypesAndSizes ct = getColTypeAndSize(types);
		Tuple t = new Tuple();
		try 
		{
			t.setHdr((short) 4,ct.colTypes, ct.colSizes);
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
			t.setHdr((short) 4,ct.colTypes, ct.colSizes);
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
				f.insertRecord(t.returnTupleByteArray());
			}
		} 
		catch (Exception e) 
		{
			System.err.println("*** error in Tuple.setStrFld() ***");
			e.printStackTrace();
			return false;
		} 
		return true;
	}

	public boolean insertIntoTable(String tableName, List<String> values)
	{
		TableObject table = getTableDetails(tableName);
		Map<String, String> mapValues = new HashMap<String, String>();
		for(int i=0; i<values.size(); ++i)
		{
			mapValues.put(table.columnId.get(i), values.get(i));
		}
		return insertIntoTable(tableName, mapValues);
	}
	public boolean insertIntoTable(String tableName, Map<String, String> values)
	{
		TableObject table = getTableDetails(tableName);
		if(table.columnId.size() == 0)
		{
			System.err.println("Table: "+tableName+" not found");
			return false;
		}
		String types[] = new String[table.columnId.size()];
		for (int i=0; i<types.length; ++i)
		{
			types[i] = new String(table.columnTypes.get(table.columnId.get(i)));
		}
		ColTypesAndSizes ct = getColTypeAndSize(types);
		Tuple t = new Tuple();
		try 
		{
			t.setHdr((short) ct.colTypes.length, ct.colTypes, ct.colSizes);
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
			f = new Heapfile(tableName +".in");
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
			t.setHdr((short) ct.colTypes.length, ct.colTypes, ct.colSizes);
		}
		catch (Exception e) 
		{
			System.err.println("*** error in Tuple.setHdr() ***");
			e.printStackTrace();
			return false;
		}
		try 
		{
			for(int i=0; i<ct.colTypes.length; ++i)
			{
				switch (ct.colTypes[i].attrType)
				{
				case AttrType.attrString:
					t.setStrFld(i+1, values.get(table.columnId.get(i)));
					break;
				case AttrType.attrInteger:
					t.setIntFld(i+1, Integer.parseInt(values.get(table.columnId.get(i))));
					break;
				case AttrType.attrReal:
					t.setFloFld(i+1, Float.parseFloat(values.get(table.columnId.get(i))));
					break;
				case AttrType.attrGeom:
					String val[] = values.get(table.columnId.get(i)).split(",");
					int x1 = Integer.parseInt(val[0]); int y1 = Integer.parseInt(val[1]);
					int x2 = Integer.parseInt(val[2]); int y2 = Integer.parseInt(val[3]);
					int x3 = Integer.parseInt(val[4]); int y3 = Integer.parseInt(val[5]);
					int x4 = Integer.parseInt(val[6]); int y4 = Integer.parseInt(val[7]);
					t.setSdoGeomFld(i+1, new SdoGeom(x1, y1, x2, y2, x3, y3, x4, y4));
					break;
				}
			}
			t.print(ct.colTypes);
			f.insertRecord(t.returnTupleByteArray());
		} 
		catch (Exception e) 
		{
			System.err.println("*** error in Tuple.setfld() ***");
			e.printStackTrace();
			return false;
		} 
		return true;
	}
	public AttrType getAttrTypeFromString(String type)
	{
		AttrType attrType;
		if(type.matches("INTEGER"))
		{
			attrType = new AttrType(AttrType.attrInteger);
		}
		else if (type.matches("REAL"))
		{
			attrType = new AttrType(AttrType.attrReal);
		}
		else if (type.matches("VARCHAR"))
		{
			attrType = new AttrType(AttrType.attrString);
		}
		else if(type.matches("SDOGEOM"))
		{
			attrType = new AttrType(AttrType.attrGeom);
		}
		else attrType = null;
		return attrType;
	}
	public TableObject getTableDetails(String tableName)
	{
		String types[] = {"VARCHAR", "VARCHAR", "VARCHAR", "INTEGER"};
		ColTypesAndSizes ct = getColTypeAndSize(types);
		FldSpec [] projection = new FldSpec[4];
		for(int i=0; i<4; ++i)
		{
			projection[i] = new FldSpec(new RelSpec(RelSpec.outer), i+1);
		}
		CondExpr expr[] = new CondExpr[2];
		expr[0] = new CondExpr();
		expr[0].next  = null;
		expr[0].op    = new AttrOperator(AttrOperator.aopEQ);
		expr[0].type1 = new AttrType(AttrType.attrSymbol);
		expr[0].type2 = new AttrType(AttrType.attrString);
		expr[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),1);
		expr[0].operand2.string = new String (tableName);
		FileScan fs = null;
		try 
		{
			fs  = new FileScan("metadata.in", ct.colTypes, ct.colSizes, (short)4, (short)4, projection, expr);
		}
		catch (Exception e) 
		{
			System.err.println (""+e);
		}
		Tuple t;
		int count =0;
		Map<Integer, String> colId = new HashMap<Integer, String>();
		Map<String, String> typeList = new HashMap<String, String>();
		String tName=null;
		try 
		{
			while((t=fs.get_next()) != null)
			{
				tName = t.getStrFld(1);
				String cname = t.getStrFld(2);
				String ctype = t.getStrFld(3);
				Integer cid = t.getIntFld(4);

				//				System.out.print(tName);
				//				System.out.print(cname);
				//				System.out.print(ctype);
				//				System.out.println(cid);

				colId.put(cid, cname);
				typeList.put(cname, ctype);
				count++;
			}
		}
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return new TableObject(tName, count, colId, typeList);
	}
	public FileScan select (String tableName, CondExpr[] expr)
	{
		TableObject table = getTableDetails(tableName);
		if(table.columnId.size() == 0)
		{
			System.err.println("Table: "+tableName+" not found");
			return null;
		}
		String types[] = new String[table.columnId.size()];
		for (int i=0; i<types.length; ++i)
		{
			types[i] = new String(table.columnTypes.get(table.columnId.get(i)));
		}
		ColTypesAndSizes ct = getColTypeAndSize(types);
		//String s[] = {"Name", "location"};

		//		FldSpec [] projection = new FldSpec[s.length];
		//	    for(int i=0; i<projection.length; ++i)
		//	    {
		//	    	projection[i] = new FldSpec(new RelSpec(RelSpec.outer), 1+table.columnName.get(s[i]));
		//	    }
		FldSpec [] projection = new FldSpec[ct.colTypes.length];
		for(int i=0; i<projection.length; ++i)
		{
			projection[i] = new FldSpec(new RelSpec(RelSpec.outer), i+1);
		}
		FileScan am = null;
		try 
		{
			am  = new FileScan(tableName+".in", ct.colTypes, ct.colSizes, (short)ct.colTypes.length, (short)projection.length, projection, expr);
		}
		catch (Exception e) 
		{
			System.err.println (""+e);
		}
		Tuple t;
		try 
		{
			while ((t = am.get_next()) != null)
			{
				t.print(ct.colTypes);
			}
		}
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return am;
	}
	public static void main(String args[])
	{
		TableOperations test = new TableOperations();
		List<String> cn = new ArrayList<String>();
		List<String> cn1 = new ArrayList<String>();
		List<String> ct = new ArrayList<String>();
		List<String> v1 = new ArrayList<String>();
		List<String> v2 = new ArrayList<String>();
		List<String> v3 = new ArrayList<String>();
		List<String> v4 = new ArrayList<String>();
		List<String> v5 = new ArrayList<String>();
		cn.add("Name"); cn.add("Age"); cn.add("salary"); cn.add("location");
		cn1.add("Name"); cn1.add("location"); cn1.add("Age"); cn1.add("salary");
		ct.add("VARCHAR"); ct.add("INTEGER"); ct.add("INTEGER"); ct.add("SDOGEOM");
		v1.add("nav1"); v1.add("0,0,1,1,2,2,9,9"); v1.add("26"); v1.add("1000");
		v2.add("nav2"); v2.add("27"); v2.add("2000"); v2.add("0,0,1,1,2,2,5,5");
		v3.add("nav3"); v3.add("28"); v3.add("3000"); v3.add("0,0,1,1,2,2,6,6");
		v4.add("nav4"); v4.add("29"); v4.add("4000"); v4.add("0,0,1,1,2,2,7,7");
		v5.add("nav5"); v5.add("22"); v5.add("5000"); v5.add("0,0,1,1,2,2,8,8");
		test.createTable("Navneet", cn, ct);
		test.createTable("Ak", cn, ct);
		Map<String, String> m1  = new HashMap<String, String>();
		Map<String, String> m2  = new HashMap<String, String>();
		for(int i=0; i<4; ++i)
		{
			m1.put(cn1.get(i), v1.get(i));
			m2.put(cn.get(i), v2.get(i));
		}
		test.insertIntoTable("Navneet", m1);
		test.insertIntoTable("Navneet", m2);
		test.insertIntoTable("Ak", m1);
		test.insertIntoTable("Navneet", v3);
		test.insertIntoTable("Navneet", v4);
		System.out.println("Selecting....");
		test.select("Navneet", null);
	}
}