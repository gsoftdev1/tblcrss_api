package com.tablecross.api.object;

import com.tablecross.api.model.RestaurantsDTO;

public class SearchRestaurantResponse extends ErrorInfo {
	private Integer quantity;
	private RestaurantsDTO[] items;

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public RestaurantsDTO[] getItems() {
		return items;
	}

	public void setItems(RestaurantsDTO[] items) {
		this.items = items;
	}

}
