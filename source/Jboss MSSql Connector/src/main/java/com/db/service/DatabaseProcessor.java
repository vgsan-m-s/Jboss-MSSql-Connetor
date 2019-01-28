package com.db.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

public class DatabaseProcessor implements Processor {

	// Create a variable for the connection string.
	String connectionUrl = "";
	SQLServerDriver driver = new SQLServerDriver();

	@Override
	public void process(Exchange exchange) throws Exception {
		connectionUrl = exchange.getIn().getHeader("dbConString").toString();
		try {

			try (Connection con = DriverManager.getConnection(this.connectionUrl);) {

				insertIntoXMLFiletable(exchange, con);
				//generateAndInsertIntoXMLFiletable(con);
				//getWorkOrderDetails(con);
				

			}
			// Handle any errors that may have occurred.
			catch (SQLException e) {
				e.printStackTrace();
			}

			exchange.getOut().setBody("");
		} catch (Exception ex) {
			exchange.getOut().setBody(ex.getMessage());
			ex.printStackTrace();
		}
	}

	public void generateAndInsertIntoXMLFiletable(Connection con) throws SQLException {
		
		System.out.println("PreparedStatement execute start...");

		int workOrderId = 12345;
		int serialCount = 100000;
		String generateSerialNumbers = generateSerialNumbers(serialCount);
		
		String body = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<SL version=\"3.0\" identifier=\"workorder_serials_respond\" xmlns:api=\"http://www.h2compute.com/en/sl/2018\">\r\n" + 
				"	<APIDataHeader>		\r\n" + 
				"		<!-- Following will be use for tracking and auditing -->\r\n" + 
				"		<Timestamp>2018-07-12T09:25:53.0871062Z</Timestamp>\r\n" + 
				"		<TransactionID>D807725E-62CC-4B18-9EF3-880B65B8776F</TransactionID>		\r\n" + 
				"	</APIDataHeader>\r\n" + 
				"	<APIDataBody>\r\n" + 
				"		<WorkOrder>\r\n" + 
				"			<WOID>" + workOrderId + "</WOID>\r\n" + 
				"			<Quantity>" + serialCount + "</Quantity>\r\n" + 
				"		</WorkOrder>\r\n" + 
				"		<Serials>\r\n" + 
				"			<!-- OwnerEncodingType will identify GTIN or SSCC or even NTIN and OwnerValue can be the GTIN Value -->\r\n" + 
				"			<Serial OwnerEncodingType=\"GTIN\" OwnerValue=\"26992927369385\">\r\n" + 
								generateSerialNumbers +
				"			</Serial>	\r\n" + 
				"			<!-- Serial can be multiple if Aggregated and all levels are requested -->\r\n" + 
				"		</Serials>\r\n" + 
				"	</APIDataBody>\r\n" + 
				"</SL>";
		
		System.out.println("Prepared!");
		
		PreparedStatement pstmt = con
				.prepareStatement("INSERT INTO XMLFile (Workorder_ID,FileXML,XMLFileName) VALUES (?, ?, ?)");
		
		pstmt.setString(1, workOrderId + "");
		pstmt.setString(2, body);
		pstmt.setString(3, "SL Serial Info Respond");

		pstmt.executeUpdate();

		System.out.println("PreparedStatement executed!");
	}
	
	public String generateSerialNumbers(int count) {
		String rtn = "";
		
		for(int i=0; i <= count; i ++) {
			rtn = rtn + "				<NO>190" + (900000000 + i) + "</NO>\r\n";
		}
		
		return rtn;
	}
	
	public void insertIntoXMLFiletable(Exchange exchange, Connection con) throws SQLException {
		
		System.out.println("PreparedStatement execute start...");
		String body = exchange.getIn().getBody(String.class);
		
		PreparedStatement pstmt = con
				.prepareStatement("INSERT INTO XMLFile (Workorder_ID,FileXML,XMLFileName) VALUES (?, ?, ?)");
		
		pstmt.setString(1, "777");
		pstmt.setString(2, body);
		pstmt.setString(3, "From JBC");

		pstmt.executeUpdate();

		System.out.println("PreparedStatement executed!");
	}

	public void getWorkOrderDetails(Connection con)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {

		Statement stmt = con.createStatement();
		String SQL = "SELECT Workorder_ID,FileXML,XMLFileName FROM XMLFile where Workorder_ID = 777";
		ResultSet rs = stmt.executeQuery(SQL);

		// Iterate through the data in the result set and display it.
		while (rs.next()) {
			System.out.println("EMPLOYEE: " + rs.getString("Workorder_ID") + ", " + rs.getString("FileXML"));
		}
	}
}
