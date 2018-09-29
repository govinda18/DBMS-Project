package dbms.shoppingcart.entity;

import java.io.Serializable;
 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
 
@Entity
@Table(name = "Category")
public class Category implements Serializable {
 
    private static final long serialVersionUID = -1000119078147252957L;
 
    private String code;
    private String name;
 
 
    public Category() {
    }

    @Id
    @Column(name="Code",length=20,nullable=false)
	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}

	@Column(name="Name",length=300,nullable=false)
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}



    
}