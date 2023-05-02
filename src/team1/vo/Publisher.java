package team1.vo;

import java.io.Serializable;

// 출판사 클래스
public class Publisher extends Member implements Serializable {
	// 사업자등록번호 10자리 "-" 제외
	private int brn;
	
	
	public int getBrn() {
		return brn;
	}

	public void setBrn(int brn) {
		this.brn = brn;
	}

	public Publisher() {
		super();
	}

	public Publisher(int code,String id, String pw, String level, String name, int brn) {
		super(code, id, pw, level, name);
		this.brn = brn;
	}
	@Override
	public String toString() {
		return "Publisher [brn=" + brn + "]";
	}
	
}
