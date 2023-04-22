package bam.controller;

import java.util.Scanner;

import bam.dto.Member;
import bam.service.MemberService;
import bam.util.Util;

public class MemberController extends Controller {
	
	private Scanner sc;
	private MemberService memberService;
	
	public MemberController(Scanner sc) {
		memberService = new MemberService();
		this.sc = sc;
	}
	
	public void doAction(String cmd, String methodName) {
		switch (methodName) {
		case "join" :
			doJoin();
			break;
		case "login" :
			doLogin();
			break;
		case "logout" :
			doLogout();
			break;
		default :
			System.out.println("존재하지 않는 명령어입니다.");
			break;
		}
	}
	
	private void doJoin() {
		System.out.println("== 회원 가입 ==");
		int id = memberService.setMemberId();
		
		String loginId = null;
		
		while (true) {
			System.out.printf("로그인 아이디 : ");
			loginId = sc.nextLine();
			
			if(memberService.isLoginIdDup(loginId) == false) {
				System.out.printf("%s은(는) 이미 사용중인 아이디입니다.\n", loginId);
				continue;
			}
			
			System.out.printf("%s은(는) 사용가능한 아이디입니다.\n", loginId);
			break;
		}
			
		String loginPw = null;
		
		while (true) {
			System.out.printf("로그인 비밀번호 : ");
			loginPw = sc.nextLine();
			System.out.printf("로그인 비밀번호 확인 : ");
			String loginPwCk = sc.nextLine();
			
			if (loginPw.equals(loginPwCk) == false) {
				System.out.println("비밀번호를 확인해주세요.");
				continue;
			}
			break;
		}
		
		System.out.printf("이름 : ");
		String name = sc.nextLine();
		
		Member member = new Member(id, Util.getDateStr(), loginId, loginPw, name);
		
		memberService.add(member);
		
		System.out.printf("%s회원님이 가입되었습니다.\n", loginId);
	}
	
	private void doLogin() {
		
		if (loginedMember != null) {
			System.out.println("로그아웃 후 이용해주세요.");
			return;
		}
		
		while (true) {
			System.out.print("로그인 아이디 : ");
			String loginId = sc.nextLine();
			System.out.print("로그인 비밀번호 : ");
			String loginPw = sc.nextLine();
			
			Member member = memberService.getMemberByLoginId(loginId);
			
			if (member == null) {
				System.out.println("일치하는 회원이 없습니다.");
				return;
			}
			if (member.loginPw.equals(loginPw) == false) {
				System.out.println("비밀번호를 확인해주세요.");
				return;
			}
			
			loginedMember = member;
			
			System.out.printf("%s님 환영합니다.\n", member.name);
			break;
		}
	}
	
	private void doLogout() {
		
		if (loginedMember == null) {
			System.out.println("로그인 상태가 아닙니다.");
			return;
		}
		
		loginedMember = null;
		System.out.println("로그아웃 되었습니다.");
	}
	
	public void makeTestData() {
		System.out.println("테스트용 회원 데이터 3개 생성");
		memberService.makeTestData();
	}
	
	
}
