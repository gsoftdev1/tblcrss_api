package com.tablecross.api.object;

import com.tablecross.api.model.AreaDTO;

public class GetAreasResponse extends ErrorInfo {
	private Integer quantity;
	private AreaDTO[] items;

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public AreaDTO[] getItems() {
		return items;
	}

	public void setItems(AreaDTO[] items) {
		this.items = items;
	}

}
