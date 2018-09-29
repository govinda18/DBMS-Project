package dbms.shoppingcart.dao;
import dbms.shoppingcart.model.*;
import java.util.List;

public interface OrderDAO {
	 
    public void saveOrder(CartInfo cartInfo);
    
    
    public OrderInfo getOrderInfo(String orderId);
    
    public List<OrderDetailInfo> listOrderDetailInfos(String orderId);
    
    public List<OrderInfo> getOrders();
    
    public PaginationResult<OrderInfo> listOrderInfo(int page,
            int maxResult, int maxNavigationPage);
}
