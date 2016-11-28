package db;

/**
 * Class create to hold the data used in the programm in a database
 * @author Jelibon: Aydemir
 */

import java.sql.*;
import java.util.ArrayList;
import question.*;
import question.history.Entry;

public class QuestionManager 
{
	private static QuestionCollection collection; 
	private static Connection connection; 
	private static Statement statement;

	// SQL statements as final strings 
	private final static String SELECT_FROM = "select * from questions";
	private final static String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	private final static String JDBS_URL = "jdbc:derby:BilClickData;create=true";
	private final static int MAX_NUM_OF_CHOICES = 6;


	/**
	 * Add values of the question to the table
	 * @param q to be inserted
	 * @throws SQLException
	 */
	public static void addQuestion( Question q) throws SQLException
	{

		// Take the choices as an array 
		ArrayList<String> choices = q.getChoices();

		// Create the first part of the query assigning the cathegorym 
		String query =  "INSERT INTO questions " +
				"VALUES ( '" +q.getQuestionCode()+ "', '"
				+q.getCategory()     +"', '"
				+q.getQuestionText() +"', "
				+q.getAnswerIndex();

		// Add the rest according to the size of the choices 
		// For unfilled parts of the choices raw assign a String "NUll"

		for ( int i = 0; i < choices.size(); i++)
		{
			// we do not need " ' " after the 2 
			if ( i == 0 )
				query = query + ", '"+ choices.get(i);
			else 
				query = query + "', '"+ choices.get(i);


			if( i == 5)
				query = query + "')";
		}

		// if less than the max number 6 
		int rest = MAX_NUM_OF_CHOICES - choices.size();

		// For the rest assing the value null 
		for( int i = 0; i < rest; i++)
		{
			query = query + "', '"+ "null";

			if ( i + choices.size() == 5)
				query = query + "')";
		}

		connection  = DriverManager.getConnection(JDBS_URL); 
		try
		{
			//Creat the connection and the statement 
			statement =  connection.createStatement();
			statement.execute(query);
			
			
			for(Entry cycler : q.getHistory())
			{
				HistoryManager.questionAsked(q.getQuestionCode(), cycler);
			}
			

		}catch (SQLException  | ClassNotFoundException e )
		{
			System.out.print("ERROR : AddQuestion");
			e.printStackTrace();
		}
		finally {
			connection.close();
		}
	}


	/**
	 * When passed a code of the question, retruns the question if it is exist in the dabase
	 * @param questionCode Question code of the desired question
	 * @return Question
	 * @throws SQLException
	 *
	 */
	public static Question getQuestion( String questionCode ) throws SQLException
	{
		//Creat the connection and the statement 
		connection  = DriverManager.getConnection(JDBS_URL); 

		// Create the result object 
		Question result = null; 

		try
		{
			// Create the statement
			Statement stm = connection.createStatement();
			ResultSet resultSet = stm.executeQuery(SELECT_FROM);

			// Get the question with particular code
			while ( resultSet.next())
			{
				// If the question code is equal to the given code return the Question object
				if( resultSet.getString(1).equals( questionCode))
				{
					// When reached needed index return read the data and return the object

					result = new Question ( resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), 
							resultSet.getInt(4), resultSet.getString(5), 
							resultSet.getString(6));
					

					// if the next 4 choices are not null's in the database add them to the choices
					for (int j = 0; j < 4; j++)
					{
						if ( !resultSet.getString( j + 7).equals("null"))
							result.addChoice(resultSet.getString(j + 7)); // get i + 6'th string in the raw
					}
				}
			}
			if(HistoryManager.hasRecordedHistory(result.getQuestionCode())) // TODO yanlis olabilir
				for(Entry cycler : HistoryManager.getEntry(result.getQuestionCode()))
					result.addEntry(cycler);

		}

		catch (SQLException e )
		{
			System.out.print("ERROR:GetQuestion(String)");
			e.printStackTrace(); // Trace the error
		}
		finally
		{
			connection.close();
		}


		return result;
	}


	/**
	 * updates the current collection 
	 * @throws SQLException
	 */
	public static void updateCollection() throws SQLException
	{
		// Creat the connection and the statement 
		connection  = DriverManager.getConnection(JDBS_URL); 

		// Initialize the result object 
		collection = new QuestionCollection();

		try
		{
			// Create the statement
			Statement stm = connection.createStatement();
			ResultSet resultSet = stm.executeQuery(SELECT_FROM);


			while ( resultSet.next())
			{		
				// Create a current question( question at this raw) and add it 
				Question current = new Question ( resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), 
						resultSet.getInt(4), resultSet.getString(5), 
						resultSet.getString(6));

				// if the next 4 choices are not null's in the database add them to the choices
				for (int j = 0; j < 4; j++)
				{
					if ( !resultSet.getString( j + 7).equals("null"))
						current.addChoice(resultSet.getString(j + 7)); // get j + 6'th string in the raw
				}
				
				// Also add the history 
				if ( HistoryManager.hasRecordedHistory( resultSet.getString(1)))
				{
					for(Entry cycler : HistoryManager.getEntry(resultSet.getString(1)))
					{
						current.addEntry(cycler);
					}
				}

				collection.addQuestion( current); // Add the current ( question at this raw) to the list 
			}
		}

		catch (SQLException e )
		{
			System.out.print("ERROR : Update Question");
			e.printStackTrace();
		}
		finally
		{
			connection.close();
		}
	}

	/**
	 * Returns collection of all question from the last update
	 * @return collection
	 */
	public static QuestionCollection getCollection() throws SQLException
	{
		// If the colllection is initially zero, fist update it and then return 
		if ( collection == null)
		{
			updateCollection();
			return collection; 
		}
		else 
			return collection;
	}

	/**
	 * Updates and returns the collection 
	 * @return collection
	 * @throws SQLException
	 */
	public static QuestionCollection getUpdatedCollection() throws SQLException
	{
		updateCollection(); 
		return getCollection();
	}

	/**
	 * Deletes the question at a given raw
	 * @param raw
	 * @throws SQLException
	 */
	public static void deleteQuestion( int raw) throws SQLException
	{
		connection  = DriverManager.getConnection(JDBS_URL); 
		try
		{
			// Create two connection one for iterating the second one to delete the raw statement
			Statement stm = connection.createStatement(); 
			Statement stm2 = connection.createStatement(); 

			ResultSet resultSet = stm.executeQuery(SELECT_FROM);


			// Start counting 
			int i = 0;

			while ( resultSet.next())
			{		
				// When reached needed index return delete the value 
				if ( i == raw)
				{
					String questionCode = resultSet.getString(1); // Get the QUESTION_CODE value of the raw 

					// Delete the raw corresponting to that value of QUESTION_CODE
					String query = "delete from questions where QUESTION_CODE = '" +questionCode+ "'";
					stm2.execute(query);
					break;
				}
				i++; 
			}
		}

		catch (SQLException e )
		{
			System.out.println("ERROR");
		}
		finally
		{
			connection.close();
		}
	}

	/**
	 * Deletes the question if such question exists in the directory 
	 * @param question
	 * @throws SQLException
	 */
	public static void deleteQuestion( Question question) throws SQLException
	{
		connection  = DriverManager.getConnection(JDBS_URL);
		try 
		{
			Statement statement = connection.createStatement();
			statement.execute("delete from questions where QUESTION_CODE = '" +question.getQuestionCode()+ "'");
		}
		catch (SQLException e)
		{
			System.out.println("Error");
		}
		finally {
			connection.close();
		}
	}

	public static void deleteQuestion( String questionCode) throws SQLException
	{
		connection  = DriverManager.getConnection(JDBS_URL);

		try 
		{
			Statement statement = connection.createStatement();
			statement.execute("delete from questions where QUESTION_CODE = '" +questionCode+ "'");
		}			
		catch (SQLException e)
		{
			System.out.println("Error");
		}
		finally {
			connection.close();
		}
	}
	


	/**
	 * Changes the question parameters
	 * NOTE: question code neves change once created. Only way is to delete and recreate the object
	 * @param question
	 * @param raw
	 * @throws SQLException
	 */
	public static void setQuestion( Question question, int raw) throws SQLException
	{
		connection  = DriverManager.getConnection(JDBS_URL); 
		try
		{
			QuestionManager.deleteQuestion( raw);
			QuestionManager.addQuestion( question);
		}
		catch (SQLException e)
		{
			System.out.println("Error");
		}
		finally {
			connection.close();
		}


	}



	public static void setQuestion( Question question, String questionCode) throws SQLException
	{
		connection  = DriverManager.getConnection(JDBS_URL);

		try
		{
			QuestionManager.deleteQuestion( questionCode);
			HistoryManager.deleteHistory(questionCode);
			QuestionManager.addQuestion( question);
		}
		catch (SQLException e)
		{
			System.out.println("Error");
		}
		finally {
			connection.close();
		}

	}
	
	public static void saveQuestion(Question q) throws SQLException{
		setQuestion(q, q.getQuestionCode());
	}


}
