package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tokenizer.Token;
import tokenizer.Tokenizer;
import tests.TableOperations;
import vo.ColumnDataPair;
import vo.ColumnValuePair;
import vo.CreateQuery;
import vo.InsertQuery;

public class Parser
{
	private CreateParser createParser;
	private InsertParser insertParser;
	private SelectParser selectParser;
	private Tokenizer tokenizer;
	private Token ttype;
	
	public Parser()
	{
		tokenizer = Tokenizer.getInstance();
		
	}
	public void parse_sql()
	{
		boolean flag = true;
		while(flag)
		{
			boolean result = false;
			System.out.println("=======================================");
			System.out.println("Please enter a query ('EXIT' to finish)");
			ttype = tokenizer.getToken();
			switch(ttype)
			{
			case CREATE:
			{
				tokenizer.ungetToken();
				createParser = new CreateParser();
				result = createParser.parse_create();
				System.out.println(result?"Result: Correct Syntax":"Result: Syntax Error");
				if(result)
                                {
					createParser.getQueryValues().display();
                                        if(createParser.createTable())
                                            System.out.println("Table creation successful.");
                                        else
                                            System.out.println("Table creation failure.");
                                }
			}
			break;
			case INSERT:
			{
				tokenizer.ungetToken();
				insertParser = new InsertParser();
				result = insertParser.parse_insert();
				System.out.println(result?"Result: Correct Syntax":"Result: Syntax Error");
				if(result)
                                {
					insertParser.getQueryValues().display();
                                        if(insertParser.insertValues())
                                            System.out.println("Insertion successful.");
                                        else
                                            System.out.println("Insertion failure.");
                                        
                                }
			}
			break;
			case SELECT:
			{
				tokenizer.ungetToken();
				selectParser = new SelectParser();
				result = selectParser.parse_select();
				System.out.println(result?"Result: Correct Syntax":"Result: Syntax Error");
				if(result)
                                {
					selectParser.getQueryValues().display();
                                        //List<String> selectResultSet=selectParser.getSelectedValues();
                                       
                                        selectParser.printSelectedValues();
                                        
                                        //for(String selectedColumns : selectResultSet)
                                        //    System.out.println(selectedColumns);
                                }
			}
			break;
			case EXIT:
			{
				flag = false;
			}
			break;
			default:
				System.out.println("Invalid query: Supported queries are CREATE TABLE, SELECT, INSERT.");
			}
		}
	}

	public static void main(String[] args)
	{
		Parser p = new Parser();
		p.parse_sql();
	}
}
