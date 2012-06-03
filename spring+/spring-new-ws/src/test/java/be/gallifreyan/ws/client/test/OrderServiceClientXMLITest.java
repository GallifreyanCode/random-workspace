package be.gallifreyan.ws.client.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import be.gallifreyan.ws.model.Address;
import be.gallifreyan.ws.model.Customer;
import be.gallifreyan.ws.model.FoodItem;
import be.gallifreyan.ws.model.FoodItemType;
import be.gallifreyan.ws.model.Name;
import be.gallifreyan.ws.model.Order;
import be.gallifreyan.ws.server.service.OrderService;

public class OrderServiceClientXMLITest {
	private static ClassPathXmlApplicationContext context = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		context = new ClassPathXmlApplicationContext("/applicationContext.xml");
	}

	@Test
	public void testCancelOrder() {
		OrderService client = (OrderService) context
				.getBean("OrderServiceClient");
		boolean cancelled = client
				.cancelOrder("Ref-2010-9-15-0.8432452204897198");
		
		Assert.assertTrue(cancelled);
	}

	@Test
	public void testPlaceOrder() {
		OrderService client = (OrderService) context
				.getBean("OrderServiceClient");

		Order order = prepareDummyOrder();
		String orderRef = client.placeOrder(order);

		Assert.assertNotNull(orderRef);
	}

	private Order prepareDummyOrder() {
		Order order = new Order();
		order.setCustomer(prepareCustomer());
		try {
			order.setDateSubmitted(prepareDate(2010, 10, 15, 8, 00, 00));
			order.setOrderDate(prepareDate(2010, 10, 15, 12, 00, 00));
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		order.getItems().addAll(prepareOrderItems());
		return order;
	}

	private List<FoodItem> prepareOrderItems() {
		List<FoodItem> items = new ArrayList<FoodItem>(5);
		items.add(prepareFoodItem("Vegetable Thali",

		FoodItemType.MEALS, 2));
		items.add(prepareFoodItem("Kheer/ Palpayasam",

		FoodItemType.DESSERTS, 4));
		items.add(prepareFoodItem("Fresh Orange Juice",

		FoodItemType.JUICES, 1));
		items.add(prepareFoodItem("Fresh Carrot Juice",

		FoodItemType.JUICES, 1));
		items.add(prepareFoodItem("Sweet Corn Soup",

		FoodItemType.STARTERS, 2));
		return items;
	}

	private XMLGregorianCalendar prepareDate(int year, int month, int date,
			int hour, int minute, int second)
			throws DatatypeConfigurationException {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, date);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		return DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
	}

	private FoodItem prepareFoodItem(String name, FoodItemType type,
			double quantity) {
		FoodItem item = new FoodItem();
		item.setName(name);
		item.setType(type);
		item.setQuantity(quantity);
		return item;
	}

	private Customer prepareCustomer() {
		Customer customer = new Customer();
		customer.setName(prepareCustomerName());
		customer.setAddressPrimary(prepareAddress("123", "My Office Building",
				"My Office Street", "Dubai", "United Arab Emirates",
				"0097150xxxxxxx", "009714xxxxxxx", "shameer@mycompany.com"));
		customer.setAddressSecondary(prepareAddress("234", "My Home Building",
				"My Home Street", "Dubai", "United Arab Emirates",
				"0097150xxxxxxx", "009714xxxxxxx", "shameer@myhome.com"));
		return customer;
	}

	private Name prepareCustomerName() {

		Name name = new Name();
		name.setLName("Shameer");
		name.setFName("Kunjumohamed");
		return name;
	}

	private Address prepareAddress(String doorNo, String building,
			String street, String city, String country, String phoneMobile,
			String phoneLandline, String email) {

		Address address = new Address();
		address.setDoorNo(doorNo);
		address.setBuilding(building);
		address.setStreet(street);
		address.setCity(city);
		address.setCountry(country);
		address.setPhoneMobile(phoneMobile);
		address.setPhoneLandLine(phoneLandline);
		address.setEmail(email);
		return address;
	}
}
