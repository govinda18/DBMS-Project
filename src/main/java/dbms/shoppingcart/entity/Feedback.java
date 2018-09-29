package dbms.shoppingcart.entity;

import java.io.Serializable;
 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
 
@Entity
@Table(name = "Feedback")
public class Feedback  implements Serializable{
	
	private static final long serialVersionUID = 10l;
 
 
    private String orderid;
    private String review;
    private double rating;
 
 
    public Feedback() {
    }

    @Id
    @Column(name="OrderID",nullable=false)
	public String getOrderid() {
		return orderid;
	}


	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	@Column(name="Review",length=300,nullable=false)
	public String getReview() {
		return review;
	}


	public void setReview(String review) {
		this.review = review;
	}

	@Column(name="Rating",nullable=false)
	public double getRating() {
		return rating;
	}

	
	public void setRating(double rating) {
		this.rating = rating;
	}
    
}