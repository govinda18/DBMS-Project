package dbms.shoppingcart.dao;
import dbms.shoppingcart.entity.Category;
import dbms.shoppingcart.model.*;
import java.util.List;


public interface CategoryDAO 
{
    
    public Category findCategory(String code);
    
    public CategoryInfo findCategoryInfo(String code) ;
  
    public void save(CategoryInfo itemInfo);
    
    public List<CategoryInfo> getCategorys();
    
    public PaginationResult<CategoryInfo> queryCategorys(int page,
            int maxResult, int maxNavigationPage  );

    public PaginationResult<CategoryInfo> queryCategorys(int page, int maxResult,
            int maxNavigationPage, String likeName);

    
}