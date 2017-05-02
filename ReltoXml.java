import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.*;
import java.util.*;
import java.sql.*;
import java.sql.DatabaseMetaData.*;
import org.w3c.dom.*;
public class ReltoXml implements ActionListener {
	public static ReltoXml obj;
	public JTextArea display;
	public JTextField uidfield, table;
	public JPasswordField pwdfield;
	public String uid,pwd,tablename;
	public ResultSetMetaData rsm;
	public ResultSet rs;
	public DatabaseMetaData dmd;
	public Connection con;
	public String tabnames="";
	public Statement stmt;
	public ReltoXml(){
		   JFrame frame1=new JFrame("CONVERSION OF RELATIONAL TO XML DATA");
		   frame1.pack();
		  
		   JPanel pane1=new JPanel(new GridBagLayout());
		   JPanel pane2=new JPanel(new GridBagLayout());
		   JPanel pane3=new JPanel(new GridBagLayout());
		   pane1.setBackground(Color.CYAN);
		   pane2.setBackground(Color.GREEN);
                   Color cc=new Color(255,255,100);
		    pane3.setBackground(cc);
		   JLabel uidlabel=new JLabel("USER ID");
		    uidfield=new JTextField(10);
		   JLabel pwdlabel=new JLabel("PASSWORD");
		    pwdfield=new JPasswordField(10);
		   JLabel tablabel=new JLabel("ENTER TABLE NAME");
		    table=new JTextField(10);
		   
		   JButton connect=new JButton("Connect");
		   JButton displaytable=new JButton("Display-Table");
		   JButton displayxml=new JButton("Display-XML");
		   JButton displayschema=new JButton("Display-schema");
		   JButton insert=new JButton("Insertion");
		   JButton update=new JButton("Updation");
		   JButton delete=new JButton("Deletion");
		   display=new JTextArea(30,50);
		   
		   GridBagConstraints c=new GridBagConstraints();
		   c.insets=new Insets(10,10,10,10);
		 
		   c.gridx=0;
		   c.gridy=0;
		   pane1.add(uidlabel,c);
		   c.gridx=1;
		   c.gridy=0;
		   pane1.add(uidfield,c);
		   c.gridx=2;
		   c.gridy=0;
		   pane1.add(pwdlabel,c);
		   c.gridx=3;
		   c.gridy=0;
		   pane1.add(pwdfield,c);
		   c.gridx=4;
		   c.gridy=0;
		   pane1.add(connect);
		   c.gridx=5;
		   c.gridy=0;
		   pane1.add(tablabel);
		   c.gridx=6;
		   c.gridy=0;
		   pane1.add(table);
		   frame1.add(pane1,BorderLayout.NORTH);
		  
		  
		   c.gridx=0;
		   c.gridy=2;
		   pane2.add(displaytable,c);
		   c.gridx=0;
		   c.gridy=3;
		   pane2.add(displayxml,c);
		   c.gridx=0;
		   c.gridy=4;
		   pane2.add(displayschema,c);
		   c.gridx=0;
		   c.gridy=5;
		   pane2.add(insert,c);
		   c.gridx=0;
		   c.gridy=6;
		   pane2.add(update,c);
		   c.gridx=0;
		   c.gridy=7;
		   pane2.add(delete,c);
		   frame1.add(pane2,BorderLayout.WEST);
		  
		   c.gridx=0;
		   c.gridy=4;
		   pane3.add(display,c);
		  
		   frame1.add(pane3);

        	   connect.addActionListener(this);
		   displaytable.addActionListener(this);
		   displayxml.addActionListener(this);
		   displayschema.addActionListener(this);
		   insert.addActionListener(this);
		   update.addActionListener(this);
		   delete.addActionListener(this);

		   frame1.setSize(950,700);
		   frame1.setVisible(true);
		   frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		   
	}
	public void datatable()
	{
          
		try
		{
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection con=DriverManager.getConnection("jdbc:odbc:project",uid,pwd);
		stmt=con.createStatement();
		dmd=con.getMetaData();
		tabnames="TABLE NAMES\n";
		ResultSet t=stmt.executeQuery("select * from tab");
		
		while(t.next()){     
		 String s1=t.getString(1);
		 System.out.println(s1);
		 tabnames=tabnames+s1+"\n";  
		 }  
						
		rs=stmt.executeQuery("select * from "+tablename);
		rsm=rs.getMetaData();
		int nc=rsm.getColumnCount();

		  
		}catch(Exception e)
		{ System.out.println(" FROM MAIN================"+e);}

	}
	
	

	public static void main(String args[])
	{
		obj=new ReltoXml();
	}

//===========ACTION PERFORMED======================

	public void actionPerformed(ActionEvent e) 
	{   
		String s=e.getActionCommand();
	
		if(s.equals("Connect"))
		{ 	
			uid=uidfield.getText();
			pwd=pwdfield.getText();
			tablename=table.getText();
			datatable();
			display.setText(tabnames);
		        Xmldata xml=new Xmldata();
			xml.createxml(rs,rsm,tablename,dmd); 
          	}
		else if(s.equals("Display-Table"))
		{ 
		  	table t=new table();
		  	
		  	t.tabularformat(stmt,tablename,obj);
		}
		else if(s.equals("Display-XML"))
		{ 
			
				display.setText(s);
				String data="";
		try{	
			tablename=table.getText();
			FileInputStream fin=new FileInputStream(tablename+".xml");
			BufferedReader br=new BufferedReader(new InputStreamReader(fin));
			String line=br.readLine();
			while(line!=null)
			{ 
			 data=data+line+"\n";
			 line=br.readLine();
			}
		   }catch(Exception e1)
		     {	
		       System.out.println(e1);
		     }
			
			display.setText(data);
		}
		else if(s.equals("Display-schema"))
		{ 
			display.setText(s);
			String data="";
		try{	
		        tablename=table.getText();
			FileInputStream fin=new FileInputStream(tablename+"schema.txt");
			BufferedReader br=new BufferedReader(new InputStreamReader(fin));
			String line=br.readLine();
			while(line!=null)
			{ 
			 data=data+line+"\n";
			 line=br.readLine();
			}
		   }catch(Exception e1)
		    {	
		     System.out.println(e1);
		    }
		   display.setText(data);
		}
		else if(s.equals("Insertion"))
		{
		    	inserting insert=new inserting();
			insert.insertingnode(rs,rsm,tablename,stmt);
		}
		else if(s.equals("Updation"))
		{
		 	Updation u=new Updation();
		   	u.update(rs,rsm,tablename,stmt);
		   	
		}
		else if(s.equals("Deletion"))
		{
		 	Deletingnode d=new Deletingnode();
		   	d.delete(rs,rsm,tablename,stmt); 	
		}
		else
		{
			display.setText("");
		}
	}
	
}
// ---tabular format--------------------------------------------
class table
{   public ResultSet rs;
	public ResultSetMetaData rsm;
	public void tabularformat(Statement stmt,String tablename,ReltoXml ob)
	{
		String values="";
		try{ rs=stmt.executeQuery("select * from "+tablename);
			rsm=rs.getMetaData();
		 int nc=rsm.getColumnCount();
		 int i=1;
		 for(i=1;i<=nc;i++)
		    {
		      String cn=rsm.getColumnName(i);
		      values=values+cn+"\t";
		    }
        	 values=values+"\n";
         
		 
		 while(rs.next())
		   {
		      for(i=1;i<=nc;i++)
		        {
		          values=values+rs.getString(i)+"\t";
			}
			values=values+"\n";
		   }
		 ob.display.setText(values);
       	  }catch(Exception e){System.out.println(e);}
        }
	
}

//----------------------------------------------------------------
// CREATION XML-&- SCHEMA ===========================================
class Xmldata
 {
	public String tablename;
	public static void main(String args[])
	{ 
	
		String uid,pwd;
		boolean flag=true;
	
	}
	
	public void createxml(ResultSet rs,ResultSetMetaData rsm, String tablename, DatabaseMetaData dmd){
		
	try {
		 int nc;
		 String cn;
		 nc=rsm.getColumnCount();
		 int i=1;
		 String data="\n";

           	 File f1=new File(tablename+".xml");
		 OutputStream fout=new FileOutputStream(f1);
           	 BufferedWriter bf=new BufferedWriter(new OutputStreamWriter(fout));
           
           	 bf.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	         bf.newLine();
		 bf.write("<"+tablename+"data>");
		 bf.newLine();
		 while(rs.next())
		   {
		   	bf.write("<"+tablename+">");
                        bf.newLine();
		   	
		   	for(i=1;i<=nc;i++)
		         {
		          cn=rsm.getColumnName(i);
		          bf.write("\t<"+cn+">"+rs.getString(i)+"</"+cn+">");
                          bf.newLine();
		         }
		   	bf.write("</"+tablename+">");
                        bf.newLine();
		  }
		   bf.write("</"+tablename+"data>");
	           bf.close();
	         fout.close();
	         f1=null;  
	        
	    // Generating xmlschema    
	          
	       ResultSet rs2=dmd.getPrimaryKeys(null,null,tablename);
                i=1;
		    String[] primarykeys=new String[30];
                while(rs2.next())
                  {
                       primarykeys[i]=rs2.getString("COLUMN_NAME");
                       i++;
                   }
                   int npk=i;
                   
                   
                    ResultSet rs1 = dmd.getExportedKeys(null,null,tablename);
                    String fkcolumnname[]=new String[40];
                    String fktablename[]=new String[40];
                    i=1;
     			while(rs1.next()) {
     			  	 fktablename[i] = rs1.getString("FKTABLE_NAME");
     			  	 fkcolumnname[i] = rs1.getString("FKCOLUMN_NAME");
    			  	 i++;
     				}
     			  int nfk=i;
     			  String filename=tablename+"schema.txt";
     			 
     		     File f2=new File(filename);
		     OutputStream fout2=new FileOutputStream(f2);
                     BufferedWriter bf2=new BufferedWriter(new OutputStreamWriter(fout2));
                  
                     bf2.write("<?xml version='1.0' ?>");
                     bf2.newLine();
                     bf2.write("<xs:schema xmlns:xs=\"http://www.w3.org/2001/xmlschema\" >");
                     bf2.newLine();
                     bf2.write(" <xs:element name=\""+tablename+"\">");
                     bf2.newLine();
                  
                     bf2.write("  <xs:complexType>");
                     bf2.newLine();
                     bf2.write("   <xs:sequence>");
                     bf2.newLine();
                  
              
                    nc=rsm.getColumnCount();
                    String type=" ";
                    String columntype=" ";
                    for(i=1;i<=nc;i++)
                     {
                  	bf2.write("\t<xs:element name=\""+rsm.getColumnName(i)+"\" type=");
                  	
                  	columntype=rsm.getColumnTypeName(i);
                  	
                  	if(columntype.equalsIgnoreCase("VARCHAR2")||columntype.equals("CHARACTER"))
                  	type="xs:String";
                  	
                  	if(columntype.equalsIgnoreCase("NUMBER"))
                  	type="xs:integer";
                  	
                  	if(columntype.equalsIgnoreCase("INTEGER"))
                  	type="xs:integer";
                  	
                  	if(columntype.equalsIgnoreCase("DECIMAL"))
                  	type="xs:decimal";
                  	
                  	if(columntype.equalsIgnoreCase("DATE"))
                  	type="xs:date";
                  	
                  	if(columntype.equalsIgnoreCase("TIME"))
                        type="xs:time";
                  	
                  	bf2.write("\""+type+"\" />");
                  	bf2.newLine();
                  }
                  
                   bf2.write("   </xs:sequence>");
                   bf2.newLine();
                   bf2.write("  </xs:complexType>");
                   bf2.newLine();
                   
                   // primary keys in schema
                   
                   for(i=1;i<npk;i++)
                   {
                
                   bf2.write("  <xs:key name=");
                   bf2.write("\"newdatasetkey"+i+"\" msdata:PrimaryKey=\"true\"> ");
                   bf2.newLine();
                   bf2.write("\t<xs:selector xpath=\".//"+tablename+"\" />");
                   bf2.newLine();
                   bf2.write("\t<xs:field xpath=\""+primarykeys[i]+"\" />");
                   bf2.newLine();
                   bf2.write("  </xs:key>");
                   bf2.newLine();
                
                   }
                   
                   
                   // foreign keys in schema
                   
                   for(i=1;i<nfk;i++)
                   {
                   	
                   	bf2.write("  <xs:keyref name=\"");
                   	bf2.write(fkcolumnname[i]+"\" refer=\"newDataSetkeyrefer"+i+"\" >");
                   	bf2.newLine();
                   	bf2.write("\t<xs:selector xpath=\".//"+fktablename[i]+"\" />");
                    bf2.newLine();
                    bf2.write("\t<xs:field xpath=\""+primarykeys[1]+"\" />");
                    bf2.newLine();
                    bf2.write("  </xs:keyref>");
                    bf2.newLine();
                    
                   }
                   
                   
                   bf2.write(" </xs:element>");
                   bf2.newLine();
                   bf2.write("</xs:schema>");
                   bf2.newLine();
                   
                   bf2.close();
		   
		   
	}
	catch(Exception e){
		System.out.println(e);
	}
 
  }
}
//====================================================================
	 //===================INSERTION==============
//*******************************************************************
class inserting implements ActionListener
{
	
	JLabel tablename1;
	JLabel col1;
	String[] columnnames=new String[20];
	JTextField tf[]=new JTextField [20];
	String[] val=new String[20];
	int nc;
	JFrame iframe;
	LayoutManager GridBagLayout;
	Statement stmt;
	String tablename;
	ResultSetMetaData rsm;
     public void insertingnode(ResultSet rs,ResultSetMetaData rsm1, String tablename2,Statement stmt1)
     { 
	try{  stmt=stmt1;
			tablename=tablename2;
			rsm=rsm1;
	rs=stmt.executeQuery("select * from "+tablename);
	rsm=rs.getMetaData();
	nc=rsm.getColumnCount();
	System.out.println("nc "+nc);

	iframe=new JFrame("Insert data");

	   
	   iframe.setSize(500,500);
	   iframe.setVisible(true);
	  
	   JPanel ipanel1=new JPanel(new GridBagLayout());
	   
	   ipanel1.setBackground(Color.CYAN);
	   tablename1=new JLabel(tablename);
	   
	   GridBagConstraints c=new GridBagConstraints();
	   c.insets=new Insets(10,10,10,10);
	   
	   int x=0,y=0;
	   c.gridx=x;
	   c.gridy=y;
	   ipanel1.add(tablename1,c);
	   
	  
	   
	   for(int i=0;i<nc;i++)
	   {
		   x=0;
		   columnnames[i]=rsm.getColumnName(i+1);
		  col1=new JLabel(columnnames[i]);
		   
		   y++;
		   c.gridx=x;
		   c.gridy=y;
		   ipanel1.add(col1,c);
		   
		   
		   x++;
		   tf[i]=new JTextField(10);
		   
		   c.gridx=x;
		   c.gridy=y;
		   ipanel1.add(tf[i],c);
		   
	   }
	  x=0;
	  y=y+1;
	   JButton button=new JButton("Insert");
	   button.setBackground(Color.LIGHT_GRAY);
	   c.gridx=x;
	   c.gridy=y;
	   ipanel1.add(button,c);
	   iframe.add(ipanel1,BorderLayout.CENTER);
	   
	   button.addActionListener(this);

	}catch(Exception e)
	{
	  System.out.println("inside insertion "+e);
	}
	
     }
     public void actionPerformed(ActionEvent e) 
	{
		try{
		
		int i;
		for(i=0;i<nc;i++)
		{
			val[i]=new String(tf[i].getText());
			System.out.println("val "+val[i]);
		}
		
		//sql insertion
		
		String datavalues="'"+val[0]+"'";
	       for(i=1;i<nc;i++)
	       {
	       	datavalues+=",";
	       datavalues+="'"+val[i]+"'";
	       
	       }	
	    stmt.executeUpdate("insert into "+tablename+" values("+datavalues+")");
	
		
		//xml insertion
		
		String xmlfilename=tablename;
		xmlfilename=xmlfilename+".xml";
		
		DocumentBuilderFactory dom=DocumentBuilderFactory.newInstance();
		dom.setValidating(false);
		dom.setIgnoringElementContentWhitespace(true);
		DocumentBuilder build=dom.newDocumentBuilder();
		Document tabledoc=build.parse(new File(xmlfilename));
		
	
		
		Element subnode=tabledoc.createElement(tablename);
		i=1;
	       for(i=0;i<nc;i++)
		{
			Element subsubnode=tabledoc.createElement(columnnames[i]);
			Text subsubvalue=tabledoc.createTextNode(val[i]);
			subsubnode.appendChild(subsubvalue);
			subnode.appendChild(subsubnode);
		}	
		
		NodeList oldnode=tabledoc.getElementsByTagName(tablename);

		
		oldnode.item(0).getParentNode().insertBefore(subnode,oldnode.item(0));
		
	Transformer tf1 = TransformerFactory.newInstance().newTransformer();
	tf1.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	tf1.setOutputProperty(OutputKeys.INDENT, "yes");
	StreamResult result=new StreamResult(new FileOutputStream(xmlfilename));
	tf1.transform(new DOMSource(tabledoc),result);

	for(i=0;i<nc;i++)
		tf[i].setText("");
        
        }catch(Exception e1){
        	System.out.println(e1+"INSERT XML NODE");
        }
	}
}

//===================================================================
	//===================UPDATION==============
//********************************************************************
class Updation implements ActionListener
{
	
	JLabel tablename1;
	JLabel col1;
	String[] columnnames=new String[20];
	JTextField tf[]=new JTextField [4];
	String[] val=new String[4];
	int nc;
	JFrame iframe;
	LayoutManager GridBagLayout;
	Statement stmt;
	String tablename;
	ResultSetMetaData rsm;
     public void update(ResultSet rs,ResultSetMetaData rsm1, String tablename2,Statement stmt1)
     { 
	try{  
			stmt=stmt1;
			tablename=tablename2;
			rsm=rsm1;
		rs=stmt.executeQuery("select * from "+tablename);
		rsm=rs.getMetaData();
		nc=rsm.getColumnCount();
		System.out.println("nc "+nc);

	   iframe=new JFrame("Update data");
	   iframe.setSize(500,500);
	   iframe.setVisible(true);
	  
	   JPanel ipanel1=new JPanel(new GridBagLayout());
	   
	   ipanel1.setBackground(Color.PINK);
	   
	   String clnms=tablename+":: COLS :";
	 
	   int x;
	   for(int i=0;i<nc;i++)
	   {
	        columnnames[i]=rsm.getColumnName(i+1);
		clnms=clnms+" "+(i+1)+" "+columnnames[i]; 
	   }   
		tablename1=new JLabel(clnms);
		GridBagConstraints c=new GridBagConstraints();
	   	c.insets=new Insets(10,10,10,10);
	   
	   int y=0;
	   x=0;
	   c.gridx=x;
	   c.gridy=y;
	  ipanel1.add(tablename1,c);
	  
		  col1=new JLabel("ENTER COLM NAME");
		   y++;
		   c.gridx=x;
		   c.gridy=y;
		   ipanel1.add(col1,c);
		    x++;
		   tf[0]=new JTextField(10);
		   c.gridx=x;
		   c.gridy=y;
		   ipanel1.add(tf[0],c);

		   x=0;
		   col1=new JLabel("ENTER VALUE");
		   y++;
		   c.gridx=x;
		   c.gridy=y;
		   ipanel1.add(col1,c);
		    x++;
		   tf[1]=new JTextField(10);
		   c.gridx=x;
		   c.gridy=y;
		   ipanel1.add(tf[1],c);

		   x=0;
		   col1=new JLabel("ENTER COLM NAME TO UPDATE");
		   y++;
		   c.gridx=x;
		   c.gridy=y;
		   ipanel1.add(col1,c);
		   x++;
		   tf[2]=new JTextField(10);
		   c.gridx=x;
		   c.gridy=y;
		   ipanel1.add(tf[2],c);

		   x=0;
		   col1=new JLabel("ENTER VALUE TO UPDATE");
		   y++;
		   c.gridx=x;
		   c.gridy=y;
		   ipanel1.add(col1,c);		  
		   x++;
		   tf[3]=new JTextField(10);
		   c.gridx=x;
		   c.gridy=y;
		   ipanel1.add(tf[3],c);
	   
	  x=0;
	  y=y+1;
	  JButton button=new JButton("update");
	  button.setBackground(Color.LIGHT_GRAY);
	  c.gridx=x;
	  c.gridy=y;
	  ipanel1.add(button,c);
	  iframe.add(ipanel1,BorderLayout.CENTER);
	   
	  button.addActionListener(this);

	}catch(Exception e)
	{
	  System.out.println("inside updation "+e);
	}
	
    }
     public void actionPerformed(ActionEvent e) 
	{
		try{
		  String ucs=new String(tf[0].getText());
		  String uvs=new String(tf[1].getText());
		  String uc=new String(tf[2].getText());
		  String uv=new String(tf[3].getText());

		  // SQL Updation
		  stmt.executeUpdate("update "+tablename+" set "+uc+"='"+uv+"' where "+ucs+"="+uvs);
		
  		  // XML Updation
		  DocumentBuilderFactory doc=DocumentBuilderFactory.newInstance();
		 doc.setValidating(false);
		 doc.setIgnoringElementContentWhitespace(true);
			
		 String filename=tablename+".xml";
		 DocumentBuilder build=doc.newDocumentBuilder();
		 Document tabledoc=build.parse(new File(filename));
		 Element rootelement=tabledoc.getDocumentElement();
	         NodeList nodelist=tabledoc.getElementsByTagName(tablename);
	  
	         int tnn=nodelist.getLength(),i;
		 for(i=0;i<tnn;i++)
		  {
		   String valuei=tabledoc.getElementsByTagName(ucs).item(i).getTextContent();
		   if(uvs.equalsIgnoreCase(valuei))
		    {
			tabledoc.getElementsByTagName(uc).item(i).setTextContent(uv);
		    }
		  }
		Transformer tf1= TransformerFactory.newInstance().newTransformer();
		tf1.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		tf1.setOutputProperty(OutputKeys.INDENT, "yes");
		StreamResult result=new StreamResult(new FileOutputStream(filename));
		tf1.transform(new DOMSource(tabledoc),result);
		for(i=0;i<4;i++)
		  tf[i].setText("");

        }catch(Exception e1){
        	System.out.println(e1+" in UPDATION");
        }
	}
}
//===================================================================
	//===================Deletion==============
//********************************************************************************

class Deletingnode implements ActionListener
{
	
	JLabel tablename1;
	JLabel col1;
	String[] columnnames=new String[20];
	JTextField tf[]=new JTextField [4];
	int nc;
	JFrame iframe;
	LayoutManager GridBagLayout;
	Statement stmt;
	String tablename;
	ResultSetMetaData rsm;

     public void delete(ResultSet rs,ResultSetMetaData rsm1, String tablename2,Statement stmt1)
     { 
	try{  
			stmt=stmt1;
			tablename=tablename2;
			rsm=rsm1;
		rs=stmt.executeQuery("select * from "+tablename);
		rsm=rs.getMetaData();
		nc=rsm.getColumnCount();
		System.out.println("nc "+nc);

	   iframe=new JFrame("Deleting node");
	   iframe.setSize(500,500);
	   iframe.setVisible(true);
	  
	   JPanel ipanel1=new JPanel(new GridBagLayout());
	   
	   ipanel1.setBackground(Color.ORANGE);
	   
	   String clnms=tablename+":: COLS :";
	 
	   int x;
	   for(int i=0;i<nc;i++)
	   {
	        columnnames[i]=rsm.getColumnName(i+1);
		clnms=clnms+" "+(i+1)+" "+columnnames[i]; 
	   }   
		tablename1=new JLabel(clnms);
		GridBagConstraints c=new GridBagConstraints();
	   	c.insets=new Insets(10,10,10,10);
	   
	   int y=0;
	   x=0;
	   c.gridx=x;
	   c.gridy=y;
	  ipanel1.add(tablename1,c);
	  
		  col1=new JLabel("ENTER COLM NAME");
		   y++;
		   c.gridx=x;
		   c.gridy=y;
		   ipanel1.add(col1,c);
		    x++;
		   tf[0]=new JTextField(10);
		   c.gridx=x;
		   c.gridy=y;
		   ipanel1.add(tf[0],c);

		   x=0;
		   col1=new JLabel("ENTER VALUE FOR DELETION");
		   y++;
		   c.gridx=x;
		   c.gridy=y;
		   ipanel1.add(col1,c);
		    x++;
		   tf[1]=new JTextField(10);
		   c.gridx=x;
		   c.gridy=y;
		   ipanel1.add(tf[1],c);
	   
	  x=0;
	  y=y+1;
	  JButton button=new JButton("Delete");
	  button.setBackground(Color.LIGHT_GRAY);
	  c.gridx=x;
	  c.gridy=y;
	  ipanel1.add(button,c);
	  iframe.add(ipanel1,BorderLayout.CENTER);
	   
	  button.addActionListener(this);

	}catch(Exception e)
	{
	  System.out.println("inside deletion "+e);
	}
	
    }
     public void actionPerformed(ActionEvent e) 
	{
		try{

		  String ucs=new String(tf[0].getText());
		  String uvs=new String(tf[1].getText());
		  //sql deletion
		stmt.executeUpdate("delete from "+tablename+" where "+ucs+"="+uvs);
			
		// xml deletion
	  DocumentBuilderFactory fact=DocumentBuilderFactory.newInstance();
	  fact.setValidating(false);
	  fact.setIgnoringElementContentWhitespace(true);
	  String filename=tablename+".xml";
	  DocumentBuilder build=fact.newDocumentBuilder();
	  Document tabledoc=build.parse(new File(filename));
	  Element rootelement=tabledoc.getDocumentElement();
	  NodeList nodelist=tabledoc.getElementsByTagName(tablename);
	 
	  int tnn=nodelist.getLength();
	  int i;
	  for(i=0;i<tnn;i++)  
	  {
		String valuen=tabledoc.getElementsByTagName(ucs).item(i).getTextContent();  
		
		if(uvs.equalsIgnoreCase(valuen))
		{
			rootelement.removeChild(nodelist.item(i));
			tnn--;
			
		}
	  }
		Transformer tf1 = TransformerFactory.newInstance().newTransformer();
		tf1.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		tf1.setOutputProperty(OutputKeys.INDENT, "yes");
		StreamResult result=new StreamResult(new FileOutputStream(filename));
		tf1.transform(new DOMSource(tabledoc),result);
	tf[0].setText("");	
        tf[1].setText("");

        }catch(Exception e1){
        	System.out.println(e1+" in DELETION");
        
        }
	}
}
//********************************************************************************