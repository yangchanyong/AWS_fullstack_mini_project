package team1.vo;

import java.io.Serializable;

// 일반회원 클래스
/**
 * 
*/
public class Customer extends Member implements Serializable {
	
	
	/**
	 * 
	 */
	
	private String email;
	private String phone;
	private String address;

	private Cart Cart = new Cart();
	
	public Customer() {
		super();
	}

	public Customer(int code,String id, String pw, String level, String name, String email, String phone, String address) {
		super(code, id, pw, level, name);
		this.email = email;
		this.phone = phone;
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public team1.vo.Cart getCart() {
		return Cart;
	}

	public void setCart(team1.vo.Cart cart) {
		Cart = cart;
	}

	@Override
	public String toString() {
		return  super.getName() + email +  phone  + address;
	}
	
		
	

}
