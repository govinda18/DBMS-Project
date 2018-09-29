package dbms.shoppingcart.model;

import dbms.shoppingcart.entity.Category;

public class CategoryInfo 
{
    private String code;
    private String name;
    
    private boolean newCategory=false;
 
 
    public CategoryInfo() 
    {
    }
 
    public CategoryInfo(Category category)
    {
        this.code = category.getCode();
        this.name = category.getName();
    }
 
    public CategoryInfo(String code, String name) 
    {
        this.code = code;
        this.name = name;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public boolean isNewCategory() {
		return newCategory;
	}

	public void setNewCategory(boolean newCategory) {
		this.newCategory = newCategory;
	}
 
}
