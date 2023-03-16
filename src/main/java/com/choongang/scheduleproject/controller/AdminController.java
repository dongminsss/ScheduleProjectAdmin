package com.choongang.scheduleproject.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.choongang.scheduleproject.command.AdminLoginVO;
import com.choongang.scheduleproject.service.AdminService;
import com.choongang.scheduleproject.util.Criteria;
import com.choongang.scheduleproject.util.PageVO;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	AdminService adminService;
	
	//회원목록 페이지
	@GetMapping("/manageMember")
	public String manageMember(Model model , Criteria criteria) { //단순 목록은 타임리프 처리
		int total = adminService.getMemberCount(criteria);//토탈 검색 (search에 따른 검색결과 건수 변화를 위해 criteria를 매개변수로 사용)
		model.addAttribute("count", total); //검색 결과 건수
		model.addAttribute("UserList", adminService.getMemberList(criteria)); //페이지에 넘길 데이터를 모델에 담는다.
		PageVO pageVO = new PageVO(criteria,total ); //pageVO 객체에서 사용할 criteria 와 total 값 주입
		model.addAttribute("pageVO", pageVO); //넘겨줄 VO 데이터
		return "/admin/adminManageMember";
	}
	//프로젝트 관리 페이지
	@GetMapping("/manageProject")
	public String manageProject(Model model , Criteria criteria) {
		int total = adminService.getProjectCount(criteria); //프로젝트 총 갯수
		PageVO pageVO = new PageVO(criteria,total ); //페이징
		model.addAttribute("projectList", adminService.getProjectList(criteria));//화면에 보여질 프로젝트리스트 
		model.addAttribute("pageVO", pageVO);
		return "/admin/adminManageProject";
	}
	//통계 목록 페이지
	@GetMapping("/manageStatistics")
	public String manageStatistics(Model model, Criteria criteria) {
		int total = adminService.getProjectCount(criteria); //프로젝트 총 갯수
		PageVO pageVO = new PageVO(criteria,total ); //페이징
		model.addAttribute("projectStaticVO" , adminService.getProjectStatic(criteria)); //프로젝트 통계 목록 담아줌
		model.addAttribute("pageVO", pageVO); //페이징 
		
		return "/admin/adminManageStatistics";
	}
	//어드민 로그인 요청
	@PostMapping("/login")
	public String login(@Valid AdminLoginVO vo , Errors errors, HttpSession session, RedirectAttributes ra) {
		//서버단에서 유효성 검사 실행
		if(errors.hasErrors()) {
			System.out.println(errors.toString());
			String msg = "로그인 시도 중 서버에서 문제가 발생했습니다.";
			ra.addFlashAttribute("msg", msg);
			return "redirect:/";
		}
		
		AdminLoginVO adminLoginVO = adminService.getLoginVO(vo); //로그인한 정보 담음
		
		if(adminLoginVO == null || !adminLoginVO.getAdmin_pw().equals(vo.getAdmin_pw())) { //아이디 또는 비밀번호가 맞지 않을 때 
			ra.addFlashAttribute("msg","아이디 또는 비밀번호가 일치하지 않습니다.");
			return "redirect:/";
		}
		
		session.setAttribute("admin_id", adminLoginVO.getAdmin_id()); //session에 id 담아줌
		
		
		return "redirect:/admin/manageMember";
	}
	@GetMapping("/noticeContent")
	public String noticeContent() {
		return "/admin/adminNoticeContent";
	}
	@GetMapping("/noticeModify")
	public String noticeModify() {
		return "/admin/adminNoticeModify";
	}
	@GetMapping("/noticeRegist")
	public String noticeRegist() {
		return "/admin/adminNoticeRegist";
	}
	@GetMapping("/noticeTableList")
	public String noticeTableList() {
		return "/admin/adminNoticeTableList";
	}
	
	
	
}
