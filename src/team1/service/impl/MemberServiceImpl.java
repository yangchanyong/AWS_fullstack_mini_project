package team1.service.impl;

import static team1.utils.BookStoreUtils.nextInt;
import team1.service.AdminService;
import team1.service.CustomerService;
import team1.service.MemberService;
import team1.service.PublisherService;
import team1.vo.Member;

public class MemberServiceImpl implements MemberService {
	private static MemberService MemberService = new MemberServiceImpl();

	public static MemberService getInstance() {
		return MemberService;
	}
	protected Member loginUser = null;
	protected MemberServiceImpl() {}

	private CustomerService customerService = CustomerServiceImpl.getInstance();
	private PublisherService publisherService = PublisherServiceImpl.getInstance();
	private AdminService adminService = AdminServiceImpl.getInstance();

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
	
	/**
	 * @author 양찬용
	 * @since 23/02/13
	 * @param Member register
	 * @return 기능 설명 통합 회원가입 기능
	 */
	@Override
	public Member register(int x) {
		x = 0;
		switch (checkRange(nextInt("1. 일반사용자 회원가입 2. 출판사 회원가입"), 1, 2)) { // 1~2번만 가능하게
		case 1:
			customerService.register(1);
			break;
		case 2:
			publisherService.register(2);
			break;
		}
		return null;
	}

	/**
	 * @author 양찬용
	 * @since 23/02/13
	 * @param Member login
	 * @return 기능 설명 통합 로그인 기능
	 */

	@Override
	public Member login() {
		int input = 0;

		switch (input = checkRange(nextInt("1. 회원 로그인 2. 출판사 로그인 3. 관리자 로그인"), 1, 3)) { // 1~3 번만 가능하게
		case 1:
			loginUser = customerService.login();
			// customer 메인메뉴 메서드 입력필요
			break;
		case 2:
			loginUser = publisherService.login();
			// customer 메인메뉴 메서드 입력필요
			break;
		case 3:
			loginUser = adminService.login();
			// admin 메인메뉴 메서드 입력필요
			break;
		}
		return loginUser;
	}

	@Override
	public Member getLoginUser() {
		return loginUser;
	}
	@Override
	public void logout() {
		loginUser = null;
	}

}