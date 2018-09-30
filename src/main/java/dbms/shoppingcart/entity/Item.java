package dbms.shoppingcart.entity;

import java.io.Serializable;
 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
 
@Entity
@Table(name = "Products")
public class Item implements Serializable {
 
    private static final long serialVersionUID = -1000119078147252957L;
 
    private String code;
    private String name;
    private double price;
    private String category;
 
 
    public Item() {
    }

    @Id
    @Column(name="Code",length=20,nullable=false)
	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}

	@Column(name="Name",length=300,nullable=false)
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	@Column(name="Price",nullable=false)
	public double getPrice() {
		return price;
	}

	
	public void setPrice(double price) {
		this.price = price;
	}

	@Column(name="Category",nullable=true)
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	
    
}