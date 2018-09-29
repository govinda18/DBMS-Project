package dbms.shoppingcart.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import dbms.shoppingcart.dao.OrderDAO;
import dbms.shoppingcart.entity.*;
import dbms.shoppingcart.dao.ItemDAO;
import dbms.shoppingcart.model.OrderDetailInfo;
import dbms.shoppingcart.model.OrderInfo;
import dbms.shoppingcart.model.ItemInfo;
import dbms.shoppingcart.model.PaginationResult;
import dbms.shoppingcart.validator.ItemInfoValidator;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
 
@Controller
// Enable Hibernate Transaction.
@Transactional
// Need to use RedirectAttributes
@EnableWebMvc
public class AdminController {
 
    @Autowired
    private OrderDAO orderDAO;
 
    @Autowired
    private ItemDAO itemDAO;
 
    @Autowired
    private ItemInfoValidator itemInfoValidator;
    
    @Autowired
    private SessionFactory sessionFactory;
 
    // Configurated In ApplicationContextConfig.
 
    @InitBinder
    public void myInitBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        System.out.println("Target=" + target);
 
        if (target.getClass() == ItemInfo.class) {
            dataBinder.setValidator(itemInfoValidator);
            // For upload Image.
            dataBinder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
        }
    }
 
    // GET: Show Login Page
    @RequestMapping(value = { "/login" }, method = RequestMethod.GET)
    public String login(Model model) {
 
        return "login";
    }
    
    @RequestMapping(value = { "/accountInfo" }, method = RequestMethod.GET)
    public String accountInfo(Model model) {
 
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(userDetails.getPassword());
        System.out.println(userDetails.getUsername());
        System.out.println(userDetails.isEnabled());
 
        model.addAttribute("userDetails", userDetails);
        return "accountInfo";
    }
 
    @RequestMapping(value = { "/orderList" }, method = RequestMethod.GET)
    public String orderList(Model model, //
            @RequestParam(value = "page", defaultValue = "1") String pageStr) {
        int page = 1;
        try {
            page = Integer.parseInt(pageStr);
        } catch (Exception e) {
        }
        final int MAX_RESULT = 5;
        final int MAX_NAVIGATION_PAGE = 10;
 
        PaginationResult<OrderInfo> paginationResult //
        = orderDAO.listOrderInfo(page, MAX_RESULT, MAX_NAVIGATION_PAGE);
 
        model.addAttribute("paginationResult", paginationResult);
        return "orderList";
    }
 
    // GET: Show product.
    @RequestMapping(value = { "/product" }, method = RequestMethod.GET)
    public String product(Model model, @RequestParam(value = "code", defaultValue = "") String code) {
        ItemInfo itemInfo = null;
 
        if (code != null && code.length() > 0) {
            itemInfo = itemDAO.findItemInfo(code);
        }
        if (itemInfo == null) {
            itemInfo = new ItemInfo();
            itemInfo.setNewItem(true);
        }
        model.addAttribute("productForm", itemInfo);
        return "product";
    }
 
    // POST: Save product
    @RequestMapping(value = { "/product" }, method = RequestMethod.POST)
    // Avoid UnexpectedRollbackException (See more explanations)
    @Transactional(propagation = Propagation.NEVER)
    public String productSave(Model model, //
            @ModelAttribute("productForm") @Validated ItemInfo itemInfo, //
            BindingResult result, //
            final RedirectAttributes redirectAttributes) {
 
        if (result.hasErrors()) {
            return "product";
        }
        try {
            itemDAO.save(itemInfo);
         
        } catch (Exception e) {
            // Need: Propagation.NEVER?
            String message = e.getMessage();
            model.addAttribute("message", message);
            // Show product form.
            return "product";
 
        }
        return "redirect:/productList";
    }
 
    @RequestMapping(value = { "/order" }, method = RequestMethod.GET)
    public String orderView(Model model, @RequestParam("orderId") String orderId) {
        OrderInfo orderInfo = null;
        if (orderId != null) {
            orderInfo = this.orderDAO.getOrderInfo(orderId);
        }
        if (orderInfo == null) {
            return "redirect:/orderList";
        }
        List<OrderDetailInfo> details = this.orderDAO.listOrderDetailInfos(orderId);
        orderInfo.setDetails(details);
 
        model.addAttribute("orderInfo", orderInfo);
 
        return "order";
    }
  
    @RequestMapping(value = { "/deleteorder" }, method = RequestMethod.GET)
    public String orderDelete(Model model, @RequestParam("orderId") String orderId) {
        OrderInfo orderInfo = null;
        if (orderId != null) {
        	try {	
        	String sql = "delete from " +  Order.class.getName() +  " where OrderID=" + String.valueOf(orderId);
        	Session session = this.sessionFactory.getCurrentSession();
        	Query query = session.createQuery(sql);
        	int result = query.executeUpdate();
        	System.out.println(result);
            orderInfo = this.orderDAO.getOrderInfo(orderId);}
        	catch(Exception e)
        	{
        		return "redirect:/orderList";
        	}
        }
            return "redirect:/orderList";
    }
    
    
}
