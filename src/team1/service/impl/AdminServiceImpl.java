package team1.service.impl;

import static team1.utils.BookStoreUtils.nextLine2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import team1.service.AdminService;
import team1.vo.Admin;
import team1.vo.Member;


public class AdminServiceImpl extends MemberServiceImpl implements AdminService {
	
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
	public Member getLoginUser() {
		return (Admin)loginUser;
	}
	/**
	 * @author 양찬용
	 * @since 23/02/14
	 * @param getLoginUser
	 * @return 기능 설명 
	 * 로그인
	 */

	private static AdminService adminService = new AdminServiceImpl();
	public static AdminService getInstance() {
		return adminService;
	}

	private AdminServiceImpl() {}
	
	private List<Admin> admins = new ArrayList<Admin>();
//	private Admin loginUser = null;
	
	
	/**
	 * @author 양찬용
	 * @since 23/02/13
	 * @param login
	 * @return 기능 설명 
	 * 로그인
	 */

	@Override
	public Member login() {
		Admin loginUser = null;
		String id = nextLine2("ID : ");
		String pw = nextLine2("PW : ");
		Admin findMember = findById(id);
		if(findMember == null) {
			System.out.println("회원정보를 찾을 수 없습니다");
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
	
	{
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("admins.ser"))) {
			admins = (List<Admin>) ois.readObject();
		} catch (FileNotFoundException e) {
			admins.add(new Admin(0, "admin", "1234", "100", "관리자"));
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		save();
	}
	

	private void save() {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("admins.ser"))) {
			oos.writeObject(admins);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	/**
	 * @author 양찬용
	 * @since 23/02/13
	 * @param findById
	 * @return 기능 설명 id 조회하기
	 */
	@Override
	public Admin findById(String id) {
		Admin admin = null;
		for (int i = 0; i < admins.size(); i++) {
			if (admins.get(i).getId().equals(id)) {
				admin = admins.get(i);
				break;
			}

		}
		return admin;
	}


	@Override
	public Member register(int x) {
		return null;
	}

}
