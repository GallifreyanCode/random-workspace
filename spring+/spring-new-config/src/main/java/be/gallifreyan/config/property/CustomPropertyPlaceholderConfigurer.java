package be.gallifreyan.config.property;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class CustomPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    private Properties props;

    public void loadProperties(Properties props) throws IOException {
        super.loadProperties(props);
        this.props = props;
    }

    public Properties getProps() {
        return props;
    }
}
