package be.gallifreyan.hornetq;

import static org.junit.Assert.*;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TheITest {
	private static final String expectedFactory = "org.jnp.interfaces.NamingContextFactory";
	private static final String expectedProviderUrl = "localhost:1099";
	private static final String expectedFactoryUrl = "org.jboss.naming:org.jnp.interfaces";
	private static final String MESSAGE = "This is a test message!";
	private Connection connection = null;
	private InitialContext initialContext = null;
	private Queue queue;

	@Before
	public void startUp() {
		try {
			// Step 1. Create an initial context to perform the JNDI lookup.
			initialContext = new InitialContext();
			assertNotNull(initialContext);
			testLoadedJNDIProperties();
			// Step 2. Perfom a lookup on the queue
			queue = (Queue) initialContext.lookup("/queue/exampleQueue");
			assertNotNull(queue);
			// Step 3. Perform a lookup on the Connection Factory
			ConnectionFactory cf = (ConnectionFactory) initialContext
					.lookup("/ConnectionFactory");
			assertNotNull(cf);
			// Step 4.Create a JMS Connection
			connection = cf.createConnection();
			assertNotNull(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testLoadedJNDIProperties() {
		try {
			String loadedFactory = initialContext.getEnvironment()
					.get("java.naming.factory.initial").toString();
			String loadedProviderUrl = initialContext.getEnvironment()
					.get("java.naming.provider.url").toString();
			String loadedFactoryUrl = initialContext.getEnvironment()
					.get("java.naming.factory.url.pkgs").toString();

			assertNotNull(loadedFactory);
			assertNotNull(loadedProviderUrl);
			assertNotNull(loadedFactoryUrl);
			assertEquals(expectedFactory, loadedFactory);
			assertEquals(expectedProviderUrl, loadedProviderUrl);
			assertEquals(expectedFactoryUrl, loadedFactoryUrl);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void thisTest() {
		try {
			// Step 5. Create a JMS Session
			Session session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			assertNotNull(session);
			// Step 6. Create a JMS Message Producer
			MessageProducer producer = session.createProducer(queue);
			assertNotNull(producer);
			// Step 7. Create a Text Message
			TextMessage message = session.createTextMessage(MESSAGE);
			assertNotNull(message);
			assertEquals(MESSAGE, message.getText());

			// Step 8. Send the Message
			producer.send(message);

			// Step 9. Create a JMS Message Consumer
			MessageConsumer messageConsumer = session.createConsumer(queue);
			assertNotNull(messageConsumer);
			// Step 10. Start the Connection
			connection.start();

			// Step 11. Receive the message
			TextMessage messageReceived = (TextMessage) messageConsumer
					.receive(5000);
			
			assertNotNull(messageReceived);
			assertEquals(MESSAGE, messageReceived.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@After
	public void tearDown() {
		if (initialContext != null) {
			try {
				initialContext.close();
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
}
