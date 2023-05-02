package team1.vo;

import java.io.Serializable;

// 관리자 클래스
public class Admin extends Member implements Serializable {
	public Admin() {
		super();
	}

	public Admin(int code, String id, String pw, String level, String name) {
		super(code, id, pw, level, name);
	}
	
}
