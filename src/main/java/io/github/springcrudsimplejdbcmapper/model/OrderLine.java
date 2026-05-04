package io.github.springcrudsimplejdbcmapper.model;

import io.github.simplejdbcmapper.annotation.Column;
import io.github.simplejdbcmapper.annotation.Id;
import io.github.simplejdbcmapper.annotation.IdType;
import io.github.simplejdbcmapper.annotation.Table;

@Table(name = "order_line")
public class OrderLine {
	@Id(type = IdType.AUTO_GENERATED)
	private Integer id;

	@Column
	private Integer orderId;

	@Column
	private Integer productId;

	@Column
	private Integer numOfUnits;

	private Product product;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getNumOfUnits() {
		return numOfUnits;
	}

	public void setNumOfUnits(Integer numOfUnits) {
		this.numOfUnits = numOfUnits;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
