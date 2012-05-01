package be.gallifreyan.javaee.repo;

import java.sql.*;

import javax.persistence.*;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.*;
import org.dbunit.dataset.*;
import org.dbunit.dataset.filter.SequenceTableFilter;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import static org.junit.Assert.*;
import org.junit.rules.TestName;
import org.slf4j.*;

public class AbstractRepositoryTest {
	private static final Logger logger = LoggerFactory
			.getLogger(AbstractRepositoryTest.class);

	@Rule
	public TestName testName = new TestName();
	private static EntityManagerFactory emf;
	protected EntityManager em;

	@BeforeClass
	public static void beforeClass() {
		logger.info("Running the beforeClass method of {}",
				AbstractRepositoryTest.class);
		emf = Persistence.createEntityManagerFactory("javaee-example-ejb-test");
	}

	@AfterClass
	public static void afterClass() {
		logger.info("Running the afterClass method of {}",
				AbstractRepositoryTest.class);
		if (emf != null) {
			emf.close();
		}
	}

	public AbstractRepositoryTest() {
		super();
	}

	/**
	 * Creates a new persistence context and initializes the repository with the
	 * context. Also, start a new transaction that will be associated with the
	 * persistence context.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		logger.info("Performing the setup of test {}", testName.getMethodName());
		IDatabaseConnection connection = null;
		try {
			connection = getConnection();
			assertNotNull(connection);
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
			if (connection != null) {
				assertNotNull(connection);
				connection.close();
			}
		}

		em = emf.createEntityManager();
		em.getTransaction().begin();
	}

	/**
	 * Rollsback the transaction to ensure that changes are not persisted. Also
	 * closes the persistence context.
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		logger.info("Performing the teardown of test {}",
				testName.getMethodName());
		if (em != null) {
			em.getTransaction().rollback();
			em.close();
		}
	}

	private IDatabaseConnection getConnection() throws ClassNotFoundException,
			SQLException, DatabaseUnitException {
		@SuppressWarnings({ "rawtypes", "unused" })
		Class driverClass = Class.forName("org.apache.derby.jdbc.ClientDriver");
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
