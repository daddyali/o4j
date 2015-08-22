package com.organist4j.test.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class TestHSQLDB {
	static Logger logger = Logger.getLogger(TestHSQLDB.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 
		try {
			new TestHSQLDB().doTest1();
		} catch (Exception e) {
			 
			e.printStackTrace();
		}
	}

	private void doTest1() throws Exception {
		 
		
		Connection c = DriverManager.getConnection("jdbc:hsqldb:file:hsqlorg4j", "organist4j", "organist4j");
		Statement s = c.createStatement();
		ResultSet rs = s.executeQuery("select * from music_item ");
		while (rs.next()) {
			logger.debug(rs.getString("name"));
		}
	}

}
