package db;

import java.sql.*;

public class DataLaunch 
{
	public static void main(String[] args) throws ClassNotFoundException, SQLException
	{
		String driver = "org.apache.derby.jdbc.EmbeddedDriver";
		String jdbs_url = "jdbc:derby:BilClickData;create=true";
		
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(jdbs_url);
		
		/**
		 * Code comented out below creates a 'questions' table in th folder 'BilClickData'
		 */


		try
		{
			//(String catagory, String questionText, int answerIndex, String... choices)
			conn.createStatement().execute("create table questions( QUESTION_CODE varchar(40),"
					+ "CATEGORY varchar(20),"
					+ " QUESTION_TEXT varchar(1000),"
					+ " ANSWER_INDEX integer,"
					+ " FIRST_CHOICE varchar(100),"
					+ " SECOND_CHOICE varchar(100),"
					+ " THIRD_CHOICE varchar(100),"
					+ " FOURTH_CHOICE varchar(100),"
					+ " FIFTH_CHOICE varchar(100),"
					+ " SIXTH_CHOICE varchar(100))");


			//conn.createStatement().execute("INSERT INTO questions VALUES ('secretcode', 'FirstFolder', 'Is math hard?', 100, 'yes', 'not', 'depends', 'have no idea', 'yes, if your instructor is Tekman', 'What Math is?')");
			//"INSERT INTO questions VALUES ( 'ew', 'ewew', 2', '222', '222', 'null', 'null', 'null', 'null')"

		}
		catch (Exception e)
		{
			System.out.println("Questions Table Already Exists");
		}

	}
}
