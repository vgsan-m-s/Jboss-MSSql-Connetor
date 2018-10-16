package com.db.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

public class DatabaseProcessor implements Processor {
	
	@Override
	public void process(Exchange exchange) throws Exception {
       try {
		getWorkOrderDetails("222");
		exchange.getIn().setBody("");
       }
       catch(Exception ex) {
		exchange.getIn().setBody(ex.getMessage());
		ex.printStackTrace();
       }
	}
	
	public static void getWorkOrderDetails(String woID) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
				
		// Create a variable for the connection string.
        String connectionUrl = "jdbc:sqlserver://LKH2VS1015:1433;databaseName=TestGrid;user=sa;password=Password$";
        SQLServerDriver driver = new SQLServerDriver();
        
        try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
            String SQL = "SELECT Workorder_ID,FileXML,XMLFileName FROM XMLFile";
            ResultSet rs = stmt.executeQuery(SQL);

            // Iterate through the data in the result set and display it.
            while (rs.next()) {
            	System.out.println("EMPLOYEE: " + rs.getString("Workorder_ID") + ", " + rs.getString("FileXML"));
            }
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            e.printStackTrace();
        }		
	}
}
