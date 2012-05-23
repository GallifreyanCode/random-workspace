package be.gallifreyan.liquibase;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DatabaseITest {
	private static final String COLUMNNAME = "DEPARTMENT";
	Connection connection = null;

	@Before
	public void startUp() {
		try {
			Class.forName("org.apache.derby.jdbc.ClientDriver");
			connection = DriverManager.getConnection(
					"jdbc:derby://localhost:1527/liquibase", "APP", "APP");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testTableIsCreated() {
		try {
			DatabaseMetaData dbMeta = connection.getMetaData();
			ResultSet rs = dbMeta.getTables(null, null, "%", null);
			boolean foundColumn = false;
			while (rs.next()) {
				if (rs.getString(3).toUpperCase().equals(COLUMNNAME)) {
					foundColumn = true;
				}
			}
			assertEquals(true, foundColumn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testNumberOfColumns() {
		Statement st;
		try {
			st = connection.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM " + COLUMNNAME);
			ResultSetMetaData md = rs.getMetaData();
			int col = md.getColumnCount();
			assertEquals(3, col);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testColumnNames() {
		Statement st;
		try {
			st = connection.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM " + COLUMNNAME);
			ResultSetMetaData md = rs.getMetaData();
			int col = md.getColumnCount();
			assertEquals(3, col);
			assertEquals("ID", md.getColumnName(1).toUpperCase());
			assertEquals("NAME", md.getColumnName(2).toUpperCase());
			assertEquals("ACTIVE", md.getColumnName(3).toUpperCase());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testColumnTypes() {
		Statement st;
		try {
			st = connection.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM " + COLUMNNAME);
			ResultSetMetaData md = rs.getMetaData();
			int col = md.getColumnCount();
			assertEquals(3, col);
			assertEquals("INTEGER", md.getColumnTypeName(1).toUpperCase());
			assertEquals("VARCHAR", md.getColumnTypeName(2).toUpperCase());
			assertEquals("BOOLEAN", md.getColumnTypeName(3).toUpperCase());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testColumnSizes() {
		Statement st;
		try {
			st = connection.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM " + COLUMNNAME);
			ResultSetMetaData md = rs.getMetaData();
			int col = md.getColumnCount();
			assertEquals(3, col);
			assertEquals(11, md.getColumnDisplaySize(1));
			assertEquals(40, md.getColumnDisplaySize(2));
			assertEquals(5, md.getColumnDisplaySize(3));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testColumnNullable() {
		Statement st;
		try {
			st = connection.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM " + COLUMNNAME);
			ResultSetMetaData md = rs.getMetaData();
			int col = md.getColumnCount();
			assertEquals(3, col);
			assertEquals(0, md.isNullable(1));
			assertEquals(0, md.isNullable(2));
			assertEquals(1, md.isNullable(3));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testColumnAutoIncrement() {
		Statement st;
		try {
			st = connection.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM " + COLUMNNAME);
			ResultSetMetaData md = rs.getMetaData();
			int col = md.getColumnCount();
			assertEquals(3, col);
			assertEquals(false, md.isAutoIncrement(1));
			assertEquals(false, md.isAutoIncrement(2));
			assertEquals(false, md.isAutoIncrement(3));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@After
	public void tearDown() {
		try {
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
