package io.github.springcrudsimplejdbcmapper.core;

import io.github.simplejdbcmapper.annotation.Column;
import io.github.simplejdbcmapper.annotation.Id;
import io.github.simplejdbcmapper.annotation.IdType;
import io.github.simplejdbcmapper.annotation.Table;

@Table(name = "product")
public class Product {
	@Id(type = IdType.AUTO_GENERATED)
	private Integer Id;

	@Column
	private String sku;

	@Column(name = "product_name")
	private String name;

	@Column
	private String description;

	@Column
	private Double cost;

	private String nonDatabaseColumn;

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public String getNonDatabaseColumn() {
		return nonDatabaseColumn;
	}

	public void setNonDatabaseColumn(String nonDatabaseColumn) {
		this.nonDatabaseColumn = nonDatabaseColumn;
	}

}
