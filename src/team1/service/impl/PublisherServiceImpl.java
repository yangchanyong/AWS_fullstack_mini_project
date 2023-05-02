package team1.service.impl;

import static team1.utils.BookStoreUtils.nextLine;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import static team1.utils.BookStoreUtils.*;

import team1.exception.IdException;
import team1.service.PublisherService;
import team1.vo.Member;
import team1.vo.Publisher;

public class PublisherServiceImpl extends MemberServiceImpl implements PublisherService {

	/**
	 * @author 양찬용
	 * @since 23/02/10
	 * @param
	 * @return 기능 설명 싱글톤작업
	 */

	private static PublisherService publisherService = new PublisherServiceImpl();
	public static PublisherService getInstance() {
		return publisherService;
	}

	private PublisherServiceImpl() {}

	private List<Publisher> publishers = new ArrayList<Publisher>();
//	private Publisher loginUser = null;

	public boolean isMe() {
		return loginUser.getPw().equals("입력받을 비밀번호");
	}

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
	 * @since 23/02/11
	 * @param register
	 * @return 기능 설명 출판사 회원 가입
	 * 
	 *         23/02/13 code 추가
	 */
	public Member getLoginUser() {
		return (Publisher)loginUser;
	}

	@Override
	public Member register(int x) {
		int code = 0;
		for(int i=0; i<code; i++) {
			code += 1;
		}
		String id = checkId(nextLine("id를 입력해주세요")); // 3~10자리의 id입력
		if (findById(id) != null) {
			System.err.println("중복된 id입니다");
			return register(x);
		}
		String pw = nextLine("pw를 입력해주세요"); // 6자이상의 pw입력
		String name = nextLine("이름을 입력해주세요"); // 영문, 한글로만 입력 가능하게
		int brn = checkRange(nextInt2("사업자등록번호 13자리를 입력해주세요 (-제외)"), 13, 13); //13자리 제한
		String level = "001"; 
		Publisher publisher = new Publisher(code, id, pw, level, name, brn);
		publishers.add(publisher);
		save();
		System.out.println("승인 후 회원가입이 완료됩니다"); // ?? 확인해야함
		
		// 메인메뉴로 리턴
		return null;
	}

	private String checkId(String id) {
		if (id.length() < 3 ) {
			throw new IdException(id);
		}
		return id;
	}

	/**
	 * @author 양찬용
	 * @since 23/02/11
	 * @param
	 * @return 기능 설명 Publisher 더미계정 생성 및 영속화 작업
	 * 
	 * 23/02/13
	 * 더미데이터 코드번호 추가
	 */

	{
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("publisher.ser"))) {
			publishers = (List<Publisher>) ois.readObject();
		} catch (FileNotFoundException e) {
			publishers.add(new Publisher(2, "wisdom", "1234", "010", "위즈덤하우스", 1112233333));
			publishers.add(new Publisher(3, "munhak", "1234", "010", "문학동네", 1112244444));
			publishers.add(new Publisher(4, "dsstore", "1234", "010", "동아사이언스", 1112255555));
			publishers.add(new Publisher(5, "factory9", "1234", "010", "팩토리나인", 1112266666));
			publishers.add(new Publisher(6, "changbi", "1234", "010", "창비", 1112277777));
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		save();
	}

	/**
	 * @author 양찬용
	 * @since 23/02/11
	 * @param modify
	 * @return 기능 설명 Publisher 정보 수정
	 */

	@Override
	public void modify() {
		if (!equals(isMe())) {
			System.out.println("비밀번호가 다릅니다");
			return;
		}
		loginUser.setName(nextLine("변경할 이름을 입력하세요"));
		System.out.println("변경이 완료되었습니다.");

	}

	/**
	 * @author 양찬용
	 * @since 23/02/11
	 * @param remove
	 * @return 기능 설명 Publisher 회원 탈퇴
	 */
	@Override
	public void remove() {
		int x = 0;
		x = nextInt("1. 회원탈퇴 2. 메인메뉴로");
		if (x == 1) {
			if (equals(loginUser.getPw())) {
				nextLine2("비밀번호를 입력하세요");
				publishers.remove(loginUser);
				System.out.println("회원 탈퇴가 완료되었습니다");
			}
			else if (x == 2) {
				System.out.println("메인메뉴로 이동합니다");
			}
		}

	}

	/**
	 * @author 양찬용
	 * @since 23/02/11
	 * @param login
	 * @return 기능 설명 Publisher 로그인
	 * 
	 * 23/02/13
	 * 로그인기능 수정
	 */

	@Override
	public Member login() {

		// 로그인이 성공시(아이디와 암호가 일치할 경우 일치하는 회원주소값을 반환
		// 로그인이 실패시에는 null을 반환
		Publisher loginUser = null;
		String id = nextLine2("ID : ");
		String pw = nextLine2("PW : ");
		// 회원정보에서 아이디와 일치하는 회원조회
		Publisher findMember = findById(id);
		
		// 조회한 아이디 & 비밀번호가 일치하는지 여부 확인
		// 일치하면 리턴값을 조회한 멤버 주소값으로 변경
		if (findMember == null) {
			System.out.println("회원 정보를 찾을 수 없습니다");
		} else if (findMember.getPw().equals(pw)) {
			System.out.println("로그인 성공");
			loginUser = findMember;
		}
		return loginUser;

	}

	@Override
	public Publisher findById(String id) {
		Publisher publisher = null;
		for (int i = 0; i < publishers.size(); i++) {
			if (publishers.get(i).getId().equals(id)) {
				publisher = publishers.get(i);
				break;
			}
			
		}
		return publisher;
	}

	/**
	 * @author 양찬용
	 * @since 23/02/11
	 * @param findByPw
	 * @return 기능설명
	 * pw 조회
	 * 
	 * 23/02/13
	 * 필요없는 기능 삭제
	 */
	
	
//	@Override
//	public Publisher findByPw(String pw) {
//		Publisher publisher = null;
//		for (int i = 0; i < publishers.size(); i++) {
//			if (publishers.get(i).getPw().equals(publisher)) {
//				publisher = publishers.get(i);
//				break;
//			}
//
//		}
//
//		return publisher;
//	}
	/**
	 * @author 양찬용
	 * @since 23/02/11
	 * @param
	 * @return 기능 설명 영속화 작업
	 */

	void save() {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("publisher.ser"))) {
			oos.writeObject(publishers);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


//	@Override
//	public Publisher getloginUser() {
//		return loginUser;
//	}



}
