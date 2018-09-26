package dbms.shoppingcart.model;

public class OrderDetailInfo 
{
    private String id;
 
    private String itemCode;
    private String itemName;
 
    private int quanity;
    private double price;
    private double amount;
 
    public OrderDetailInfo() 
    {
 
    }
 
    public OrderDetailInfo(String id, String itemCode, String itemName, int quanity, double price, double amount) 
    {
        this.id = id;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.quanity = quanity;
        this.price = price;
        this.amount = amount;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getQuanity() {
		return quanity;
	}

	public void setQuanity(int quanity) {
		this.quanity = quanity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
    
 
}
