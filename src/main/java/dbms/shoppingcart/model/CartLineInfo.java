package dbms.shoppingcart.model;

public class CartLineInfo {
	 
    private ItemInfo itemInfo;
    private int quantity;
 
    public CartLineInfo() {
        this.quantity = 0;
    }
 
    public ItemInfo getItemInfo() {
        return itemInfo;
    }
 
    public void setItemInfo(ItemInfo itemInfo) {
        this.itemInfo = itemInfo;
    }
 
    public int getQuantity() {
        return quantity;
    }
 
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
 
    public double getAmount() {
        return this.itemInfo.getPrice() * this.quantity;
    }
    
}
