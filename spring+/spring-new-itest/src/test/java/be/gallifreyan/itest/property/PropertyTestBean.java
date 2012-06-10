package be.gallifreyan.itest.property;

import org.springframework.stereotype.Component;

import be.gallifreyan.config.property.InjectProperty;

@Component
public class PropertyTestBean {
		@InjectProperty(name = "custom.test")
		private String driverClassName;
		
		public String getTestProperty() {
			return driverClassName;
		}
}
