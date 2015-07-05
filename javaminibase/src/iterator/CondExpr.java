package iterator.sm_join_assign_src;

import global.*;
/**
 *  This clas will hold single select condition
 *  It is an element of linked list which is logically
 *  connected by OR operators.
 */
public class CondExpr 
{
  /**
   * Operator like "<"
   */
  public AttrOperator op;    
  /**
   * Types of operands, Null AttrType means that operand is not a
   * literal but an attribute name
   */    
  public AttrType     type1;
  public AttrType     type2;    
  /**
   *the left operand and right operand 
   */ 
  public Operand operand1;
  public Operand operand2;
  /**
   * Pointer to the next element in linked list
   */    
  public CondExpr    next;   
  /**
   *constructor
   */
  public  CondExpr() 
  {
    operand1 = new Operand();
    operand2 = new Operand();
    operand1.integer = 0;
    operand2.integer = 0;
    next = null;
  }
  public String toString()
  {
	  StringBuilder b = new StringBuilder();
	  b.append("Operand1: ").append(operand1.string).append("; ");
	  b.append("operator: ").append(op.toString()).append("; ");
	  b.append("Operand2: ");
	  switch(type2.attrType)
	  {
	  case AttrType.attrInteger:
		  b.append(operand2.integer).append(" (").append(type2.toString()).append(");");
		  break;
	  case AttrType.attrReal:
		  b.append(operand2.real).append(" (").append(type2.toString()).append(");");
		  break;
	 default:
		 b.append(operand2.string).append(" (").append(type2.toString()).append(");");
	  }
	  return b.toString();
  }
}