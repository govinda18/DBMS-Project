package dbms.shoppingcart.validator;

import dbms.shoppingcart.dao.ItemDAO;
import dbms.shoppingcart.entity.Item;
import dbms.shoppingcart.model.ItemInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
 
// @Component: As a Bean.
@Component
public class ItemInfoValidator implements Validator {
 
    @Autowired
    private ItemDAO itemDAO;
 
    // This Validator support ProductInfo class.
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == ItemInfo.class;
    }
 
    @Override
    public void validate(Object target, Errors errors) {
        ItemInfo itemInfo = (ItemInfo) target;
 
        // Check the fields of ProductInfo class.
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code", "Code is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "Name is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", "Price is required");
 
        String code = itemInfo.getCode();
        if (code != null && code.length() > 0) {
            if (code.matches("\\s+")) {
                errors.rejectValue("code", "Pattern.itemForm.code");
            } else if(itemInfo.isNewItem()) {
                Item item = itemDAO.findItem(code);
                if (item != null) {
                    errors.reject("code","Duplicate Code");
                }
            }
        }
    }
 
}
