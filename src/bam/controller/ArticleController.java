package bam.controller;

import java.util.List;
import java.util.Scanner;

import bam.container.Container;
import bam.dto.Article;
import bam.service.ArticleService;
import bam.util.Util;

public class ArticleController extends Controller {
	
	private String cmd;
	private Scanner sc;
	private ArticleService articleService;
	
	public ArticleController(Scanner sc) {
		this.articleService = Container.articleService;
		this.sc = sc;
	}
	
	public void doAction(String cmd, String methodName) {
		this.cmd = cmd;
		switch (methodName) {
		case "write" :
			doWrite();
			break;
		case "list" :
			showList();
			break;
		case "detail" :
			showDetail();
			break;
		case "modify" :
			doModify();
			break;
		case "delete" :
			doDelete();
			break;
		default :
			System.out.println("존재하지 않는 명령어입니다.");
			break;
		}
	}
	
	private void doWrite() {
		System.out.println("== 게시물 작성 ==");
		int id = articleService.setLastId();
		String regDate = Util.getDateStr();
		System.out.printf("제목 : ");
		String title = sc.nextLine();
		System.out.printf("내용 : ");
		String body = sc.nextLine();
		
		Article article = new Article(id, regDate, loginedMember.id, title, body);
		
		articleService.add(article);
		
		System.out.printf("%d번 글이 생성되었습니다.\n", id);
	}

	private void showList() {		
		
		String searchKeyword = cmd.substring("article list".length()).trim();
		
		List<Article> printArticle = articleService.getArticles(searchKeyword);
		
		if (printArticle.size() == 0) {	
			System.out.println("존재하는 게시물이 없습니다.");
			return;
		} 
		
		
		System.out.println("== 게시물 목록 ==");	
		System.out.println("	번호	|	제목	|	작성일	|	작성자	");
		
		for (int i = printArticle.size() - 1; i >= 0; i--) {
			Article article = printArticle.get(i);
			
			System.out.printf("%d	|	%s	|	%s	|	%s\n", article.id, article.title, article.regDate, article.memberId);
		}
	}
	
	private void showDetail() {

		String[] cmdBits = cmd.split(" ");
		int id = Integer.parseInt(cmdBits[2]);
		
		if (cmdBits.length == 2) {
			System.out.println("명령어를 확인해주세요");
			return;
		}
		
		Article foundArticle = articleService.getArticleById(id);
		
		if (foundArticle == null) {
			System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
			return;
		}
			
			System.out.println("== 게시물 상세보기 ==");
			System.out.printf("번호 : %d\n", foundArticle.id);
			System.out.printf("작성일 : %s\n", foundArticle.regDate);
			System.out.printf("작성자 : %s\n", foundArticle.memberId);
			System.out.printf("제목 : %d\n", foundArticle.title);
			System.out.printf("내용 : %d\n", foundArticle.body);
	}
	
	private void doModify() {
		String[] cmdBits = cmd.split(" ");
		int id = Integer.parseInt(cmdBits[2]);
		
		if (cmdBits.length == 2) {
			System.out.println("명령어를 확인해주세요");
			return;
		}
		
		Article foundArticle = articleService.getArticleById(id);
		
		if (foundArticle == null) {
			System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
			return;
		}
		
		if (foundArticle.memberId != loginedMember.id) {
			System.out.println("해당 게시물에 대한 권한이 없습니다.");
			return;
		}
		
		System.out.println("== 게시물 수정 ==");
		System.out.printf("수정할 제목 : ");
		String title = sc.nextLine();
		System.out.printf("수정할 내용 : ");
		String body = sc.nextLine();
		
		foundArticle.title = title;
		foundArticle.body = body;
		
		System.out.printf("%d번 게시물이 수정되었습니다.\n", id);
	}
	
	private void doDelete() {
		String[] cmdBits = cmd.split(" ");
		int id = Integer.parseInt(cmdBits[2]);
		
		if (cmdBits.length == 2) {
			System.out.println("명령어를 확인해주세요");
			return;
		}
		
		Article foundArticle = articleService.getArticleById(id);
		
		if (foundArticle == null) {
			System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
			return;
		}
		
		if (foundArticle.memberId != loginedMember.id) {
			System.out.println("해당 게시물에 대한 권한이 없습니다.");
			return;
		}
		
		articleService.remove(foundArticle);
			
		System.out.printf("%d번 게시물이 삭제되었습니다.\n", id);
	}
	
	public void makeTestData() {
		System.out.println("테스트용 게시물 데이터 5개 생성");
		articleService.makeTestData();
	}
}
