package dbms.shoppingcart.validator;

import dbms.shoppingcart.dao.FeedbackDAO;
import dbms.shoppingcart.entity.Feedback;
import dbms.shoppingcart.model.FeedbackInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
 
// @Component: As a Bean.
@Component
public class FeedbackInfoValidator implements Validator {
 
    @Autowired
    private FeedbackDAO feedbackDAO;
 
    // This Validator support ProductInfo class.
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == FeedbackInfo.class;
    }
 
    @Override
    public void validate(Object target, Errors errors) {
        FeedbackInfo feedbackInfo = (FeedbackInfo) target;
 
        // Check the fields of ProductInfo class.
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "orderid", "OrderID is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "review", "Review is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "rating", "Rating is required");
 
        String orderid = feedbackInfo.getOrderid();
        if (orderid != null && orderid.length() > 0) {
            if (orderid.matches("\\s+")) {
                errors.rejectValue("orderid", "Pattern.feedbackForm.orderid");
            } else if(feedbackInfo.isNewFeedback()) {
                Feedback feedback = feedbackDAO.findFeedback(orderid);
                if (feedback != null) {
                    errors.reject("orderid","Duplicate OrderID");
                }
            }
        }
    }
 
}
