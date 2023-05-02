package team1.vo;

import java.io.Serializable;

// 관리자 클래스
public class Member implements Serializable {
	
	private int code; // 회원번호
	private String id; // 회원 아이디
	private String pw; // 회원 비밀번호
	private String level; // 회원 분류(100/010/001) (관리자, 출판사, 일반회원), (1: 활성화, 0:비활성화)
	private String name;
	
	// 사용자 카트 추가
	private Cart cart;
	
	public Member() {
	}

	public Member(int code, String id, String pw, String level, String name) {
		this.code = code;
		this.id = id;
		this.pw = pw;
		this.level = level;
		this.name = name;
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}
}
