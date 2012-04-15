package be.gallifreyan.javaee.service.ejb;

import java.sql.*;
import java.util.*;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.*;
import org.dbunit.dataset.*;
import org.dbunit.dataset.filter.SequenceTableFilter;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.*;
import org.junit.rules.TestName;
import org.slf4j.*;

public class AbstractIntegrationTest {
	private static final Logger logger = LoggerFactory
			.getLogger(AbstractIntegrationTest.class);

	protected static boolean isRunFromSuite = false;
	protected static EJBContainer container;
	protected static Context context;
	protected static DataSource datasource;

	@Rule
	public TestName testMethod = new TestName();

	/**
	 * The setup routine to be used by all <code>@BeforeClass</code> methods.
	 * This is static, since it must be invoked from static methods in actual
	 * IntegrationTest classes.
	 * 
	 * @throws Exception
	 */
	public static void setUpBeforeClass() throws Exception {
		if (isRunFromSuite && container != null) {
			// Do nothing. The test suite has initialized everything.
		} else {
			startup();
		}
	}

	/**
	 * The tear down routine to be used by <code>@AfterClass</code> methods.
	 * This is static as it must be invoked from static methods in
	 * IntegrationTest classes.
	 * 
	 * @throws Exception
	 */
	public static void tearDownAfterClass() throws Exception {
		if (isRunFromSuite) {
			// Do nothing. The test suite will shutdown everything.
		} else {
			shutdown();
		}
	}

	/**
	 * Starts the GF embedded container. Also retrieves the JNDI data source for
	 * use in the integration tests.
	 * 
	 * This method should be invoked directly only from test suites. Test cases
	 * may invoke this method only after verifying that the
	 * <code>isRunFromSuite</code> property is false.
	 * 
	 * @throws Exception
	 */
	public static void startup() throws Exception {
		logger.info("Starting the embedded container.");
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("org.glassfish.ejb.embedded.glassfish.installation.root",
				"./glassfish-integration/glassfish");
		container = EJBContainer.createEJBContainer(props);
		context = container.getContext();
		datasource = (DataSource) context.lookup("jdbc/javaeeDS");
	}

	/**
	 * Shuts down the embedded container.
	 */
	public static void shutdown() {
		logger.info("Shutting down the embedded container.");
		container.close();
	}

	/**
	 * Resets the contents of the database tables before every test using
	 * DbUnit.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		IDatabaseConnection connection = null;
		try {
			connection = getConnection();
			IDataSet dataSet = getDataSet();
			// The FilteredDataSet and the SequenceTableFilter is used to
			// reorder the DELETE operations to prevent failures due to circular
			// dependencies
			// between the ALBUMS and PHOTOS tables. The plain XML file does not
			// contain this information. The DatabaseSequenceFilter class is
			// avoided as it cannot handle circular dependencies.
			String[] orderedTableNames = new String[] { "SEQUENCE",
					"USERS_GROUPS", "USERS", "GROUPS", "ALBUMS", "PHOTOS" };
			IDataSet filteredDataSet = new FilteredDataSet(
					new SequenceTableFilter(orderedTableNames), dataSet);
			DatabaseOperation.CLEAN_INSERT.execute(connection, filteredDataSet);
		} finally {
			connection.close();
		}
	}

	@After
	public void tearDown() throws Exception {

	}

	private IDatabaseConnection getConnection() throws ClassNotFoundException,
			SQLException, DatabaseUnitException {
		Class.forName("org.apache.derby.jdbc.ClientDriver");
		Connection jdbcConnection = DriverManager.getConnection(
				"jdbc:derby://localhost:1527/JAVAEEEXAMPLE", "APP", "APP");
		IDatabaseConnection databaseConnection = new DatabaseConnection(
				jdbcConnection);
		return databaseConnection;
	}

	/**
	 * Returns a dataset containing blanked out tables and table-based sequences
	 * set to 0.
	 * 
	 * @return
	 * @throws Exception
	 */
	private IDataSet getDataSet() throws Exception {
		ClassLoader classLoader = this.getClass().getClassLoader();
		return new FlatXmlDataSetBuilder().build(classLoader
				.getResourceAsStream("database-test-setup.xml"));
	}
}
