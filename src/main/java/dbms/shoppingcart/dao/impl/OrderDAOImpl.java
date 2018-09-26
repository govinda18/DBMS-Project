package dbms.shoppingcart.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
 
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import dbms.shoppingcart.dao.OrderDAO;
import dbms.shoppingcart.dao.ItemDAO;
import dbms.shoppingcart.entity.Order;
import dbms.shoppingcart.entity.OrderDetail;
import dbms.shoppingcart.entity.Item;
import dbms.shoppingcart.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
 
//Transactional for Hibernate
@Transactional
public class OrderDAOImpl implements OrderDAO {
 
    @Autowired
    private SessionFactory sessionFactory;
 
    @Autowired
    private ItemDAO itemDAO;
 
    private int getMaxOrderNum() 
    {
        String sql = "Select max(o.orderNum) from " + Order.class.getName() + " o ";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(sql);
        Integer value = (Integer) query.uniqueResult();
        if (value == null) {
            return 0;
        }
        return value;
    }
    
    @Override
    public PaginationResult<OrderInfo> listOrderInfo(int page, int maxResult, int maxNavigationPage) {
        String sql = "Select new " + OrderInfo.class.getName()//
                + "(ord.id, ord.orderDate, ord.orderNum, ord.amount, "
                + " ord.customerName, ord.customerAddress, ord.customerEmail, ord.customerPhone) " + " from "
                + Order.class.getName() + " ord "//
                + " order by ord.orderNum desc";
        Session session = this.sessionFactory.getCurrentSession();
 
        Query query = session.createQuery(sql);
 
        return new PaginationResult<OrderInfo>(query, page, maxResult, maxNavigationPage);
    }
    
    @Override
    public void saveOrder(CartInfo cartInfo) 
    {
        Session session = sessionFactory.getCurrentSession();
 
        int orderNum = this.getMaxOrderNum() + 1;
        Order order = new Order();
 
        order.setId(String.valueOf(orderNum));
        order.setOrderNum(orderNum);
        order.setOrderDate(new Date());
        order.setAmount(cartInfo.getAmountTotal());
 
        CustomerInfo customerInfo = cartInfo.getCustomerInfo();
        order.setCustomerName(customerInfo.getName());
        order.setCustomerEmail(customerInfo.getEmail());
        order.setCustomerPhone(customerInfo.getPhone());
        order.setCustomerAddress(customerInfo.getAddress());
 
        session.persist(order);
 
        List<CartLineInfo> lines = cartInfo.getCartLines();
 
        for (CartLineInfo line : lines)
        {
            OrderDetail detail = new OrderDetail();
            detail.setId(UUID.randomUUID().toString());
            detail.setOrder(order);
            detail.setAmount(line.getAmount());
            detail.setPrice(line.getItemInfo().getPrice());
            detail.setQuanity(line.getQuantity());
 
            String code = line.getItemInfo().getCode();
            Item item = this.itemDAO.findItem(code);
            detail.setItem(item);
 
            session.persist(detail);
        }
        cartInfo.setOrderNum(orderNum);
    }
 
 
    public Order findOrder(String orderId) {
        Session session = sessionFactory.getCurrentSession();
        Criteria crit = session.createCriteria(Order.class);
        crit.add(Restrictions.eq("id", orderId));
        return (Order) crit.uniqueResult();
    }
 
    @Override
    public OrderInfo getOrderInfo(String orderId) {
        Order order = this.findOrder(orderId);
        if (order == null) {
            return null;
        }
        return new OrderInfo(order.getId(), order.getOrderDate(), //
                order.getOrderNum(), order.getAmount(), order.getCustomerName(), //
                order.getCustomerAddress(), order.getCustomerEmail(), order.getCustomerPhone());
    }
 
    @Override
    public List<OrderDetailInfo> listOrderDetailInfos(String orderId) {
        String sql = "Select new " + OrderDetailInfo.class.getName() //
                + "(d.id, d.item.code, d.item.name , d.quanity,d.price,d.amount) "//
                + " from " + OrderDetail.class.getName() + " d "//
                + " where d.order.id = :orderId ";
 
        Session session = this.sessionFactory.getCurrentSession();
 
        Query query = session.createQuery(sql);
        query.setParameter("orderId", orderId);
 
        return query.list();
    }
    
    public List<OrderInfo> getOrders() {
        String sql = "Select new " + OrderInfo.class.getName()//
                + "(ord.id, ord.orderDate, ord.orderNum, ord.amount, "
                + " ord.customerName, ord.customerAddress, ord.customerEmail, ord.customerPhone) " + " from "
                + Order.class.getName() + " ord "//
                + " order by ord.orderNum desc";
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
                    OrderInfo record = (OrderInfo) resultScroll.get(0);
                    results.add(record);
                } while (resultScroll.next());
  
            }
            // Go to last record.
            resultScroll.last();
        }
        return results;
        
    }
 
}
