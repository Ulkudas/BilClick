package db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import question.history.*;

/**
 * Singleton class History Manager to modify the history of the question
 * @author Aydem�r
 *
 */
public class HistoryManager 
{
	/**
	 * Query finals
	 */
	private final static String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	private final static String JDBS_URL = "jdbc:derby:BilClickData;create=true";

	/**
	 * Other finals
	 */
	final static int A = 0; 
	final static int B = 1;
	final static int C = 2; 
	final static int D = 3;
	final static int E = 4; 
	final static int F = 5;


	/** Public vonstructor 
	 */
	public  HistoryManager(){}


	/**
	 * Creates a table to hold the history for the question if the history does not already exist 
	 * Adds the new etry
	 * @param questionCode
	 * @param entry
	 */
	public static void questionAsked( String questionCode, Entry entry) throws ClassNotFoundException, SQLException
	{
		// Open a connection
		Class.forName(DRIVER);
		Connection conn = DriverManager.getConnection(JDBS_URL);

		// Create a table if does not already exis 
		if ( !hasRecordedHistory( questionCode))
		{
			conn.createStatement().execute("create table " +questionCode+ "( DATE_ASKED varchar(50),"
					+" PARTICIPANTS integer,"
					+" ANSWER_INDEX integer,"
					+" CHOICE_DISTRIBUTION varchar(100))");
		}


		// Modify the data to enter to the table and form the query 


		String query = "INSERT INTO " +questionCode+ " VALUES ( '";
		query += entry.getDateAsked() + "', ";
		query += "" +entry.getParticipants() + ", ";
		query += "" +entry.getAnswerIndex() + ", '";

		for ( int i = 0; i < entry.getChoiceDistribution().size(); i++)
		{
			if ( i == A )
				query += "A:" +entry.getChoiceDistribution().get(i);

			else if ( i == B)
				query += " B:" +entry.getChoiceDistribution().get(i);

			else if ( i == C) // if at C 
				query += " C:" +entry.getChoiceDistribution().get(i);

			else if ( i == D) // if at D 
				query += " D:" +entry.getChoiceDistribution().get(i);

			else if ( i == E) // if at E
				query += " E:" +entry.getChoiceDistribution().get(i);

			else if ( i == F) // if at G
				query += " F:" +entry.getChoiceDistribution().get(i);

		}

		query += "')";

		// TODO ...

		// Add the Entry to the table 
		conn.createStatement().execute( query); // execute the query
		System.out.println("Succsessfully created");

		conn.close();

	}

	/**
	 *  Retrurn whether the question wich particular code has a recorded hisrory
	 * @param questionCode
	 * @return
	 * @throws SQLException
	 */
	public static boolean hasRecordedHistory( String questionCode) throws SQLException
	{
		// open a connection
		Connection conn = DriverManager.getConnection(JDBS_URL);

		// Get the data 
		DatabaseMetaData meta = conn.getMetaData(); 
		java.sql.ResultSet res =  meta.getTables(null, null, questionCode.toUpperCase(), null); 

		try 
		{
			if(!res.next())
			{ 
				return false;

			} else{
				//table exists. 
				return true;
			}
		}
		catch (SQLException e )
		{
			System.out.print("ERROR");
		}
		conn.close();

		return false;


	}


	/**
	 *  Gets the entry from the database for the question with particular code
	 * @param questionCode
	 * @return
	 * @throws SQLException
	 */
	public static ArrayList<Entry> getEntry( String questionCode) throws SQLException
	{
		ArrayList< Entry> result = null;
		// Create the connection
		Connection conn = DriverManager.getConnection(JDBS_URL);




		// First check whether question with this particular code has a recorded history 
		if ( hasRecordedHistory(questionCode))
		{
			// Initialize the result  
			result = new ArrayList< Entry>();





			// Create the query to select from that particular table  
			String query = "SELECT * FROM " +questionCode; 

			try
			{
				Statement stm = conn.createStatement();
				ResultSet resultSet = stm.executeQuery( query);

				// Get the value an the partcular raw 
				int i = 0;

				while ( ((java.sql.ResultSet) resultSet).next())
				{		
					// Convert choice distribution to the array 
					// TODO ...
					// Create and add 
					ArrayList< Integer> currentDistribution = toDistribution( resultSet.getString(4));
					
					// Create and add a new entry
					result.add( new Entry(  resultSet.getString(1) ,resultSet.getInt(2), 
											 resultSet.getInt(3), currentDistribution));
				}
			}

			catch (SQLException e )
			{
				System.out.print("HasHistoryError");
			}

		}
		// Close the connection
		conn.close();

		return result;
	}
	
	public static void deleteHistory( String questionCode) throws SQLException
	{
		Connection conn = DriverManager.getConnection(JDBS_URL);
		// First of all check whether that string exist in the database


		if ( hasRecordedHistory( questionCode))
		{
			// If it does, delete it 
			// Create the connection
			
			try 
			{
				Statement stm = conn.createStatement();
				stm.executeUpdate( "DROP TABLE " +questionCode.toUpperCase() +" ");
			}
			catch (SQLException e )
			{
				System.out.print("DeleteHistoryError");
			}
			
			
		}
		// Close the connection
		conn.close();
	}

	/**
	 * Converts the Stirg object into the list of arrays in the way they are stored in the database 
	 * @param str
	 * @return
	 */
	private static ArrayList< Integer> toDistribution( String str)
	{
		ArrayList result = new ArrayList< Integer>(); 
		
		// Read the String and convert 
		// Get index of A, And then B 
		// COnver what is in between 

		// Take the first value 
		int from  = str.indexOf('A'); 
		int to = str.indexOf('B');

		if ( from != 1 && to != -1)
		{
			result.add( Integer.parseInt( str.substring(from + 2,to - 1 ))); // Paste to the int and add
		}

		from = to;
		to = str.indexOf('C'); // And if choice C exists
		if ( to != -1)
		{
			// Add c too 
			result.add( Integer.parseInt( str.substring(from + 2, to - 1 )));
		}
		else if ( to == -1 && from != - 1)
		{
			result.add( Integer.parseInt( str.substring(from + 2)));
		}

		from = to;
		to = str.indexOf('D'); // And if choice C exists
		if ( to != -1)
		{
			// Add c too 

			result.add( Integer.parseInt( str.substring(from + 2, to - 1 )));
		}
		else if ( to == -1 && from != - 1)
		{
			result.add( Integer.parseInt( str.substring(from + 2)));
		}

		from = to;
		to = str.indexOf('E'); // And if choice C exists
		if ( to != -1)
		{
			// Add c too 
			result.add( Integer.parseInt( str.substring(from + 2, to - 1 )));
		}
		else if ( to == -1 && from != - 1)
		{
			result.add( Integer.parseInt( str.substring(from + 2)));
		}
		
		
		from = to;
		to = str.indexOf('F'); // And if choice C exists
		if ( to != -1)
		{
			// Add c too 
			result.add( Integer.parseInt( str.substring(from + 2, to - 1 )));
			result.add( Integer.parseInt( str.substring(to + 2)));
		}
		else if ( to == -1 && from != - 1)
		{
			result.add( Integer.parseInt( str.substring(from + 2)));
		}
		
		return result;

	}

}
