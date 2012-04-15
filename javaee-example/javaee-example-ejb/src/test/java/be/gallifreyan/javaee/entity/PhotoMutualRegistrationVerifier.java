package be.gallifreyan.javaee.entity;

import static be.gallifreyan.javaee.registration.context.RelationType.MANY_TO_ONE;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.*;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.*;

import be.gallifreyan.javaee.registration.context.TestContext;
import be.gallifreyan.javaee.verifier.factory.ITypeFactory;
import be.gallifreyan.javaee.verifier.registration.*;

@RunWith(Parameterized.class)
public class PhotoMutualRegistrationVerifier {
	private static final TestContext[] contexts = new TestContext[] { new TestContext(
			Photo.class, "album", Album.class, "photos", MANY_TO_ONE) };
	private static final Logger logger = LoggerFactory
			.getLogger(PhotoMutualRegistrationVerifier.class);
	private ITypeFactory typeFactory = new DomainTypeFactory();
	private TestContext testContext;

	@Parameters
	public static Collection<Object[]> data() {
		return VerifierHelper.fetchPopulatedContexts(contexts);
	}

	public PhotoMutualRegistrationVerifier(TestContext testContext) {
		this.testContext = testContext;
	}

	@Test
	public void testProperty() throws Exception {
		logger.info("Executing Test Context {}", testContext);
		MutualRegistrationVerifier verifier = MutualRegistrationVerifier
				.forContext(testContext, typeFactory);
		verifier.verify();
	}
}
