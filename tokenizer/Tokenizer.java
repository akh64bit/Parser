package tokenizer;

import java.util.Scanner;

public class Tokenizer 
{
	private Token token;
	private String tokenValue;
	private boolean readNextToken = true;
	private Scanner scanner;      // All white space delimiter
	private static Tokenizer tokenizer;
	private Tokenizer() 
	{
		scanner = new Scanner(System.in);
		scanner.useDelimiter("\\s+");
	}
	public static Tokenizer getInstance() 
	{
		if (tokenizer == null) 
		{
			tokenizer = new Tokenizer();
		}
		return tokenizer;
	}
	public Token getToken() 
	{
		if (readNextToken) 
		{
			readNextToken();
		}
		else 
		{
			readNextToken = true;
		}
		return token;
	}
	public void ungetToken() 
	{
		readNextToken = false;
	}
	private void readNextToken() 
	{
		tokenValue = scanner.next();
		token = Token.getToken(tokenValue);
	}
	public boolean hasNext() 
	{
		return scanner.hasNext();
	}
	public String getTokenValue() 
	{
		return tokenValue;
	}
}