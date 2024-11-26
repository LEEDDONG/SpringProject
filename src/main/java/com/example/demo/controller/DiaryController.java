	package com.example.demo.controller;
	
	
	import java.io.IOException;
	import java.nio.file.Files;
	import java.nio.file.Path;
	import java.nio.file.Paths;
	import java.nio.file.StandardCopyOption;
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
			model.addAttribute("diary", diary);
			return "updateDiary";
		}

		@PostMapping("/diary/update")
		public String update(@ModelAttribute Diary diary, MultipartFile file) {
			if (diary.getId() == null) {
				// 여기서 예외를 던지거나 500 오류가 발생할 수 있습니다.
				throw new IllegalArgumentException("The diary ID must not be null");
			}
			if (file != null && !file.isEmpty()) {
				try {
					// 파일 저장 경로 설정
					String fileName = file.getOriginalFilename();
					Path filePath = Paths.get("upload-dir", fileName);
					Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

					// 파일 경로를 diary 객체에 설정
					diary.setFilePath(filePath.toString()); // Diary 객체에 파일 경로 저장

				} catch (IOException e) {
					e.printStackTrace();
					return "redirect:/diary?error";
				}
			}
			diaryService.updateDiary(diary);
			return "redirect:/diary";
		}
	}