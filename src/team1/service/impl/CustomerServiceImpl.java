package team1.service.impl;

import static team1.utils.BookStoreUtils.nextInt;
import static team1.utils.BookStoreUtils.nextLine;
import static team1.utils.BookStoreUtils.nextLine2;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import team1.exception.IdException;
import team1.service.CustomerService;
import team1.vo.Customer;
import team1.vo.Member;

public class CustomerServiceImpl extends MemberServiceImpl implements CustomerService {
	
	@Override
	public int checkRange(int num) {
		return checkRange(num, 0, 100);
	}
	@Override
	public int checkRange(int num, int start, int end) {
		if (num < start || num > end) {
			throw new team1.exception.MyrangeException(start, end);
		}
		return num;
	}
	@Override
	public String chekId(String id) {
		if (id.length() < 3 ) {
			throw new IdException(id);
		}
		return id;
	}

	private static CustomerService customerService = new CustomerServiceImpl();
	public static CustomerService getInstance() {
		return customerService;
	}
	
	private CustomerServiceImpl() {}

	private List<Customer> customers = new ArrayList<Customer>();
//	private Customer loginUser = null;
	
	
	public Member getLoginUser() {
		return (Customer)loginUser;
	}
	

	/**
	 * @author 양찬용
	 * @since 23/02/09
	 * @param isMe
	 * @return 기능설명
	 * 
	 */

	public boolean isMe() {
		return loginUser.getPw().equals(nextLine2("비밀번호를 입력해주세요"));
	}

	/**
	 * @author 양찬용
	 * @since 23/02/08
	 * @param register
	 * @return 기능 설명 회원가입
	 * 23/02/13
	 * MemberServiceImpl에서 회원가입 할 수 있게 추가
	 */
	

	@Override
	public Customer register(int x) {
		int code = 0;
		for(int i=0; i<code; i++) {
			code += 1;
		}
		String id = chekId(nextLine("id를 입력해주세요")); // 3~10자리 문자열 예외처리
		if (findById(id) != null) {
			System.err.println("중복된 id입니다");
			return register(x);
		}
		String pw = nextLine("pw를 입력해주세요"); // 6~30자 이상의 문자열 예외처리
		String level = "001";
		String name = nextLine("이름을 입력해주세요");
		String email = nextLine("email을 입력해주세요"); // 영문, 한글로만 작업할수 있어야함
		String phone = nextLine("휴대폰 번호를 입력해주세요"); //-제외, 
		String address = nextLine("주소를 입력해주세요");
		Customer customer = new Customer(code, id, pw, level, name, email, phone, address);
		customers.add(customer);
		save();
		System.out.println("회원가입이 완료되었습니다.");
		//메인메뉴로 리턴
		return null;
	}

	/**
	 * @author 양찬용
	 * @since 23/02/10
	 * @param
	 * @return 기능 설명 영속화 작업
	 */

	{
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("customer.ser"))) {
			customers = (List<Customer>) ois.readObject();
		} catch (FileNotFoundException e) {
			customers.add(new Customer(1, "ycy", "1234", "001", "양찬용", "bnocc@naver.com", "010-1234-1234", "인천광역시"));
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		save();
	}

	/**
	 * @author 양찬용
	 * @since 23/02/08
	 * @param findById
	 * @return 기능 설명 id 조회하기
	 */

	@Override
	public Customer findById(String id) {
		Customer customer = null;
		for (int i = 0; i < customers.size(); i++) {
			if (customers.get(i).getId().equals(id)) {
				customer = customers.get(i);
				break;
			}

		}
		return customer;
	}

	/**
	 * @author 양찬용
	 * @since 23/02/08
	 * @param findByPw
	 * @return 기능 설명 
	 * pw 조회하기
	 * 
	 * 23/02/13
	 * 필요없는 기능 주석처리
	 */

//	@Override
//	public Customer find {
//		Customer customer = null;
//		for(int i=0; i<customers.size(); i++) {
//			if(customers.get(i).getPw().equals(pw)) {
//				customer = customers.get(i);
//				break;
//			}
//		}
//		return customer;
//	}
//	

	/**
	 * @author 양찬용
	 * @since 23/02/08
	 * @param list
	 * @return 기능 설명 회원 정보 조회하기
	 */
	void list() {
		Collection<Customer> c = customers;
		System.out.println("이름     email     휴대폰번호     주소");
		System.out.println("=======================================");
		for (Customer cc : c) {
			System.out.println(cc);
		}

	}

	@Override
	public Customer findByCode(int code) {
		Customer customer = null;
		for (int i = 0; i < customers.size(); i++) {
			if (customers.get(i).getCode() == code) {
				customer = customers.get(i);
				break;
			}
		}

		return null;
	}

	/**
	 * @author 양찬용
	 * @since 23/02/08
	 * @param modify
	 * @return 기능 설명 회원 정보 수정하기
	 */
	@Override
	public void modify() { // 수정하기
		if (!isMe()) {
			System.out.println("비밀번호가 다릅니다");
			return;
		}
		getLoginUser().setName(nextLine("변경할 이름을 입력하세요"));
		((Customer)getLoginUser()).setEmail(nextLine("변경할 e-mail을 입력하세요"));
		((Customer)getLoginUser()).setPhone(nextLine("변경할 휴대폰번호를 입력하세요"));
		((Customer)getLoginUser()).setAddress(nextLine("변경할 주소를 입력하세요"));
		System.out.println("변경이 완료되었습니다.");
	}
	

	/**
	 * @author 양찬용
	 * @since 23/02/08
	 * @param remove
	 * @return 기능 설명 회원 탈퇴
	 */
	@Override
	public void remove() {
		int x = 0;
		x = nextInt("1. 회원탈퇴 2. 메인메뉴로"); // 1, 2번만 가능하게
		if (x == 1) {
			if (isMe()) {
//				nextLine2("비밀번호를 입력하세요");
				customers.remove(loginUser);
				System.out.println("회원 탈퇴가 완료되었습니다");
			}
		} else if (x == 2) {
			System.out.println("메인메뉴로 이동합니다");
		}

	}

	/**
	 * @author 양찬용
	 * @since 23/02/09
	 * @param login
	 * @return 기능 설명
	 *  로그인
	 */

	@Override
	public Member login() {
		Customer loginUser = null;
		String id = nextLine2("ID : ");
		String pw = nextLine2("PW : ");
		Customer findMember = findById(id);
		if(findMember == null) {
			System.out.println("회원 정보를 찾을 수 없습니다");
		} else if (findMember.getPw().equals(pw)) {
			System.out.println("로그인 성공");
			loginUser = findMember;
		}
		return loginUser;
	}

	/**
	 * @author 양찬용
	 * @since 23/02/10
	 * @param
	 * @return 기능 설명 영속화 작업
	 */
	private void save() {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("customer.ser"))) {
			oos.writeObject(customers);

		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}
}