package be.gallifreyan.javaee.entity;

import java.beans.PropertyDescriptor;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.*;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.*;

import be.gallifreyan.javaee.verifier.bean.*;
import be.gallifreyan.javaee.verifier.factory.ITypeFactory;

@RunWith(Parameterized.class)
public class AlbumBeanVerifier {
	private static final Class<Album> TEST_TYPE = Album.class;
	private static final Logger logger = LoggerFactory
			.getLogger(AlbumBeanVerifier.class);
	private ITypeFactory typeFactory = new DomainTypeFactory();
	private PropertyDescriptor descriptor;

	@Parameters
	public static Collection<Object[]> properties() {
		return VerifierHelper.getProperties(TEST_TYPE);
	}

	public AlbumBeanVerifier(PropertyDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	@Test
	public void testProperty() throws Exception {
		if (descriptor.getName().equals("coverPhoto")) {
			// CoverPhoto property verification cannot be done using ths
			// BeanVerifier, as the setter checks for valid photos within the
			// photos property. Hence, the test is treated as a pass.
			// Verification is done separately in the AlbumOtherTests class.
			return;
		}
		logger.info("Verifying property {} in bean {}", descriptor.getName(),
				TEST_TYPE);
		BeanVerifier<Album> verifier = BeanVerifier.forProperty(TEST_TYPE,
				descriptor, typeFactory);
		verifier.verify();
	}
}
