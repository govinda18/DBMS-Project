package dbms.shoppingcart.validator;

import dbms.shoppingcart.dao.CategoryDAO;
import dbms.shoppingcart.entity.Category;
import dbms.shoppingcart.model.CategoryInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
 
// @Component: As a Bean.
@Component
public class CategoryInfoValidator implements Validator {
 
    @Autowired
    private CategoryDAO categoryDAO;
 
    // This Validator support ProductInfo class.
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == CategoryInfo.class;
    }
 
    @Override
    public void validate(Object target, Errors errors) {
        CategoryInfo categoryInfo = (CategoryInfo) target;
 
        // Check the fields of ProductInfo class.
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code", "Code is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "Name is required");
 
        String code = categoryInfo.getCode();
        if (code != null && code.length() > 0) {
            if (code.matches("\\s+")) {
                errors.rejectValue("code", "Pattern.categoryForm.code");
            } else if(categoryInfo.isNewCategory()) {
                Category category = categoryDAO.findCategory(code);
                if (category != null) {
                    errors.reject("code","Duplicate Code");
                }
            }
        }
    }
 
}
