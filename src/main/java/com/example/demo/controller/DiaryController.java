	package com.example.demo.controller;
	
	
	import java.util.List;
	
	import org.springframework.stereotype.Controller;
	import org.springframework.ui.Model;
	import org.springframework.web.bind.annotation.GetMapping;
	import org.springframework.web.bind.annotation.ModelAttribute;
	import org.springframework.web.bind.annotation.PostMapping;
	
	import com.example.demo.domain.Diary;
	import com.example.demo.domain.Users;
	import com.example.demo.service.DiaryService;
	
	import jakarta.servlet.http.HttpSession;
	
	@Controller
	public class DiaryController {
	
	    private final DiaryService diaryService;
	
	    public DiaryController(DiaryService diaryService) {
	        this.diaryService = diaryService;
	    }
	
	    @GetMapping("/diary")
	    public String getDiariesByUser(HttpSession session, Model model) {
	        Users loggedInUser = (Users) session.getAttribute("loggedInUser");
	
	        if (loggedInUser != null) {
	            List<Diary> diaries = diaryService.getDiariesByUser(loggedInUser);
	            model.addAttribute("diaries", diaries);
	        } else {
	            return "redirect:/login";
	        }
	        return "diaryList";
	    }
	
	    @GetMapping("/diary/create")
	    public String createForm(Model model) {
	        model.addAttribute("diary", new Diary());
	        return "createDiary";
	    }
	
	    @PostMapping("/diary/create")
	    public String create(@ModelAttribute Diary diary, HttpSession session) {
	        Users loggedInUser = (Users) session.getAttribute("loggedInUser");
	        
	        if (loggedInUser != null) {
	            diary.setAuthor(loggedInUser);
	            diaryService.saveDiary(diary);
	            return "redirect:/diary";  // Redirect to the correct URL for diary listing
	        }
	        return "redirect:/login";  // Redirect to login if user is not logged in
	    }

	}
