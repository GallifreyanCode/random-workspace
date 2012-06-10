package be.gallifreyan.config.property;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.inject.Inject;

import org.springframework.beans.BeansException;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;

public class InjectPropertyPostProcessor implements BeanPostProcessor {
	@Inject
    private CustomPropertyPlaceholderConfigurer propertyPlaceholderConfigurer;
    private SimpleTypeConverter typeConverter = new SimpleTypeConverter();
    
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
    
    public Object postProcessBeforeInitialization(final Object bean,
			String beanName) throws BeansException {
		 ReflectionUtils.doWithFields(bean.getClass(), new ReflectionUtils.FieldCallback() {
			 
	            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
	            	ReflectionUtils.makeAccessible(field);
	            	if(field.getAnnotation(InjectProperty.class) != null) {
	            		if (Modifier.isStatic(field.getModifiers())) {
	                        throw new IllegalStateException("PropertyAutowired annotation is not supported on static fields");
	                    }
	            		
	            		Object strValue = propertyPlaceholderConfigurer.getProps().get(field.getAnnotation(InjectProperty.class).name());
	            		
	                    if (strValue != null) {
	                        Object value = typeConverter.convertIfNecessary(strValue, field.getType());
	                        ReflectionUtils.makeAccessible(field);
	                        field.set(bean, value);
	                    }
	            	}
	                    

	                    
	                }
	            });
		 return bean;
		 };
}
