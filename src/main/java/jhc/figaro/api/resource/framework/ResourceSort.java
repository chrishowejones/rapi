package jhc.figaro.api.resource.framework;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort.Order;

public class ResourceSort extends org.springframework.data.domain.Sort {

	public ResourceSort(ResourceSortDirection direction, List<String> properties) {
		super(Direction.fromString(direction.toString()), properties);
	}
	
	public ResourceSort(ResourceSortDirection direction, String... properties) {
		super(Direction.fromString(direction.toString()), properties);
	}

	public ResourceSort(List<ResourceOrder> orders) {
		super(convertOrders(orders));
	}

	private static List<Order> convertOrders(List<ResourceOrder> orders) {
		List<Order> sortOrders = new ArrayList<Order>();
		for (ResourceOrder order : orders) {
			sortOrders.add(order);
		}
		return sortOrders;
	}

	public ResourceSort(Order... orders) {
		super(orders);
	}

	public ResourceSort(String... properties) {
		super(properties);
	}
	
	public class ResourceOrder extends org.springframework.data.domain.Sort.Order {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ResourceOrder(ResourceSortDirection direction, String property) {
			super(Direction.fromString(direction.toString()), property);
		}

		public ResourceOrder(String property) {
			super(property);
		}

		public ResourceSortDirection getResourceDirection() {
			return ResourceSortDirection.fromString(getDirection().toString());
		}
		
	}

	public ResourceOrder getResourceOrderFor(String property) {
		Order order = getOrderFor(property);
		ResourceOrder resourceOrder = new ResourceOrder(ResourceSortDirection.fromString(order.getDirection().toString()), order.getProperty());
		return resourceOrder;		
	}

}
