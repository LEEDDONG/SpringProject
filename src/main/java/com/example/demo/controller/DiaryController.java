	package com.example.demo.controller;
	
	
	import java.io.IOException;
	import java.util.List;
	
	import org.springframework.stereotype.Controller;
	import org.springframework.ui.Model;
	import org.springframework.web.bind.annotation.GetMapping;
	import org.springframework.web.bind.annotation.ModelAttribute;
	import org.springframework.web.bind.annotation.PathVariable;
	import org.springframework.web.bind.annotation.PostMapping;
	
	import com.example.demo.domain.Diary;
	import com.example.demo.domain.Users;
	import com.example.demo.service.DiaryService;
	
	import jakarta.servlet.http.HttpSession;
	import org.springframework.web.multipart.MultipartFile;

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

		@GetMapping("/diary/{id}")
		public String getDiaryById(@PathVariable Long id, Model model) {
			Diary diary = diaryService.getDiaryById(id);
			if (diary == null) {
				throw new RuntimeException("Diary with id " + id + " not found!");
			}
			model.addAttribute("diary", diary); // 조회된 다이어리 객체를 모델에 추가
			return "updateDiary"; // 수정 페이지로 이동
		}

		@PostMapping("/diary/update")
		public String update(@ModelAttribute Diary diary, MultipartFile file, HttpSession session) {
			Users loggedInUser = (Users) session.getAttribute("loggedInUser");

			if (loggedInUser != null) {
				diary.setAuthor(loggedInUser);
				diaryService.update(diary);
				return "redirect:/diary";
			}
			return "redirect:/login";
		}

	}
