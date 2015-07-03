package tokenizer;

public enum Token
{
	EQUAL_TO("="), SELECT("SELECT"), FROM("FROM"), CREATE(
			"CREATE"), TABLE("TABLE"), INSERT("INSERT"), INTO("INTO"), NOT_EQUAL_TO(
			"!="), GREATER(">"), LESS("<"), GREATER_EQUAL(">="), LESS_EQUAL(
			"<="), VARCHAR("VARCHAR"), SDO_GEOM("SDO_GEOM"), INTEGER("INTEGER"), REAL(
			"REAL"), VALUES("VALUES"), VAL("VAL"), ID("ID"), WHERE("WHERE"), AREA("SDO_GEOM.SDO_AREA"),
			DISTANCE("SDO_GEOM.SDO_DISTANCE"), INTERSECTION("SDO_GEOM.SDO_INTERSECTION"), SEMICOLON(";"),
			COMMA(","), LPAREN("("), RPAREN(")"), AND("AND"), OR("OR"), ON("ON"), INDEX("INDEX"), SDO_GEOMV1("SDO_GEOMETRY"),
			INDEXTYPE("INDEXTYPE"), IS("IS"), SPATIAL_INDEX("MDSYS.SPATIAL_INDEX"), EXIT("EXIT");
	private String token;
	private Token(String token) 
	{
		this.token = token;
	}
	public String getValue(String token)
	{
		return getToken(token).token;
	}
	public String getValue()
	{
		return token;
	}
	public static Token getToken(String token) 
	{ // implement own valueOf like method for force pattern match of = or other reserved tokens
		switch (token) 
		{
		case "EXIT":
			return Token.EXIT;
		case "ON":
			return Token.ON;
		case "MDSYS.SPATIAL_INDEX":
			return Token.SPATIAL_INDEX;
		case "IS":
			return Token.IS;
		case "INDEXTYPE":
			return Token.INDEXTYPE;
		case "SDO_GEOMETRY":
			return Token.SDO_GEOMV1;
		case "CREATE":
			return Token.CREATE;
		case "TABLE":
			return Token.TABLE;
		case "INDEX":
			return Token.INDEX;
		case "INSERT":
			return Token.INSERT;
		case "INTO":
			return Token.INTO;
		case "VALUES":
			return Token.VALUES;                    
		case "AND":
			return Token.AND;
		case "OR":
			return Token.OR;
		case "(":
			return Token.LPAREN;
		case ")":
			return Token.RPAREN;
		case ",":
			return Token.COMMA;
		case ";":
			return Token.SEMICOLON;
		case "=":
			return Token.EQUAL_TO;
		case "SELECT":
			return Token.SELECT;
		case "FROM":
			return Token.FROM;
		case "<":
			return Token.LESS;
		case ">":
			return Token.GREATER;
		case ">=":
			return Token.GREATER_EQUAL;
		case "<=":
			return Token.LESS_EQUAL;
		case "INTEGER":
			return Token.INTEGER;
		case "REAL":
			return Token.REAL;
		case "SDO_GEOM":
			return Token.SDO_GEOM;
		case "VARCHAR":
			return Token.VARCHAR;
		case "WHERE":
			return Token.WHERE;
		case "SDO_GEOM.SDO_AREA":
			return Token.AREA;
		case "SDO_GEOM.SDO_DISTANCE":
			return Token.DISTANCE;
		case "SDO_GEOM.SDO_INTERSECTION":
			return Token.INTERSECTION;
		default:
			return checkValOrId(token);
		}
	}
	private static Token checkValOrId(String token) 
	{
		Token tok=null;
		if (token.matches("[A-z]+(.[A-z]+)?") || token.matches("^[A-z]+([0-9]|[A-z])*(.[A-z]+([0-9]|[A-z])*)?")) 
		{
			tok = Token.ID;
		}
		else if (token.matches("\\d+") || token.matches("\\d+\\.\\d+") ||
				token.matches("^'[A-z]+(.[A-z]+)?'$") || token.matches("SDO_GEOM\\((\\d+[,]){7}\\d+\\)")) 
		{
			tok = Token.VAL;
		}
		return tok;
	}
}