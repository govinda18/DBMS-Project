package dbms.shoppingcart.dao.impl;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import dbms.shoppingcart.dao.CategoryDAO;
import dbms.shoppingcart.entity.Category;
import dbms.shoppingcart.entity.Order;
import dbms.shoppingcart.model.CategoryInfo;
import dbms.shoppingcart.model.OrderInfo;
import dbms.shoppingcart.model.PaginationResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
 
// Transactional for Hibernate
@Transactional
public class CategoryDAOImpl implements CategoryDAO {
 
    @Autowired
    private SessionFactory sessionFactory;
 
    @Override
    public Category findCategory(String code) {
    		String sql = "Select new " + CategoryInfo.class.getName() //
                + "(p.code, p.name) " + " from "//
                + Category.class.getName() + " p where Code = '" + code + "'";	
            System.out.println(sql);
    		Session session = this.sessionFactory.getCurrentSession();
			Query query = session.createQuery(sql);
			Category category = (Category) query.uniqueResult();
			return category;
    }
    
    @Override
    public PaginationResult<CategoryInfo> queryCategorys(int page, int maxResult, int maxNavigationPage,
            String likeName) {
        String sql = "Select new " + CategoryInfo.class.getName() //
                + "(p.code, p.name) " + " from "//
                + Category.class.getName() + " p ";
        if (likeName != null && likeName.length() > 0) {
            sql += " Where lower(p.name) like :likeName ";
        }
        //
        Session session = sessionFactory.getCurrentSession();
 
        Query query = session.createQuery(sql);
        if (likeName != null && likeName.length() > 0) {
            query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
        }
        return new PaginationResult<CategoryInfo>(query, page, maxResult, maxNavigationPage);
    }
    
    @Override
    public PaginationResult<CategoryInfo> queryCategorys(int page, int maxResult, int maxNavigationPage) {
        return queryCategorys(page, maxResult, maxNavigationPage, null);
    }
 
    @Override
    public CategoryInfo findCategoryInfo(String code) {
        Category category = this.findCategory(code);
        if (category == null) {
            return null;
        }
        return new CategoryInfo(category.getCode(), category.getName());
    }
 
    @Override
    public void save(CategoryInfo categoryInfo) {
        String code = categoryInfo.getCode();
 
        Category category = null;
 
        boolean isNew = false;
        if (code != null) {
            category = this.findCategory(code);
        }
        if (category == null) {
            isNew = true;
            category = new Category();
        }
        category.setCode(code);
        category.setName(categoryInfo.getName());
        if (isNew) {
        	try {
            	String sql = "insert into " +  Category.class.getName() +  "(Code,Name) values('" + category.getCode()//
            					+ "','" + category.getName() +"')";
            			;
            	System.out.println(sql);
            	Session session = this.sessionFactory.getCurrentSession();
            	Query query = session.createQuery(sql);
            	int result = query.executeUpdate();
            	System.out.println(result);
        	}
        	catch (Exception e){
        	this.sessionFactory.getCurrentSession().persist(category);}
        }
        this.sessionFactory.getCurrentSession().flush();
    }
    
    public List<CategoryInfo> getCategorys() {
        String sql = "Select new " + CategoryInfo.class.getName() //
                + "(p.code, p.name) " + " from "//
                + Category.class.getName() + " p ";
        Session session = this.sessionFactory.getCurrentSession();
 
        Query query = session.createQuery(sql);
        ScrollableResults resultScroll = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
        List results = new ArrayList();
        
        boolean hasResult = resultScroll.first();
  
        if (hasResult) {
            // Scroll to position:
            hasResult = resultScroll.scroll(0);
  
            if (hasResult) {
                do {
                    CategoryInfo record = (CategoryInfo) resultScroll.get(0);
                    results.add(record);
                } while (resultScroll.next());
  
            }
            // Go to last record.
            resultScroll.last();
        }
        return results;
        
    }
 
    
}
