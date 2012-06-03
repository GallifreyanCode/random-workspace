package be.gallifreyan.logging.bean;

import java.math.BigDecimal;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.stereotype.Component;

import be.gallifreyan.logging.annotation.LoggableClass;

@Component(value = "classLevelLoggingBean")
@LoggableClass
public class ClassLevelLoggingBean extends MethodLevelLoggingBean {

	private BigDecimal decimalProperty;

	public BigDecimal getDecimalProperty() {
		return decimalProperty;
	}

	public void setDecimalProperty(final BigDecimal decimalProperty) {
		this.decimalProperty = decimalProperty;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("decimalProperty",
				decimalProperty).appendSuper(super.toString()).toString();
	}
}
