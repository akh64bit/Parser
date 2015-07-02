
package parser;

import tokenizer.Token;
import tokenizer.Tokenizer;


public class Parser {
    
    private static CreateParser createParser;
    private static InsertParser insertParser;
    private static SelectParser selectParser;
    private static Tokenizer tokenizer;
    private static Token ttype;
    
    public Parser()
    {
        tokenizer = Tokenizer.getInstance();
    }

    
    public static void main(String[] args) 
    {
            
            Parser parser=new Parser();
            
            ttype = tokenizer.getToken();
            
            switch(ttype)
            {
                case CREATE:
                    tokenizer.ungetToken();
                    createParser = new CreateParser();
                    System.out.println(createParser.parse_create());
                    break;
                case INSERT:
                    tokenizer.ungetToken();
                    insertParser = new InsertParser();
                    System.out.println(insertParser.parse_insert());
                    break;
                case SELECT:
                    tokenizer.ungetToken();
                    selectParser = new SelectParser();
                    System.out.println(selectParser.parse_select());
                    break;
                default:
                    System.out.println("Invalid query: Supported queries are CREATE TABLE, SELECT, INSERT.");
            }
    }
    
}
