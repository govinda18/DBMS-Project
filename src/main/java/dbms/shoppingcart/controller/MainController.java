package dbms.shoppingcart.controller;


import java.util.*;

import javax.servlet.http.HttpServletRequest;
 
import dbms.shoppingcart.dao.OrderDAO;
import dbms.shoppingcart.dao.ItemDAO;
import dbms.shoppingcart.dao.FeedbackDAO;
import dbms.shoppingcart.dao.CategoryDAO;
import dbms.shoppingcart.entity.*;
import dbms.shoppingcart.model.CartInfo;
import dbms.shoppingcart.model.CustomerInfo;
import dbms.shoppingcart.model.FeedbackInfo;
import dbms.shoppingcart.model.ItemInfo;
import dbms.shoppingcart.model.PaginationResult;
import dbms.shoppingcart.model.CategoryInfo;
import dbms.shoppingcart.util.Utils;
import dbms.shoppingcart.validator.CustomerInfoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
 
@Controller
// Enable Hibernate Transaction.
@Transactional
// Need to use RedirectAttributes
@EnableWebMvc
public class MainController {
 
    @Autowired
    private OrderDAO orderDAO;
 
    @Autowired
    private ItemDAO itemDAO;
 
    @Autowired
    private FeedbackDAO feedbackDAO;
    
    @Autowired
    private CategoryDAO categoryDAO;
    
    @Autowired
    private CustomerInfoValidator customerInfoValidator;
 
    @InitBinder
    public void myInitBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        System.out.println("Target=" + target);
 
        // For Cart Form.
        // (@ModelAttribute("cartForm") @Validated CartInfo cartForm)
        if (target.getClass() == CartInfo.class) {
 
        }
        // For Customer Form.
        // (@ModelAttribute("customerForm") @Validated CustomerInfo
        // customerForm)
        else if (target.getClass() == CustomerInfo.class) {
            dataBinder.setValidator(customerInfoValidator);
        }
 
    }
 
    @RequestMapping("/403")
    public String accessDenied() {
        return "/403";
    }
 
    @RequestMapping("/")
    public String home() {
        return "index";
    }
    
    @RequestMapping("/paymentsuccess")
    public String paymentsuccess() {
        return "PaymentCompleted";
    }
 
    @RequestMapping({ "/productList" })
    public String listProductHandler(Model model, //
            @RequestParam(value = "name", defaultValue = "") String likeName,
            @RequestParam(value = "page", defaultValue = "1") int page) {
        final int maxResult = 10;
        final int maxNavigationPage = 10;
 
        PaginationResult<ItemInfo> result =itemDAO.queryItems(page, //
                maxResult, maxNavigationPage, likeName);
 
        model.addAttribute("paginationProducts", result);
        return "productList";
    }

    @RequestMapping({ "/categoryList" })
    public String listCategoryHandler(Model model, //
            @RequestParam(value = "name", defaultValue = "") String likeName,
            @RequestParam(value = "page", defaultValue = "1") int page) {
        final int maxResult = 10;
        final int maxNavigationPage = 10;
 
        PaginationResult<CategoryInfo> result =categoryDAO.queryCategorys(page, //
                maxResult, maxNavigationPage, likeName);
 
        model.addAttribute("paginationProducts", result);
        return "categoryList";
    }    
    
    @RequestMapping({ "/buyProduct" })
    public String listProductHandler(HttpServletRequest request, Model model, //
            @RequestParam(value = "code", defaultValue = "") String code) {
 
        Item item = null;
        if (code != null && code.length() > 0) {
            item = itemDAO.findItem(code);
        }
        if (item != null) {
 
            // Cart info stored in Session.
            CartInfo cartInfo = Utils.getCartInSession(request);
 
            ItemInfo itemInfo = new ItemInfo(item);
 
            cartInfo.addItem(itemInfo, 1);
        }
        // Redirect to shoppingCart page.
        return "redirect:/shoppingCart";
    }
 
    @RequestMapping({ "/shoppingCartRemoveProduct" })
    public String removeProductHandler(HttpServletRequest request, Model model, //
            @RequestParam(value = "code", defaultValue = "") String code) {
        Item item = null;
        if (code != null && code.length() > 0) {
            item = itemDAO.findItem(code);
        }
        if (item != null) {
 
            // Cart Info stored in Session.
            CartInfo cartInfo = Utils.getCartInSession(request);
 
            ItemInfo itemInfo = new ItemInfo(item);
 
            cartInfo.removeItem(itemInfo);
 
        }
        // Redirect to shoppingCart page.
        return "redirect:/shoppingCart";
    }
 
    // POST: Update quantity of products in cart.
    @RequestMapping(value = { "/shoppingCart" }, method = RequestMethod.POST)
    public String shoppingCartUpdateQty(HttpServletRequest request, //
            Model model, //
            @ModelAttribute("cartForm") CartInfo cartForm) {
 
        CartInfo cartInfo = Utils.getCartInSession(request);
        cartInfo.updateQuantity(cartForm);
 
        // Redirect to shoppingCart page.
        return "redirect:/shoppingCart";
    }
 
    // GET: Show Cart
    @RequestMapping(value = { "/shoppingCart" }, method = RequestMethod.GET)
    public String shoppingCartHandler(HttpServletRequest request, Model model) {
        CartInfo myCart = Utils.getCartInSession(request);
 
        model.addAttribute("cartForm", myCart);
        return "shoppingCart";
    }
 
    // GET: Enter customer information.
    @RequestMapping(value = { "/shoppingCartCustomer" }, method = RequestMethod.GET)
    public String shoppingCartCustomerForm(HttpServletRequest request, Model model) {
 
        CartInfo cartInfo = Utils.getCartInSession(request);
      
        // Cart is empty.
        if (cartInfo.isEmpty()) {
             
            // Redirect to shoppingCart page.
            return "redirect:/shoppingCart";
        }
 
        CustomerInfo customerInfo = cartInfo.getCustomerInfo();
        if (customerInfo == null) {
            customerInfo = new CustomerInfo();
        }
 
        model.addAttribute("customerForm", customerInfo);
 
        return "shoppingCartCustomer";
    }
 
    // POST: Save customer information.
    @RequestMapping(value = { "/shoppingCartCustomer" }, method = RequestMethod.POST)
    public String shoppingCartCustomerSave(HttpServletRequest request, //
            Model model, //
            @ModelAttribute("customerForm") @Validated CustomerInfo customerForm, //
            BindingResult result, //
            final RedirectAttributes redirectAttributes) {
  
        // If has Errors.
        if (result.hasErrors()) {
            customerForm.setValid(false);
            // Forward to reenter customer info.
            return "shoppingCartCustomer";
        }
 
        customerForm.setValid(true);
        CartInfo cartInfo = Utils.getCartInSession(request);
 
        cartInfo.setCustomerInfo(customerForm);
 
        // Redirect to Confirmation page.
        return "redirect:/shoppingCartConfirmation";
    }
 
    // GET: Review Cart to confirm.
    @RequestMapping(value = { "/shoppingCartConfirmation" }, method = RequestMethod.GET)
    public String shoppingCartConfirmationReview(HttpServletRequest request, Model model) {
        CartInfo cartInfo = Utils.getCartInSession(request);
 
        // Cart have no products.
        if (cartInfo.isEmpty()) {
            // Redirect to shoppingCart page.
            return "redirect:/shoppingCart";
        } else if (!cartInfo.isValidCustomer()) {
            // Enter customer info.
            return "redirect:/shoppingCartCustomer";
        }
 
        return "shoppingCartConfirmation";
    }
 
    // POST: Send Cart (Save).
    @RequestMapping(value = { "/shoppingCartConfirmation" }, method = RequestMethod.POST)
    // Avoid UnexpectedRollbackException (See more explanations)
    @Transactional(propagation = Propagation.NEVER)
    public String shoppingCartConfirmationSave(HttpServletRequest request, Model model) {
        CartInfo cartInfo = Utils.getCartInSession(request);
 
        // Cart have no products.
        if (cartInfo.isEmpty()) {
            // Redirect to shoppingCart page.
            return "redirect:/shoppingCart";
        } else if (!cartInfo.isValidCustomer()) {
            // Enter customer info.
            return "redirect:/shoppingCartCustomer";
        }
        try {
            orderDAO.saveOrder(cartInfo);
        } catch (Exception e) {
            // Need: Propagation.NEVER?
            return "shoppingCartConfirmation";
        }
        // Remove Cart In Session.
        Utils.removeCartInSession(request);
         
        // Store Last ordered cart to Session.
        Utils.storeLastOrderedCartInSession(request, cartInfo);
 
        // Redirect to successful page.
        return "redirect:/shoppingCartFinalize";
    }
 
    @RequestMapping(value = { "/shoppingCartFinalize" }, method = RequestMethod.GET)
    public String shoppingCartFinalize(HttpServletRequest request, Model model) {
 
        CartInfo lastOrderedCart = Utils.getLastOrderedCartInSession(request);
 
        if (lastOrderedCart == null) {
            return "redirect:/shoppingCart";
        }
 
        return "shoppingCartFinalize";
    }
    
    // GET: Show Feedback.
    @RequestMapping(value = { "/feedback" }, method = RequestMethod.GET)
    public String feedback(Model model, @RequestParam(value = "orderid", defaultValue = "") String orderid) {
        FeedbackInfo feedbackInfo = null;
        
        if (orderid != null ) {
            feedbackInfo = feedbackDAO.findFeedbackInfo(orderid);
        }
        if (feedbackInfo == null) {
            feedbackInfo = new FeedbackInfo();
            feedbackInfo.setNewFeedback(true);
            feedbackInfo.setOrderid(orderid);
        }
        model.addAttribute("feedbackForm", feedbackInfo);
        return "feedback";
    }
 
    // POST: Save product
    @RequestMapping(value = { "/feedback" }, method = RequestMethod.POST)
    // Avoid UnexpectedRollbackException (See more explanations)
    @Transactional(propagation = Propagation.NEVER)
    public String productSave(Model model, //
            @ModelAttribute("feedbackForm") @Validated FeedbackInfo feedbackInfo, //
            BindingResult result, //
            final RedirectAttributes redirectAttributes) {
 
        if (result.hasErrors()) {
            return "feedback";
        }
        try {
            feedbackDAO.save(feedbackInfo);
         
        } catch (Exception e) {
            // Need: Propagation.NEVER?
            String message = e.getMessage();
            model.addAttribute("message", message);
            // Show product form.
            return "feedback";
 
        }
        return "redirect:/productList";
    }
 
     
}
