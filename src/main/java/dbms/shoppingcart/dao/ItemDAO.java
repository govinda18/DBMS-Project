package dbms.shoppingcart.dao;
import dbms.shoppingcart.entity.Item;
import dbms.shoppingcart.model.*;
import java.util.List;


public interface ItemDAO 
{
    
    public Item findItem(String code);
    
    public ItemInfo findItemInfo(String code) ;
  
    public void save(ItemInfo itemInfo);
    
    public List<ItemInfo> getItems();
    
    public PaginationResult<ItemInfo> queryItems(int page,
            int maxResult, int maxNavigationPage  );

    public PaginationResult<ItemInfo> queryItems(int page, int maxResult,
            int maxNavigationPage, String likeName);

    
}