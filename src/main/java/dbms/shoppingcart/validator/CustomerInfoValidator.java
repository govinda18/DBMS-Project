package dbms.shoppingcart.validator;

import org.apache.commons.validator.routines.EmailValidator;
import dbms.shoppingcart.model.CustomerInfo;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

//@Component: As a Bean.
@Component
public class CustomerInfoValidator implements Validator 
{

   private EmailValidator emailValidator = EmailValidator.getInstance();

   // This Validator support CustomerInfo class.
   @Override
   public boolean supports(Class<?> clazz) {
       return clazz == CustomerInfo.class;
   }

   @Override
   public void validate(Object target, Errors errors) {
       CustomerInfo custInfo = (CustomerInfo) target;

       // Check the fields of CustomerInfo class.
       ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "Name is Required");
       ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "Email is required");
       ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "Address is required");
       ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "Phone is required");

       if (!emailValidator.isValid(custInfo.getEmail())) {
           errors.rejectValue("email", "Email is invalid");
       }
   }

}
