CREATE TABLE `Accounts` (
  `UserId` varchar(20) NOT NULL,
  `Password` varchar(500) DEFAULT NULL,
  `User_Role` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Category` (
  `Code` varchar(30) NOT NULL,
  `Name` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`Code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Products` (
  `Code` varchar(20) NOT NULL,
  `Name` varchar(300) NOT NULL,
  `Price` double NOT NULL,
  `Category` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`Code`),
  KEY `FK_Category` (`Category`),
  CONSTRAINT `FK_Category` FOREIGN KEY (`Category`) REFERENCES `Category` (`Code`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Orders` (
  `OrderID` varchar(50) NOT NULL,
  `Amount` double NOT NULL,
  `Customer_Address` varchar(300) NOT NULL,
  `Customer_Name` varchar(255) NOT NULL,
  `Customer_Phone` varchar(150) NOT NULL,
  `Order_Date` datetime NOT NULL,
  `Order_Num` int(11) NOT NULL,
  `Customer_Email` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`OrderID`),
  UNIQUE KEY `UK_sxhpvsj665kmi4f7jdu9d2791` (`Order_Num`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ;


CREATE TABLE `Feedback` (
  `OrderID` varchar(30) NOT NULL,
  `Review` text,
  `Rating` float DEFAULT NULL,
  PRIMARY KEY (`OrderID`),
  CONSTRAINT `Feedback_ibfk_1` FOREIGN KEY (`OrderID`) REFERENCES `Orders` (`OrderID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Order_Details` (
  `ID` varchar(50) NOT NULL,
  `Amount` double DEFAULT NULL,
  `Price` double DEFAULT NULL,
  `Quanity` int(11) DEFAULT NULL,
  `ORDER_ID` varchar(50) DEFAULT NULL,
  `PRODUCT_ID` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `ORDER_DETAIL_ORD_FK` (`ORDER_ID`),
  KEY `ORDER_DETAIL_PROD_FK` (`PRODUCT_ID`),
  CONSTRAINT `ORDER_DETAIL_ORD_FK` FOREIGN KEY (`ORDER_ID`) REFERENCES `Orders` (`OrderID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ORDER_DETAIL_PROD_FK` FOREIGN KEY (`PRODUCT_ID`) REFERENCES `Products` (`Code`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

delimiter $$
create trigger role_restriction_before_insert before insert on 
	Accounts    
	for each row     
	begin       
	if  new.User_Role not in ('EMPLOYEE','MANAGER') 
	then         
	SIGNAL SQLSTATE '45000'            
	SET MESSAGE_TEXT = 'User role must be employee or manager only';       
	end if;        
	end;$$
delimiter ;

delimiter $$
create trigger role_restriction_before_update before update on 
	Accounts    
	for each row     
	begin       
	if  new.User_Role not in ('EMPLOYEE','MANAGER') 
	then         
	SIGNAL SQLSTATE '45000'            
	SET MESSAGE_TEXT = 'User role must be employee or manager only';       
	end if;        
	end;$$
delimiter ;

delimiter $$
create trigger rating_restriction_before_insert before insert on 
	Feedback    
	for each row     
	begin       
	if  new.rating < 0 or new.rating > 5 
	then         
	SIGNAL SQLSTATE '45000'            
	SET MESSAGE_TEXT = 'Rating must be between 0 to 5';       
	end if;        
	end;$$
delimiter ;

delimiter $$
create trigger rating_restriction_before_update before update on 
	Feedback   
	for each row     
	begin       
	if  new.rating < 0 or new.rating > 5 
	then         
	SIGNAL SQLSTATE '45000'            
	SET MESSAGE_TEXT = 'Rating must be between 0 to 5';       
	end if;        
	end;$$
delimiter ;

delimiter $$
create trigger order_details_updaterestricion before update on 
	Order_Details   
	for each row     
	begin       
	if  new.amount <> old.amount or new.price <> old.price or new.quanity <> old.quanity
	then         
	SIGNAL SQLSTATE '45000'            
	SET MESSAGE_TEXT = 'Amount,Price or Quanity cant be updated';       
	end if;        
	end;$$
delimiter ;

delimiter $$
create trigger order_details_deletecascade before delete on 
	Order_Details   
	for each row     
	begin       
	update Orders set amount = amount - old.amount where OrderID = old.Order_ID;
	end;$$
delimiter ;

delimiter $$
create trigger product_delete_orders_update before delete on 
	Products   
	for each row     
	begin       
	delete from Order_Details where PRODUCT_ID = old.Code;
	end;$$
delimiter ;

delimiter $$
create trigger email_validation before insert on 
	Orders 
	for each row
	begin
    if NOT (SELECT NEW.Customer_Email like '%_@__%.__%') 
    then
        SIGNAL SQLSTATE '45000'
		SET MESSAGE_TEXT = 'Email Address is not valid';       
    end if;
end$$
delimiter ;

delimiter $$
create trigger phone_validation before insert on 
	Orders 
	for each row
	begin
    if NOT (SELECT NEW.Customer_Phone REGEXP '^[6789][0-9]{9}$') 
    then
        SIGNAL SQLSTATE '45000'
		SET MESSAGE_TEXT = 'Phone should be of 10 digits starting with 6 7 8 or 9';       
    end if;
end$$
delimiter ;

delimiter $$
create trigger email_validation_on_update before update on 
	Orders 
	for each row
	begin
    if NOT (SELECT NEW.Customer_Email like '%_@__%.__%') 
    then
        SIGNAL SQLSTATE '45000'
		SET MESSAGE_TEXT = 'Email Address is not valid';       
    end if;
end$$
delimiter ;

delimiter $$
create trigger phone_validation_on_update before update on 
	Orders 
	for each row
	begin
    if NOT (SELECT NEW.Customer_Phone REGEXP '^[6789][0-9]{9}$') 
    then
        SIGNAL SQLSTATE '45000'
		SET MESSAGE_TEXT = 'Phone should be of 10 digits starting with 6 7 8 or 9';       
    end if;
end$$
delimiter ;

delimiter $$
create trigger price_restriction_before_insert before insert on 
	Products    
	for each row     
	begin       
	if  new.price < 0  
	then         
	SIGNAL SQLSTATE '45000'            
	SET MESSAGE_TEXT = 'Price must be positive';       
	end if;        
	end;$$
delimiter ;

delimiter $$
create trigger price_restriction_before_update before update on 
	Products   
	for each row     
	begin       
	if  new.price < 0  
	then         
	SIGNAL SQLSTATE '45000'            
	SET MESSAGE_TEXT = 'Price must be positive';       
	end if;        
	end;$$
delimiter ;

delimiter $$
create trigger ordernum_restriction_before_insert before insert on 
	Orders    
	for each row     
	begin       
	if  new.Order_Num < 0  
	then         
	SIGNAL SQLSTATE '45000'            
	SET MESSAGE_TEXT = 'Order Number must be positive';       
	end if;        
	end;$delimiter $$
create trigger ordernum_restriction_before_insert before insert on 
	Orders    
	for each row     
	begin       
	if  new.Order_Num < 0  
	then         
	SIGNAL SQLSTATE '45000'            
	SET MESSAGE_TEXT = 'Order Number must be positive';       
	end if;        
	end;$$
delimiter ;

delimiter $$
create trigger ordernum_restriction_before_update before update on 
	Orders   
	for each row     
	begin       
	if  new.Order_Num < 0  
	then         
	SIGNAL SQLSTATE '45000'            
	SET MESSAGE_TEXT = 'Order Number must be positive';       
	end if;        
	end;$$
delimiter ;
$
delimiter ;

delimiter $$
create trigger ordernum_restriction_before_update before update on 
	Orders   
	for each row     
	begin       
	if  new.Order_Num < 0  
	then         
	SIGNAL SQLSTATE '45000'            
	SET MESSAGE_TEXT = 'Order Number must be positive';       
	end if;        
	end;$$
delimiter ;
