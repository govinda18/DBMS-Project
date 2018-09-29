package dbms.shoppingcart.model;

import dbms.shoppingcart.entity.Feedback;

public class FeedbackInfo 
{
    private String orderid;
    private String review;
    private double rating;
    
    private boolean newFeedback=false;
 
 
    public FeedbackInfo() 
    {
    }
 
    public FeedbackInfo(Feedback feedback)
    {
        this.orderid = feedback.getOrderid();
        this.review = feedback.getReview();
        this.rating = feedback.getRating();
    }
 
    public FeedbackInfo(String orderid, String review, double rating) 
    {
        this.orderid = orderid;
        this.review = review;
        this.rating = rating;
    }

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	

	public boolean isNewFeedback() {
		return newFeedback;
	}

	public void setNewFeedback(boolean newFeedback) {
		this.newFeedback = newFeedback;
	}
 
}
