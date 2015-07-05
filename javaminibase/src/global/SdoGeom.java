package global;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class SdoGeom 
{
	int x1, y1, x2, y2, x3, y3, x4, y4;
	
	public SdoGeom(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4)
	{
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.x3 = x3;
		this.y3 = y3;
		this.x4 = x4;
		this.y4 = y4;
	}
	
	public SdoGeom (byte[] b) throws IOException
	{
		InputStream in = new ByteArrayInputStream(b);
		DataInputStream instr = new DataInputStream (in);
		x1 = instr.readInt();
		y1 = instr.readInt();
		x2 = instr.readInt();
		y2 = instr.readInt();
		x3 = instr.readInt();
		y3 = instr.readInt();
		x4 = instr.readInt();
		y4 = instr.readInt();
	}
	
	public byte[] getByteArray() throws IOException
	{
		OutputStream out = new ByteArrayOutputStream();
		DataOutputStream outstr = new DataOutputStream (out);
		
		outstr.writeInt(x1);
		outstr.writeInt(y1);
		outstr.writeInt(x2);
		outstr.writeInt(y2);
		outstr.writeInt(x3);
		outstr.writeInt(y3);
		outstr.writeInt(x4);
		outstr.writeInt(y4);
		
		return ((ByteArrayOutputStream) out).toByteArray();
		
	}
	
	public String toString()
	{
		StringBuilder s =  new StringBuilder();
		s.append("("); s.append(x1); s.append(","); s.append(y1); s.append(")");
		s.append("("); s.append(x2); s.append(","); s.append(y2); s.append(")");
		s.append("("); s.append(x3); s.append(","); s.append(y3); s.append(")");
		s.append("("); s.append(x4); s.append(","); s.append(y4); s.append(")");
		return s.toString();
	}
}
