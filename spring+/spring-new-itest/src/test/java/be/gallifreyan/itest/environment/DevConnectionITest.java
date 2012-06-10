package be.gallifreyan.itest.environment;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class DevConnectionITest {
	Connection connection = null;

	@Before
	public void startUp() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:8083/springnew", "springadmin", "springpassword");
			assertNotNull(connection);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void thisisatest() {
		try {
			DatabaseMetaData dbMeta = connection.getMetaData();
			ResultSet rs = dbMeta.getTables(null, null, "%", null);
			//boolean foundColumn = false;
			while (rs.next()) {
				System.out.println(rs.getString(3));
//				if (rs.getString(3).toUpperCase().equals(COLUMNNAME)) {
//					foundColumn = true;
//				}
			}
			//assertEquals(true, foundColumn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
