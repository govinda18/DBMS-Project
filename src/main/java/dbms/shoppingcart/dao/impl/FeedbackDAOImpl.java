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
import dbms.shoppingcart.dao.FeedbackDAO;
import dbms.shoppingcart.entity.Feedback;
import dbms.shoppingcart.entity.Item;
import dbms.shoppingcart.entity.Order;
import dbms.shoppingcart.model.FeedbackInfo;
import dbms.shoppingcart.model.PaginationResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
 
// Transactional for Hibernate
@Transactional
public class FeedbackDAOImpl implements FeedbackDAO {
 
    @Autowired
    private SessionFactory sessionFactory;
 
    @Override
    public Feedback findFeedback(String orderid) {
        Session session = sessionFactory.getCurrentSession();
        Criteria crit = session.createCriteria(Feedback.class);
        crit.add(Restrictions.eq("orderid", orderid));
        return (Feedback) crit.uniqueResult();
    }
    
    @Override
    public PaginationResult<FeedbackInfo> queryFeedbacks(int page, int maxResult, int maxNavigationPage,
            String likeReview) {
        String sql = "Select new " + FeedbackInfo.class.getName() //
                + "(p.orderid, p.review, p.rating) " + " from "//
                + Feedback.class.getName() + " p ";
        if (likeReview != null && likeReview.length() > 0) {
            sql += " Where lower(p.review) like :likeReview ";
        }
        //
        Session session = sessionFactory.getCurrentSession();
 
        Query query = session.createQuery(sql);
        if (likeReview != null && likeReview.length() > 0) {
            query.setParameter("likeReview", "%" + likeReview.toLowerCase() + "%");
        }
        return new PaginationResult<FeedbackInfo>(query, page, maxResult, maxNavigationPage);
    }
    
    @Override
    public PaginationResult<FeedbackInfo> queryFeedbacks(int page, int maxResult, int maxNavigationPage) {
        return queryFeedbacks(page, maxResult, maxNavigationPage, null);
    }
 
    @Override
    public FeedbackInfo findFeedbackInfo(String orderid) {
        Feedback feedback = this.findFeedback(orderid);
        if (feedback == null) {
            return null;
        }
        return new FeedbackInfo(feedback.getOrderid(), feedback.getReview(), feedback.getRating());
    }
 
    @Override
    public void save(FeedbackInfo feedbackInfo) {
        String orderid = feedbackInfo.getOrderid();
 
        Feedback feedback = null;
 
        boolean isNew = false;
        if (orderid != null) {
            feedback = this.findFeedback(orderid);
        }
        if (feedback == null) {
            isNew = true;
            feedback = new Feedback();
        }
        feedback.setOrderid(orderid);
        feedback.setReview(feedbackInfo.getReview());
        feedback.setRating(feedbackInfo.getRating());
        if (isNew) {

        	this.sessionFactory.getCurrentSession().persist(feedback);
        }
        this.sessionFactory.getCurrentSession().flush();
    }
    
    public List<FeedbackInfo> getFeedbacks() {
        String sql = "Select new " + FeedbackInfo.class.getName() //
                + "(p.orderid, p.review, p.rating) " + " from "//
                + Feedback.class.getName() + " p ";
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
                    FeedbackInfo record = (FeedbackInfo) resultScroll.get(0);
                    results.add(record);
                } while (resultScroll.next());
  
            }
            // Go to last record.
            resultScroll.last();
        }
        return results;
        
    }
 
    
}
