package be.gallifreyan.javaee.view;

import static org.junit.Assert.*;

import be.gallifreyan.javaee.view.user.*;
import be.gallifreyan.javaee.view.album.*;
import be.gallifreyan.javaee.view.photo.*;

import java.io.*;
import java.net.*;
import java.sql.*;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.*;
import org.dbunit.dataset.*;
import org.dbunit.dataset.filter.SequenceTableFilter;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
//import org.jacoco.core.runtime.*;
import org.jboss.arquillian.container.test.api.*;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.*;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.*;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.slf4j.*;

@RunWith(Arquillian.class)
@RunAsClient
public class AllPagesIntegrationTest
{

	private static final Logger logger = LoggerFactory.getLogger(AllPagesIntegrationTest.class);
	
	@Rule
	public TestName testMethod = new TestName();

	@Drone
	protected WebDriver driver;

	@ArquillianResource
	protected URI contextPath;

	@Deployment(testable=false)
	public static EnterpriseArchive createDeployment()
	{
		logger.info("Preparing deployment.");
		JavaArchive ejbArchive = ShrinkWrap.create(JavaArchive.class, "javaee-example-ejb.jar")
				.addPackage("be.gallifreyan.javaee.entity")
				.addPackage("be.gallifreyan.javaee.repo")
				.addPackage("be.gallifreyan.javaee.service.ejb")
				.addPackage("be.gallifreyan.javaee.service.jpa")
				.addPackage("be.gallifreyan.javaee.service")
				.addPackage("be.gallifreyan.javaee.util")
				.addAsResource("ValidationMessages.properties")
				.addAsResource("ValidationMessages_de.properties")
				.addAsManifestResource("META-INF/ejb-jar.xml", "ejb-jar.xml")
				.addAsManifestResource("META-INF/glassfish-ejb-jar.xml", "glassfish-ejb-jar.xml")
				.addAsManifestResource("META-INF/persistence.xml", "persistence.xml");
		System.out.println("******Contents of EJB Archive******");
		System.out.println(ejbArchive.toString(true));

		WebArchive webArchive = ShrinkWrap
				.create(WebArchive.class, "javaee-example-war.war") // Ensure that this is not "galleria-jsf.war", for now
				.setWebXML(new File("src/webapp/WEB-INF/web.xml"))
				.addPackage("be.gallifreyan.javaee.converter")
				.addPackage("be.gallifreyan.javaee.filter")
				.addPackage("be.gallifreyan.javaee.i18n")
				.addPackage("be.gallifreyan.javaee.listener")
				.addPackages(true, "be.gallifreyan.javaee.view")
				.addAsResource("resources/messages_en.properties", "resources/messages_en.properties")
				.addAsResource("resources/messages_de.properties", "resources/messages_de.properties")
				.addAsResource("resources/StandardIcon.png", "resources/StandardIcon.png")
				.addAsWebResource(new File("src/webapp/templates", "content.xhtml"), "templates/content.xhtml")
				.addAsWebResource(new File("src/webapp/templates", "defaultLayout.xhtml"),	"templates/defaultLayout.xhtml")
				.addAsWebResource(new File("src/webapp/templates", "footer.xhtml"), "templates/footer.xhtml")
				.addAsWebResource(new File("src/webapp/templates", "header.xhtml"), "templates/header.xhtml")
				.addAsWebResource(new File("src/webapp/templates", "privateLayout.xhtml"), "templates/privateLayout.xhtml")
				.addAsWebResource(new File("src/webapp/resources/styles", "all.css"), "resources/styles/all.css")
				.addAsWebResource(new File("src/webapp", "Index.xhtml"))
				.addAsWebResource(new File("src/webapp", "Signup.xhtml"))
				.addAsWebResource(new File("src/webapp", "Login.xhtml"))
				.addAsWebResource(new File("src/webapp/private", "HomePage.xhtml"), "private/HomePage.xhtml")
				.addAsWebResource(new File("src/webapp/private/user", "AccountPreferences.xhtml"), "private/user/AccountPreferences.xhtml")
				.addAsWebResource(new File("src/webapp/private/album", "CreateAlbum.xhtml"), "private/album/CreateAlbum.xhtml")
				.addAsWebResource(new File("src/webapp/private/album", "EditAlbum.xhtml"), "private/album/EditAlbum.xhtml")
				.addAsWebResource(new File("src/webapp/private/album", "ViewAlbum.xhtml"), "private/album/ViewAlbum.xhtml")
				.addAsWebResource(new File("src/webapp/private/photo", "UploadPhoto.xhtml"), "private/photo/UploadPhoto.xhtml")
				.addAsWebResource(new File("src/webapp/private/photo", "EditPhoto.xhtml"), "private/photo/EditPhoto.xhtml")
				.addAsWebResource(new File("src/webapp/private/photo", "ViewPhoto.xhtml"), "private/photo/ViewPhoto.xhtml")
				.addAsWebInfResource(new File("src/webapp/WEB-INF/faces-config.xml"), "faces-config.xml")
				/* .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml") */
				.addAsLibraries(
						DependencyResolvers
								.use(MavenDependencyResolver.class)
								.loadMetadataFromPom("pom.xml")
								.artifacts("org.primefaces:primefaces:3.0.M2",
										"commons-io:commons-io:1.4",
										"commons-fileupload:commons-fileupload:1.2.1",
										"commons-codec:commons-codec:1.5").resolveAsFiles());
		System.out.println(webArchive.toString(true));

		EnterpriseArchive enterpriseArchive = ShrinkWrap
				.create(EnterpriseArchive.class, "javaee-example-ear.ear")
				.addAsModule(webArchive)
				.addAsModule(ejbArchive);
//				.addAsLibraries(
//						DependencyResolvers
//								.use(MavenDependencyResolver.class)
//								.artifact("commons-codec:commons-codec:1.5")
//								.resolveAsFiles());
		System.out.println("******Contents of EAR Archive******");
		System.out.println(enterpriseArchive.toString(true));

		return enterpriseArchive;
	}

	@BeforeClass
	public static void beforeClass()
	{
		logger.info("Before Class");
	}

	@AfterClass
	public static void afterClass()
	{
		logger.info("After Class");
	}

	/**
	 * Resets the contents of the database tables before every test using
	 * DbUnit.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		logger.info("Performing setup before Test {}", testMethod.getMethodName());
		IDatabaseConnection connection = null;
		try
		{
			connection = getConnection();
			IDataSet dataSet = getDataSet();
			// The FilteredDataSet and the SequenceTableFilter is used to
			// reorder the DELETE operations to prevent failures due to circular
			// dependencies
			// between the ALBUMS and PHOTOS tables. The plain XML file does not
			// contain this information. The DatabaseSequenceFilter class is
			// avoided as it cannot handle circular dependencies.
			String[] orderedTableNames = new String[] { "SEQUENCE", "USERS_GROUPS", "USERS", "GROUPS", "ALBUMS",
					"PHOTOS" };
			IDataSet filteredDataSet = new FilteredDataSet(new SequenceTableFilter(orderedTableNames), dataSet);
			DatabaseOperation.CLEAN_INSERT.execute(connection, filteredDataSet);
		}
		catch (Exception ex)
		{
			throw ex;
		}
		finally
		{
			try
			{
				connection.close();
			}
			catch (SQLException sqlEx)
			{
				logger.warn(sqlEx.getMessage(), sqlEx);
			}
		}
	}

	/**
	 * Logout the user after every test. This will prevent subsequent tests from
	 * failing, as the UserRedirectFilter will redirect the Firefox driver to
	 * the home page instead of the index, login or signup pages.
	 */
	@After
	public void tearDown()
	{
		WebElement logoutLink = null;
		try
		{
			logoutLink = driver.findElement(By.id("commonActions:logout"));
		}
		catch (NoSuchElementException noElemEx)
		{
			// do nothing if the logout link is not present.
		}
		if (logoutLink != null)
		{
			logoutLink.click();
			Wait<WebDriver> wait = new WebDriverWait(driver, 15);
			wait.until(PageUtilities.visibilityOfElementLocated(By.id("publicWrapper")));
		}
	}

	private IDatabaseConnection getConnection() throws ClassNotFoundException, SQLException, DatabaseUnitException
	{
		Class.forName("org.apache.derby.jdbc.ClientDriver");
		Connection jdbcConnection = DriverManager.getConnection("jdbc:derby://localhost:1527/JAVAEEEXAMPLE", "APP",
				"APP");
		IDatabaseConnection databaseConnection = new DatabaseConnection(jdbcConnection);
		return databaseConnection;
	}

	/**
	 * Returns a dataset containing blanked out tables and table-based sequences
	 * set to 0.
	 * 
	 * @return
	 * @throws Exception
	 */
	private IDataSet getDataSet() throws Exception
	{
		ClassLoader classLoader = this.getClass().getClassLoader();
		return new FlatXmlDataSetBuilder().build(classLoader.getResourceAsStream("database-test-setup.xml"));
	}

	@Test
	public void testSignupUser() throws Exception
	{
		driver.get(contextPath.toString());
		IndexPage indexPage = new IndexPage(driver, contextPath);
		SignupPage signupPage = indexPage.chooseToSignup();
		indexPage = signupPage.signupAs("User#1", "password");
		String[] message = { "The new user account has been created. You may now login." };
		assertArrayEquals(message, indexPage.fetchSuccessMessages());
	}

	@Test
	public void testSignupUserWithNoValues() throws Exception
	{
		driver.get(contextPath.toString());
		IndexPage indexPage = new IndexPage(driver, contextPath);
		SignupPage signupPage = indexPage.chooseToSignup();
		signupPage = signupPage.signupAsExpectingError("", "", "");
		String[] errorMessages = { "The user Id should be between 1 and 50 characters in length.",
				"The password should be between 1 and 500 characters in length.",
				"The confirmed password should be between 1 and 500 characters in length." };
		assertArrayEquals(errorMessages, signupPage.fetchSignupErrorMessages());
	}

	@Test
	public void testSignupUserWithExcessiveValues() throws Exception
	{
		driver.get(contextPath.toString());
		IndexPage indexPage = new IndexPage(driver, contextPath);
		SignupPage signupPage = indexPage.chooseToSignup();
		signupPage = signupPage
				.signupAsExpectingError(
						"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
						"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
						"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		String[] errorMessages = { "The user Id should be between 1 and 50 characters in length.",
				"The password should be between 1 and 500 characters in length.",
				"The confirmed password should be between 1 and 500 characters in length." };
		assertArrayEquals(errorMessages, signupPage.fetchSignupErrorMessages());
	}

	@Test
	public void testSignupUserWithMismatchedPasswords() throws Exception
	{
		driver.get(contextPath.toString());
		IndexPage indexPage = new IndexPage(driver, contextPath);
		SignupPage signupPage = indexPage.chooseToSignup();
		signupPage = signupPage.signupAsExpectingError("User#1", "password", "mismatchpassword");
		String[] errorMessages = { "The entered passwords do not match. Please correct the passwords before retrying." };
		assertArrayEquals(errorMessages, signupPage.fetchSignupErrorMessages());
	}

	@Test
	public void testSignupDuplicateUsers() throws Exception
	{
		driver.get(contextPath.toString());
		IndexPage indexPage = new IndexPage(driver, contextPath);
		SignupPage signupPage = indexPage.chooseToSignup();
		indexPage = signupPage.signupAs("User#1", "password");
		String[] message = { "The new user account has been created. You may now login." };
		assertArrayEquals(message, indexPage.fetchSuccessMessages());

		signupPage = indexPage.chooseToSignup();
		signupPage = signupPage.signupAsExpectingError("User#1", "password", "password");
		String[] errorMessages = { "The user account Id is already taken. Please choose another." };
		assertArrayEquals(errorMessages, signupPage.fetchSignupErrorMessages());
	}

	@Test
	public void testLoginUserInvalidCredentials() throws Exception
	{
		driver.get(contextPath.toString());
		IndexPage indexPage = new IndexPage(driver, contextPath);
		SignupPage signupPage = indexPage.chooseToSignup();
		indexPage = signupPage.signupAs("User#1", "password");
		String[] message = { "The new user account has been created. You may now login." };
		assertArrayEquals(message, indexPage.fetchSuccessMessages());

		LoginPage loginPage = indexPage.chooseToLogin();
		loginPage.loginAsExpectingError("User#2", "password");
		String[] errorMessages = { "The user name or password is incorrect." };
		assertArrayEquals(errorMessages, loginPage.getLoginErrorMessagesDisplayed());
	}

	@Test
	public void testLoginUser() throws Exception
	{
		driver.get(contextPath.toString());
		IndexPage indexPage = new IndexPage(driver, contextPath);
		SignupPage signupPage = indexPage.chooseToSignup();
		indexPage = signupPage.signupAs("User#1", "password");
		String[] message = { "The new user account has been created. You may now login." };
		assertArrayEquals(message, indexPage.fetchSuccessMessages());

		LoginPage loginPage = indexPage.chooseToLogin();
		loginPage.loginAs("User#1", "password");
		assertTrue(driver.findElement(By.id("commonActions:AccountPreferences")) != null);
		assertTrue(driver.findElement(By.id("commonActions:logout")) != null);
	}

	@Test
	public void testLogoutUser() throws Exception
	{
		driver.get(contextPath.toString());
		IndexPage indexPage = new IndexPage(driver, contextPath);
		SignupPage signupPage = indexPage.chooseToSignup();
		indexPage = signupPage.signupAs("User#1", "password");
		String[] message = { "The new user account has been created. You may now login." };
		assertArrayEquals(message, indexPage.fetchSuccessMessages());

		LoginPage loginPage = indexPage.chooseToLogin();
		HomePage homePage = loginPage.loginAs("User#1", "password");
		homePage.logout();
	}

	@Test
	public void testModifyPassword() throws Exception
	{
		driver.get(contextPath.toString());
		IndexPage indexPage = new IndexPage(driver, contextPath);
		SignupPage signupPage = indexPage.chooseToSignup();
		indexPage = signupPage.signupAs("User#1", "password");
		String[] message = { "The new user account has been created. You may now login." };
		assertArrayEquals(message, indexPage.fetchSuccessMessages());

		LoginPage loginPage = indexPage.chooseToLogin();
		HomePage homePage = loginPage.loginAs("User#1", "password");
		AccountPreferencesPage accountPreferencesPage = homePage.goToAccountPreferences();
		homePage = accountPreferencesPage.modifyPasswordAs("password", "password1", "password1");
		String[] successMessage = { "The password has been modified." };
		assertArrayEquals(successMessage, homePage.fetchSuccessMessages());
	}

	@Test
	public void testModifyPasswordWithIncorrectOld() throws Exception
	{
		driver.get(contextPath.toString());
		IndexPage indexPage = new IndexPage(driver, contextPath);
		SignupPage signupPage = indexPage.chooseToSignup();
		indexPage = signupPage.signupAs("User#1", "password");
		String[] message = { "The new user account has been created. You may now login." };
		assertArrayEquals(message, indexPage.fetchSuccessMessages());

		LoginPage loginPage = indexPage.chooseToLogin();
		HomePage homePage = loginPage.loginAs("User#1", "password");
		AccountPreferencesPage accountPreferencesPage = homePage.goToAccountPreferences();
		accountPreferencesPage.modifyPasswordAsExpectingError("password1", "password2", "password2");
		String[] errorMessages = { "The old password entered does not match your current password." };
		assertArrayEquals(errorMessages, accountPreferencesPage.fetchErrorMessages());
	}

	@Test
	public void testModifyPasswordWithMismatchingNew() throws Exception
	{
		driver.get(contextPath.toString());
		IndexPage indexPage = new IndexPage(driver, contextPath);
		SignupPage signupPage = indexPage.chooseToSignup();
		indexPage = signupPage.signupAs("User#1", "password");
		String[] message = { "The new user account has been created. You may now login." };
		assertArrayEquals(message, indexPage.fetchSuccessMessages());

		LoginPage loginPage = indexPage.chooseToLogin();
		HomePage homePage = loginPage.loginAs("User#1", "password");
		AccountPreferencesPage accountPreferencesPage = homePage.goToAccountPreferences();
		accountPreferencesPage.modifyPasswordAsExpectingError("password", "password1", "password2");
		String[] errorMessages = { "The new password and the confirm password values do not match." };
		assertArrayEquals(errorMessages, accountPreferencesPage.fetchErrorMessages());
	}

	@Test
	public void testModifyPasswordWithSameOldAndNew() throws Exception
	{
		driver.get(contextPath.toString());
		IndexPage indexPage = new IndexPage(driver, contextPath);
		SignupPage signupPage = indexPage.chooseToSignup();
		indexPage = signupPage.signupAs("User#1", "password");
		String[] message = { "The new user account has been created. You may now login." };
		assertArrayEquals(message, indexPage.fetchSuccessMessages());

		LoginPage loginPage = indexPage.chooseToLogin();
		HomePage homePage = loginPage.loginAs("User#1", "password");
		AccountPreferencesPage accountPreferencesPage = homePage.goToAccountPreferences();
		accountPreferencesPage.modifyPasswordAsExpectingError("password", "password", "password");
		String[] errorMessages = { "Your new password cannot be the same as the old password." };
		assertArrayEquals(errorMessages, accountPreferencesPage.fetchErrorMessages());
	}

	@Test
	public void testDeleteAccount() throws Exception
	{
		driver.get(contextPath.toString());
		IndexPage indexPage = new IndexPage(driver, contextPath);
		SignupPage signupPage = indexPage.chooseToSignup();
		indexPage = signupPage.signupAs("User#1", "password");
		String[] message = { "The new user account has been created. You may now login." };
		assertArrayEquals(message, indexPage.fetchSuccessMessages());

		LoginPage loginPage = indexPage.chooseToLogin();
		HomePage homePage = loginPage.loginAs("User#1", "password");
		AccountPreferencesPage accountPreferencesPage = homePage.goToAccountPreferences();
		indexPage = accountPreferencesPage.deleteAcccount();
		String[] messages = { "The account was deleted successfully." };
		assertArrayEquals(messages, indexPage.fetchSuccessMessages());
	}

	@Test
	public void testCreateAlbum() throws Exception
	{
		driver.get(contextPath.toString());
		IndexPage indexPage = new IndexPage(driver, contextPath);
		SignupPage signupPage = indexPage.chooseToSignup();
		indexPage = signupPage.signupAs("User#1", "password");
		String[] message = { "The new user account has been created. You may now login." };
		assertArrayEquals(message, indexPage.fetchSuccessMessages());

		LoginPage loginPage = indexPage.chooseToLogin();
		HomePage homePage = loginPage.loginAs("User#1", "password");
		CreateAlbumPage createAlbumPage = homePage.goToCreateAlbum();
		homePage = createAlbumPage.createAlbumAs("Album#1", "My first album");
		String[] messages = { "The album was created successfully." };
		assertArrayEquals(messages, homePage.fetchSuccessMessages());
	}

	@Test
	public void testCreateAlbumWithNoValues() throws Exception
	{
		driver.get(contextPath.toString());
		IndexPage indexPage = new IndexPage(driver, contextPath);
		SignupPage signupPage = indexPage.chooseToSignup();
		indexPage = signupPage.signupAs("User#1", "password");
		String[] message = { "The new user account has been created. You may now login." };
		assertArrayEquals(message, indexPage.fetchSuccessMessages());

		LoginPage loginPage = indexPage.chooseToLogin();
		HomePage homePage = loginPage.loginAs("User#1", "password");
		CreateAlbumPage createAlbumPage = homePage.goToCreateAlbum();
		createAlbumPage = createAlbumPage.createAlbumAsExpectingError("", "");
		String[] errorMessages = { "The name of the album should be between 1 and 50 characters in length." };
		assertArrayEquals(errorMessages, createAlbumPage.fetchErrorMessages());
	}

	@Test
	public void testCreateAlbumWithExcessiveValues() throws Exception
	{
		driver.get(contextPath.toString());
		IndexPage indexPage = new IndexPage(driver, contextPath);
		SignupPage signupPage = indexPage.chooseToSignup();
		indexPage = signupPage.signupAs("User#1", "password");
		String[] message = { "The new user account has been created. You may now login." };
		assertArrayEquals(message, indexPage.fetchSuccessMessages());

		LoginPage loginPage = indexPage.chooseToLogin();
		HomePage homePage = loginPage.loginAs("User#1", "password");
		CreateAlbumPage createAlbumPage = homePage.goToCreateAlbum();
		createAlbumPage = createAlbumPage
				.createAlbumAsExpectingError(
						"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
						"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		String[] errorMessages = { "The name of the album should be between 1 and 50 characters in length.",
				"The description of the album should be between 0 and 255 characters in length." };
		assertArrayEquals(errorMessages, createAlbumPage.fetchErrorMessages());
	}

	@Test
	public void testCreateDuplicateAlbum() throws Exception
	{
		driver.get(contextPath.toString());
		IndexPage indexPage = new IndexPage(driver, contextPath);
		SignupPage signupPage = indexPage.chooseToSignup();
		indexPage = signupPage.signupAs("User#1", "password");
		String[] message = { "The new user account has been created. You may now login." };
		assertArrayEquals(message, indexPage.fetchSuccessMessages());

		LoginPage loginPage = indexPage.chooseToLogin();
		HomePage homePage = loginPage.loginAs("User#1", "password");
		CreateAlbumPage createAlbumPage = homePage.goToCreateAlbum();
		homePage = createAlbumPage.createAlbumAs("Album#1", "My first album");
		String[] messages = { "The album was created successfully." };
		assertArrayEquals(messages, homePage.fetchSuccessMessages());

		createAlbumPage = homePage.goToCreateAlbum();
		createAlbumPage = createAlbumPage.createAlbumAsExpectingError("Album#1", "My first album");
		String[] errorMessages = { "An existing album with the same name and description was found." };
		assertArrayEquals(errorMessages, createAlbumPage.fetchErrorMessages());
	}

	@Test
	public void testEditAlbum() throws Exception
	{
		String albumName = "Album#1";
		String albumDescription = "My first album";
		driver.get(contextPath.toString());
		IndexPage indexPage = new IndexPage(driver, contextPath);
		SignupPage signupPage = indexPage.chooseToSignup();
		indexPage = signupPage.signupAs("User#1", "password");
		String[] message = { "The new user account has been created. You may now login." };
		assertArrayEquals(message, indexPage.fetchSuccessMessages());

		LoginPage loginPage = indexPage.chooseToLogin();
		HomePage homePage = loginPage.loginAs("User#1", "password");
		CreateAlbumPage createAlbumPage = homePage.goToCreateAlbum();
		homePage = createAlbumPage.createAlbumAs(albumName, albumDescription);
		String[] messages = { "The album was created successfully." };
		assertArrayEquals(messages, homePage.fetchSuccessMessages());

		ViewAlbumPage viewAlbumPage = homePage.viewAlbumWith(albumName, albumDescription);
		EditAlbumPage editAlbumPage = viewAlbumPage.goToEditAlbumPage();
		viewAlbumPage = editAlbumPage.editAlbumAs("Album No.1", "My very first album");
		String[] editMessages = { "The album was edited successfully." };
		assertArrayEquals(editMessages, viewAlbumPage.fetchSuccessMessages());
	}

	@Test
	public void testEditAlbumWithNoValues() throws Exception
	{
		String albumName = "Album#1";
		String albumDescription = "My first album";
		driver.get(contextPath.toString());
		IndexPage indexPage = new IndexPage(driver, contextPath);
		SignupPage signupPage = indexPage.chooseToSignup();
		indexPage = signupPage.signupAs("User#1", "password");
		String[] message = { "The new user account has been created. You may now login." };
		assertArrayEquals(message, indexPage.fetchSuccessMessages());

		LoginPage loginPage = indexPage.chooseToLogin();
		HomePage homePage = loginPage.loginAs("User#1", "password");
		CreateAlbumPage createAlbumPage = homePage.goToCreateAlbum();
		homePage = createAlbumPage.createAlbumAs(albumName, albumDescription);
		String[] messages = { "The album was created successfully." };
		assertArrayEquals(messages, homePage.fetchSuccessMessages());

		ViewAlbumPage viewAlbumPage = homePage.viewAlbumWith(albumName, albumDescription);
		EditAlbumPage editAlbumPage = viewAlbumPage.goToEditAlbumPage();
		editAlbumPage = editAlbumPage.editAlbumAsExpectingError("", "");
		String[] editMessages = { "The name of the album should be between 1 and 50 characters in length." };
		assertArrayEquals(editMessages, editAlbumPage.fetchErrorMessages());
	}

	@Test
	public void testEditAlbumWithExcessiveValues() throws Exception
	{
		String albumName = "Album#1";
		String albumDescription = "My first album";
		driver.get(contextPath.toString());
		IndexPage indexPage = new IndexPage(driver, contextPath);
		SignupPage signupPage = indexPage.chooseToSignup();
		indexPage = signupPage.signupAs("User#1", "password");
		String[] message = { "The new user account has been created. You may now login." };
		assertArrayEquals(message, indexPage.fetchSuccessMessages());

		LoginPage loginPage = indexPage.chooseToLogin();
		HomePage homePage = loginPage.loginAs("User#1", "password");
		CreateAlbumPage createAlbumPage = homePage.goToCreateAlbum();
		homePage = createAlbumPage.createAlbumAs(albumName, albumDescription);
		String[] messages = { "The album was created successfully." };
		assertArrayEquals(messages, homePage.fetchSuccessMessages());

		ViewAlbumPage viewAlbumPage = homePage.viewAlbumWith(albumName, albumDescription);
		EditAlbumPage editAlbumPage = viewAlbumPage.goToEditAlbumPage();
		editAlbumPage = editAlbumPage
				.editAlbumAsExpectingError(
						"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
						"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		String[] editMessages = { "The name of the album should be between 1 and 50 characters in length.",
				"The description of the album should be between 0 and 255 characters in length." };
		assertArrayEquals(editMessages, editAlbumPage.fetchErrorMessages());
	}

	@Test
	public void testEditAlbumAsDuplicate() throws Exception
	{
		String albumName = "Album#1";
		String albumDescription = "My first album";
		String secondAlbumName = "Album#2";
		String secondAlbumDescription = "My second album";

		driver.get(contextPath.toString());
		IndexPage indexPage = new IndexPage(driver, contextPath);
		SignupPage signupPage = indexPage.chooseToSignup();
		indexPage = signupPage.signupAs("User#1", "password");
		String[] message = { "The new user account has been created. You may now login." };
		assertArrayEquals(message, indexPage.fetchSuccessMessages());

		LoginPage loginPage = indexPage.chooseToLogin();
		HomePage homePage = loginPage.loginAs("User#1", "password");
		CreateAlbumPage createAlbumPage = homePage.goToCreateAlbum();
		homePage = createAlbumPage.createAlbumAs(albumName, albumDescription);
		String[] messages = { "The album was created successfully." };
		assertArrayEquals(messages, homePage.fetchSuccessMessages());

		createAlbumPage = homePage.goToCreateAlbum();
		homePage = createAlbumPage.createAlbumAs(secondAlbumName, secondAlbumDescription);
		assertArrayEquals(messages, homePage.fetchSuccessMessages());

		ViewAlbumPage viewAlbumPage = homePage.viewAlbumWith(secondAlbumName, secondAlbumDescription);
		EditAlbumPage editAlbumPage = viewAlbumPage.goToEditAlbumPage();
		editAlbumPage = editAlbumPage.editAlbumAsExpectingError(albumName, albumDescription);
		String[] editMessages = { "An existing album with the same name and description was found." };
		assertArrayEquals(editMessages, editAlbumPage.fetchErrorMessages());
	}

	@Test
	public void testDeleteAlbum() throws Exception
	{
		String albumName = "Album#1";
		String albumDescription = "My first album";

		driver.get(contextPath.toString());
		IndexPage indexPage = new IndexPage(driver, contextPath);
		SignupPage signupPage = indexPage.chooseToSignup();
		indexPage = signupPage.signupAs("User#1", "password");
		String[] message = { "The new user account has been created. You may now login." };
		assertArrayEquals(message, indexPage.fetchSuccessMessages());

		LoginPage loginPage = indexPage.chooseToLogin();
		HomePage homePage = loginPage.loginAs("User#1", "password");
		CreateAlbumPage createAlbumPage = homePage.goToCreateAlbum();
		homePage = createAlbumPage.createAlbumAs(albumName, albumDescription);
		String[] messages = { "The album was created successfully." };
		assertArrayEquals(messages, homePage.fetchSuccessMessages());

		ViewAlbumPage viewAlbumPage = homePage.viewAlbumWith(albumName, albumDescription);
		homePage = viewAlbumPage.deleteAlbum();
		String[] deletionMessages = { "The album was deleted successfully." };
		assertArrayEquals(deletionMessages, homePage.fetchSuccessMessages());
	}

	@Test
	public void testUploadPhoto() throws Exception
	{
		String albumName = "Album#1";
		String albumDescription = "My first album";
		String photoTitle = "Photo#1";
		String photoDescription = "My first photo";
		String filePath = this.getClass().getClassLoader().getResource("TestPhoto.png").toExternalForm();

		driver.get(contextPath.toString());
		IndexPage indexPage = new IndexPage(driver, contextPath);
		SignupPage signupPage = indexPage.chooseToSignup();
		indexPage = signupPage.signupAs("User#1", "password");
		String[] message = { "The new user account has been created. You may now login." };
		assertArrayEquals(message, indexPage.fetchSuccessMessages());

		LoginPage loginPage = indexPage.chooseToLogin();
		HomePage homePage = loginPage.loginAs("User#1", "password");
		CreateAlbumPage createAlbumPage = homePage.goToCreateAlbum();
		homePage = createAlbumPage.createAlbumAs(albumName, albumDescription);
		String[] messages = { "The album was created successfully." };
		assertArrayEquals(messages, homePage.fetchSuccessMessages());

		UploadPhotoPage uploadPhotoPage = homePage.goToUploadPhoto();
		ViewAlbumPage viewAlbumPage = uploadPhotoPage.uploadAs(photoTitle, photoDescription, filePath);
		String[] uploadMessages = { "The file was uploaded successfully." };
		assertArrayEquals(uploadMessages, viewAlbumPage.fetchSuccessMessages());
	}

	@Test
	public void testUploadPhotoWithNoValues() throws Exception
	{
		String albumName = "Album#1";
		String albumDescription = "My first album";

		driver.get(contextPath.toString());
		IndexPage indexPage = new IndexPage(driver, contextPath);
		SignupPage signupPage = indexPage.chooseToSignup();
		indexPage = signupPage.signupAs("User#1", "password");
		String[] message = { "The new user account has been created. You may now login." };
		assertArrayEquals(message, indexPage.fetchSuccessMessages());

		LoginPage loginPage = indexPage.chooseToLogin();
		HomePage homePage = loginPage.loginAs("User#1", "password");
		CreateAlbumPage createAlbumPage = homePage.goToCreateAlbum();
		homePage = createAlbumPage.createAlbumAs(albumName, albumDescription);
		String[] messages = { "The album was created successfully." };
		assertArrayEquals(messages, homePage.fetchSuccessMessages());

		UploadPhotoPage uploadPhotoPage = homePage.goToUploadPhoto();
		uploadPhotoPage = uploadPhotoPage.uploadAsExpectingError("", "", "");
		String[] uploadMessages = { "The name of the uploaded file should be between 1 and 255 characters in length." };
		assertArrayEquals(uploadMessages, uploadPhotoPage.fetchErrorMessages());
	}

	@Test
	public void testUploadPhotoWithExcessiveValues() throws Exception
	{
		String albumName = "Album#1";
		String albumDescription = "My first album";
		String photoTitle = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
		String photoDescription = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
		String filePath = this.getClass().getClassLoader().getResource("TestPhoto.png").toExternalForm();

		driver.get(contextPath.toString());
		IndexPage indexPage = new IndexPage(driver, contextPath);
		SignupPage signupPage = indexPage.chooseToSignup();
		indexPage = signupPage.signupAs("User#1", "password");
		String[] message = { "The new user account has been created. You may now login." };
		assertArrayEquals(message, indexPage.fetchSuccessMessages());

		LoginPage loginPage = indexPage.chooseToLogin();
		HomePage homePage = loginPage.loginAs("User#1", "password");
		CreateAlbumPage createAlbumPage = homePage.goToCreateAlbum();
		homePage = createAlbumPage.createAlbumAs(albumName, albumDescription);
		String[] messages = { "The album was created successfully." };
		assertArrayEquals(messages, homePage.fetchSuccessMessages());

		UploadPhotoPage uploadPhotoPage = homePage.goToUploadPhoto();
		uploadPhotoPage = uploadPhotoPage.uploadAsExpectingError(photoTitle, photoDescription, filePath);
		String[] uploadMessages = { "The title should be between 0 and 255 characters in length.",
				"The description should be between 0 and 255 characters in length." };
		assertArrayEquals(uploadMessages, uploadPhotoPage.fetchErrorMessages());
	}

	@Test
	public void testUploadDuplicatePhoto() throws Exception
	{
		String albumName = "Album#1";
		String albumDescription = "My first album";
		String photoTitle = "Photo#1";
		String photoDescription = "My first photo";
		String filePath = this.getClass().getClassLoader().getResource("TestPhoto.png").toExternalForm();

		driver.get(contextPath.toString());
		IndexPage indexPage = new IndexPage(driver, contextPath);
		SignupPage signupPage = indexPage.chooseToSignup();
		indexPage = signupPage.signupAs("User#1", "password");
		String[] message = { "The new user account has been created. You may now login." };
		assertArrayEquals(message, indexPage.fetchSuccessMessages());

		LoginPage loginPage = indexPage.chooseToLogin();
		HomePage homePage = loginPage.loginAs("User#1", "password");
		CreateAlbumPage createAlbumPage = homePage.goToCreateAlbum();
		homePage = createAlbumPage.createAlbumAs(albumName, albumDescription);
		String[] messages = { "The album was created successfully." };
		assertArrayEquals(messages, homePage.fetchSuccessMessages());

		UploadPhotoPage uploadPhotoPage = homePage.goToUploadPhoto();
		ViewAlbumPage viewAlbumPage = uploadPhotoPage.uploadAs(photoTitle, photoDescription, filePath);
		String[] uploadMessages = { "The file was uploaded successfully." };
		assertArrayEquals(uploadMessages, viewAlbumPage.fetchSuccessMessages());

		uploadPhotoPage = viewAlbumPage.goToUploadPhoto();
		uploadPhotoPage = uploadPhotoPage.uploadAsExpectingError(photoTitle, photoDescription, filePath);
		String[] errorMessages = { "A duplicate photo was found in the album." };
		assertArrayEquals(errorMessages, uploadPhotoPage.fetchErrorMessages());
	}

	@Test
	public void testEditPhoto() throws Exception
	{
		String albumName = "Album#1";
		String albumDescription = "My first album";
		String photoTitle = "Photo#1";
		String photoDescription = "My first photo";
		String newPhotoTitle = "Photo No.1";
		String newPhotoDescription = "First photo";
		String filePath = this.getClass().getClassLoader().getResource("TestPhoto.png").toExternalForm();

		driver.get(contextPath.toString());
		IndexPage indexPage = new IndexPage(driver, contextPath);
		SignupPage signupPage = indexPage.chooseToSignup();
		indexPage = signupPage.signupAs("User#1", "password");
		String[] message = { "The new user account has been created. You may now login." };
		assertArrayEquals(message, indexPage.fetchSuccessMessages());

		LoginPage loginPage = indexPage.chooseToLogin();
		HomePage homePage = loginPage.loginAs("User#1", "password");
		CreateAlbumPage createAlbumPage = homePage.goToCreateAlbum();
		homePage = createAlbumPage.createAlbumAs(albumName, albumDescription);
		String[] messages = { "The album was created successfully." };
		assertArrayEquals(messages, homePage.fetchSuccessMessages());

		UploadPhotoPage uploadPhotoPage = homePage.goToUploadPhoto();
		ViewAlbumPage viewAlbumPage = uploadPhotoPage.uploadAs(photoTitle, photoDescription, filePath);
		String[] uploadMessages = { "The file was uploaded successfully." };
		assertArrayEquals(uploadMessages, viewAlbumPage.fetchSuccessMessages());

		ViewPhotoPage viewPhotoPage = viewAlbumPage.viewPhotoWith(photoTitle, photoDescription);
		EditPhotoPage editPhotoPage = viewPhotoPage.goToEditPhoto();
		viewPhotoPage = editPhotoPage.editPhotoAs(newPhotoTitle, newPhotoDescription);
		String[] editMessages = { "The photo was edited successfully." };
		assertArrayEquals(editMessages, viewPhotoPage.fetchSuccessMessages());
	}

	@Test
	public void testEditPhotoWithExcessiveValues() throws Exception
	{
		String albumName = "Album#1";
		String albumDescription = "My first album";
		String photoTitle = "Photo#1";
		String photoDescription = "My first photo";
		String newPhotoTitle = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
		String newPhotoDescription = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
		String filePath = this.getClass().getClassLoader().getResource("TestPhoto.png").toExternalForm();

		driver.get(contextPath.toString());
		IndexPage indexPage = new IndexPage(driver, contextPath);
		SignupPage signupPage = indexPage.chooseToSignup();
		indexPage = signupPage.signupAs("User#1", "password");
		String[] message = { "The new user account has been created. You may now login." };
		assertArrayEquals(message, indexPage.fetchSuccessMessages());

		LoginPage loginPage = indexPage.chooseToLogin();
		HomePage homePage = loginPage.loginAs("User#1", "password");
		CreateAlbumPage createAlbumPage = homePage.goToCreateAlbum();
		homePage = createAlbumPage.createAlbumAs(albumName, albumDescription);
		String[] messages = { "The album was created successfully." };
		assertArrayEquals(messages, homePage.fetchSuccessMessages());

		UploadPhotoPage uploadPhotoPage = homePage.goToUploadPhoto();
		ViewAlbumPage viewAlbumPage = uploadPhotoPage.uploadAs(photoTitle, photoDescription, filePath);
		String[] uploadMessages = { "The file was uploaded successfully." };
		assertArrayEquals(uploadMessages, viewAlbumPage.fetchSuccessMessages());

		ViewPhotoPage viewPhotoPage = viewAlbumPage.viewPhotoWith(photoTitle, photoDescription);
		EditPhotoPage editPhotoPage = viewPhotoPage.goToEditPhoto();
		editPhotoPage = editPhotoPage.editPhotoAsExpectingError(newPhotoTitle, newPhotoDescription);
		String[] errorMessages = { "The title should be between 0 and 255 characters in length.",
				"The description should be between 0 and 255 characters in length." };
		assertArrayEquals(errorMessages, editPhotoPage.fetchErrorMessages());
	}

	@Test
	public void testDeletePhoto() throws Exception
	{
		String albumName = "Album#1";
		String albumDescription = "My first album";
		String photoTitle = "Photo#1";
		String photoDescription = "My first photo";
		String filePath = this.getClass().getClassLoader().getResource("TestPhoto.png").toExternalForm();

		driver.get(contextPath.toString());
		IndexPage indexPage = new IndexPage(driver, contextPath);
		SignupPage signupPage = indexPage.chooseToSignup();
		indexPage = signupPage.signupAs("User#1", "password");
		String[] message = { "The new user account has been created. You may now login." };
		assertArrayEquals(message, indexPage.fetchSuccessMessages());

		LoginPage loginPage = indexPage.chooseToLogin();
		HomePage homePage = loginPage.loginAs("User#1", "password");
		CreateAlbumPage createAlbumPage = homePage.goToCreateAlbum();
		homePage = createAlbumPage.createAlbumAs(albumName, albumDescription);
		String[] messages = { "The album was created successfully." };
		assertArrayEquals(messages, homePage.fetchSuccessMessages());

		UploadPhotoPage uploadPhotoPage = homePage.goToUploadPhoto();
		ViewAlbumPage viewAlbumPage = uploadPhotoPage.uploadAs(photoTitle, photoDescription, filePath);
		String[] uploadMessages = { "The file was uploaded successfully." };
		assertArrayEquals(uploadMessages, viewAlbumPage.fetchSuccessMessages());

		ViewPhotoPage viewPhotoPage = viewAlbumPage.viewPhotoWith(photoTitle, photoDescription);
		viewAlbumPage = viewPhotoPage.deletePhoto();
		String[] deleteMessages = { "The photo was deleted successfully." };
		assertArrayEquals(deleteMessages, viewAlbumPage.fetchSuccessMessages());
	}

	@Test
	public void testModifyCoverPhoto() throws Exception
	{
		String albumName = "Album#1";
		String albumDescription = "My first album";

		String photoTitle = "Photo#1";
		String photoDescription = "My first photo";
		String filePath = this.getClass().getClassLoader().getResource("TestPhoto.png").toExternalForm();

		String secondPhotoTitle = "Photo#2";
		String secondPhotoDescription = "My second photo";
		String secondFilePath = this.getClass().getClassLoader().getResource("TestPhoto-2.png").toExternalForm();

		driver.get(contextPath.toString());
		IndexPage indexPage = new IndexPage(driver, contextPath);
		SignupPage signupPage = indexPage.chooseToSignup();
		indexPage = signupPage.signupAs("User#1", "password");
		String[] message = { "The new user account has been created. You may now login." };
		assertArrayEquals(message, indexPage.fetchSuccessMessages());

		LoginPage loginPage = indexPage.chooseToLogin();
		HomePage homePage = loginPage.loginAs("User#1", "password");
		CreateAlbumPage createAlbumPage = homePage.goToCreateAlbum();
		homePage = createAlbumPage.createAlbumAs(albumName, albumDescription);
		String[] messages = { "The album was created successfully." };
		assertArrayEquals(messages, homePage.fetchSuccessMessages());

		UploadPhotoPage uploadPhotoPage = homePage.goToUploadPhoto();
		ViewAlbumPage viewAlbumPage = uploadPhotoPage.uploadAs(photoTitle, photoDescription, filePath);
		String[] uploadMessages = { "The file was uploaded successfully." };
		assertArrayEquals(uploadMessages, viewAlbumPage.fetchSuccessMessages());

		uploadPhotoPage = viewAlbumPage.goToUploadPhoto();
		viewAlbumPage = uploadPhotoPage.uploadAs(secondPhotoTitle, secondPhotoDescription, secondFilePath);
		assertArrayEquals(uploadMessages, viewAlbumPage.fetchSuccessMessages());

		ViewPhotoPage viewPhotoPage = viewAlbumPage.viewPhotoWith(secondPhotoTitle, secondPhotoDescription);
		viewPhotoPage = viewPhotoPage.setAsCoverPhoto();
		String[] coverPhotoMessages = { "The photo was set as the album cover." };
		assertArrayEquals(coverPhotoMessages, viewPhotoPage.fetchSuccessMessages());
	}

}
