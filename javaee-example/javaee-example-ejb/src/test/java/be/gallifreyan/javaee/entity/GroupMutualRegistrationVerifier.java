package be.gallifreyan.javaee.entity;

import static be.gallifreyan.javaee.registration.context.RelationType.MANY_TO_MANY;

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
public class GroupMutualRegistrationVerifier {
	private static final TestContext[] contexts = new TestContext[] { new TestContext(
			Group.class, "users", User.class, "groups", MANY_TO_MANY) };
	private static final Logger logger = LoggerFactory
			.getLogger(GroupMutualRegistrationVerifier.class);
	private ITypeFactory typeFactory = new DomainTypeFactory();
	private TestContext testContext;

	@Parameters
	public static Collection<Object[]> data() {
		return VerifierHelper.fetchPopulatedContexts(contexts);
	}

	public GroupMutualRegistrationVerifier(TestContext testContext) {
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
