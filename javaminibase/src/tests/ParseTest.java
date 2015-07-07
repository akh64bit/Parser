package tests;

import java.io.IOException;

import parser.Parser;
import global.GlobalConst;
import global.SystemDefs;

class parseDriver extends TestDriver implements GlobalConst
{
	private boolean OK = true;
	private boolean FAIL = false;
	private Parser parser;
	public parseDriver()
	{
		super("parsetest");
		parser = new Parser();
	}
	protected String testName () 
	{
		return "SQL Parser";
	}
	public boolean runTests () 
	{
		System.out.println ("\n" + "Running " + testName() + " tests...." + "\n");
		new SystemDefs( dbpath, 1000, 50, "Clock" );
		// Kill anything that might be hanging around
		String remove_cmd = "/bin/rm -rf ";
		// Commands here is very machine dependent.  We assume
		// user are on UNIX system here
		try 
		{
			Runtime.getRuntime().exec(remove_cmd + logpath);
			Runtime.getRuntime().exec(remove_cmd + dbpath);
		}
		catch (IOException e) 
		{
			System.err.println ("IO error: "+e);
		}
		//Run the tests. Return type different from C++
		TableOperations.getInstance();
		boolean _pass = runAllTests();
		//Clean up again
		try 
		{
			Runtime.getRuntime().exec(remove_cmd + logpath);
			Runtime.getRuntime().exec(remove_cmd + dbpath);
		}
		catch (IOException e) 
		{
			System.err.println ("IO error: "+e);
		}
		System.out.print ("\n" + "..." + testName() + " tests ");
		System.out.print (_pass==OK ? "completely successfully" : "failed");
		System.out.print (".\n\n");
		return _pass;
	}
	protected boolean test1()
	{
		System.out.print ("\n" + "Running " + testName() + " tests...." + "\n");
		try
		{
			parser.parse_sql();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return FAIL;
		}
		return OK;
	}
}
public class ParseTest 
{
	public static void main(String[] args) 
	{
		parseDriver test = new parseDriver();
		boolean flag = test.runTests();
		if(!flag)
		{
			System.err.println ("Error encountered during parser tests:\n");
			Runtime.getRuntime().exit(1);
		}
		Runtime.getRuntime().exit(0);
	}
}