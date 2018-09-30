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
import dbms.shoppingcart.dao.ItemDAO;
import dbms.shoppingcart.entity.Item;
import dbms.shoppingcart.entity.Order;
import dbms.shoppingcart.model.ItemInfo;
import dbms.shoppingcart.model.OrderInfo;
import dbms.shoppingcart.model.PaginationResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
 
// Transactional for Hibernate
@Transactional
public class ItemDAOImpl implements ItemDAO {
 
    @Autowired
    private SessionFactory sessionFactory;
 
    @Override
    public Item findItem(String code) {
        Session session = sessionFactory.getCurrentSession();
        Criteria crit = session.createCriteria(Item.class);
        crit.add(Restrictions.eq("code", code));
        return (Item) crit.uniqueResult();
    }
    
    @Override
    public PaginationResult<ItemInfo> queryItems(int page, int maxResult, int maxNavigationPage,
            String likeName) {
        String sql = "Select new " + ItemInfo.class.getName() //
                + "(p.code, p.name, p.price) " + " from "//
                + Item.class.getName() + " p ";
        if (likeName != null && likeName.length() > 0) {
            sql += " Where lower(p.category) like :likeName ";
        }
        //
        Session session = sessionFactory.getCurrentSession();
 
        Query query = session.createQuery(sql);
        if (likeName != null && likeName.length() > 0) {
            query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
        }
        return new PaginationResult<ItemInfo>(query, page, maxResult, maxNavigationPage);
    }
    @Override
    public PaginationResult<ItemInfo> queryItemsforcategory(String category,int page, int maxResult, int maxNavigationPage) {
        String sql = "Select new " + ItemInfo.class.getName() //
                + "(p.code, p.name, p.price) " + " from "//
                + Item.class.getName() + " p ";
        if (category != null && category.length() > 0) {
            sql += " where Category = ";
            sql += "'category'";
        }
        sql="Select new " + ItemInfo.class.getName() //
                + "(p.code, p.name, p.price) " + " from "//
                + Item.class.getName() + " p where Category='abc'";
        
        Session session = sessionFactory.getCurrentSession();
 
        Query query = session.createQuery(sql);
        
        return new PaginationResult<ItemInfo>(query, page, maxResult, maxNavigationPage);
    }
    @Override
    public PaginationResult<ItemInfo> queryItems(int page, int maxResult, int maxNavigationPage) {
        return queryItems(page, maxResult, maxNavigationPage, null);
    }
 
    @Override
    public ItemInfo findItemInfo(String code) {
        Item item = this.findItem(code);
        if (item == null) {
            return null;
        }
        return new ItemInfo(item.getCode(), item.getName(), item.getPrice());
    }
 
    @Override
    public void save(ItemInfo itemInfo) {
        String code = itemInfo.getCode();
 
        Item item = null;
 
        boolean isNew = false;
        if (code != null) {
            item = this.findItem(code);
        }
        if (item == null) {
            isNew = true;
            item = new Item();
        }
        item.setCode(code);
        item.setName(itemInfo.getName());
        item.setPrice(itemInfo.getPrice());
        if(itemInfo.getCategory()!=null) {
        	item.setCategory(itemInfo.getCategory());
        }
        if (isNew) {

        	this.sessionFactory.getCurrentSession().persist(item);
        }
        this.sessionFactory.getCurrentSession().flush();
    }
    
    public List<ItemInfo> getItems() {
        String sql = "Select new " + ItemInfo.class.getName() //
                + "(p.code, p.name, p.price) " + " from "//
                + Item.class.getName() + " p ";
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
                    ItemInfo record = (ItemInfo) resultScroll.get(0);
                    results.add(record);
                } while (resultScroll.next());
  
            }
            // Go to last record.
            resultScroll.last();
        }
        return results;
        
    }
 
    
}
