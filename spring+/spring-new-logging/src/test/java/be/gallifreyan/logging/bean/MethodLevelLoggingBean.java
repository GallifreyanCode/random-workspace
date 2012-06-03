package be.gallifreyan.logging.bean;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.stereotype.Component;

import be.gallifreyan.logging.LogLevel;
import be.gallifreyan.logging.annotation.LoggableMethod;

@Component(value = "methodLevelLoggingBean")
public class MethodLevelLoggingBean {

	private Date dateProperty;

	private Integer integerProperty;

	private String stringProperty;

	@LoggableMethod(value = LogLevel.TRACE)
	public Date getDateProperty() {
		return dateProperty;
	}

	@LoggableMethod(value = LogLevel.TRACE)
	public void setDateProperty(final Date dateProperty) {
		this.dateProperty = dateProperty;
	}

	@LoggableMethod(value = LogLevel.TRACE)
	public Integer getIntegerProperty() {
		return integerProperty;
	}

	@LoggableMethod(value = LogLevel.TRACE)
	public void setIntegerProperty(final Integer integerProperty) {
		this.integerProperty = integerProperty;
	}

	@LoggableMethod(value = LogLevel.TRACE)
	public String getStringProperty() {
		return stringProperty;
	}

	@LoggableMethod(value = LogLevel.TRACE)
	public void setStringProperty(final String stringProperty) {
		this.stringProperty = stringProperty;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("dateProperty", dateProperty)
				.append("integerProperty", integerProperty).append(
						"stringProperty", stringProperty).toString();
	}
}
