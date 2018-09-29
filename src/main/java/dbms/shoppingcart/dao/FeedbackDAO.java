package dbms.shoppingcart.dao;

import dbms.shoppingcart.entity.Feedback;
import dbms.shoppingcart.model.FeedbackInfo;
import dbms.shoppingcart.model.PaginationResult;

import java.util.List;

public interface FeedbackDAO 
{
    
    public Feedback findFeedback(String orderid);
    
    public FeedbackInfo findFeedbackInfo(String orderid) ;
  
    public void save(FeedbackInfo feedbackInfo);
    
    public List<FeedbackInfo> getFeedbacks();
    
    public PaginationResult<FeedbackInfo> queryFeedbacks(int page,
            int maxResult, int maxNavigationPage  );

    public PaginationResult<FeedbackInfo> queryFeedbacks(int page, int maxResult,
            int maxNavigationPage, String likeName);

    
}
