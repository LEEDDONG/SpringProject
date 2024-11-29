package com.example.demo.controller;

import java.util.List;

import com.example.demo.persistance.DiaryRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.domain.Diary;
import com.example.demo.domain.Users;
import com.example.demo.service.DiaryService;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DiaryController {

	private final DiaryRepository diaryRepository;
	private final DiaryService diaryService;

	public DiaryController(DiaryService diaryService, DiaryRepository diaryRepository) {
		this.diaryService = diaryService;
		this.diaryRepository = diaryRepository;
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
			return "redirect:/diary"; // Redirect to the correct URL for diary listing
		}
		return "redirect:/login"; // Redirect to login if user is not logged in
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
	public String update(@ModelAttribute Diary diary,
			@RequestParam(value = "file", required = false) MultipartFile file, RedirectAttributes redirectAttributes) {
		if (diary.getId() == null) {
			// 여기서 예외를 던지거나 500 오류가 발생할 수 있습니다.
			throw new IllegalArgumentException("The diary ID must not be null");
		}
		try {
			if (file != null && !file.isEmpty()) {
				// 파일 데이터를 바이너리로 변환
				diary.setFileData(file.getBytes());
				diary.setFileName(file.getOriginalFilename());
				diary.setFileType(file.getContentType());
			}
			diaryService.updateDiary(diary);
			redirectAttributes.addFlashAttribute("message", "Diary updated successfully!");
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("message", "Failed to update diary.");
		}
		return "redirect:/diary";
	}

	@GetMapping("/diary/file/{id}")
	public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
		Diary diary = diaryRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Diary not found with ID: " + id));

		if (diary.getFileData() != null) {
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_TYPE, diary.getFileType());
			return new ResponseEntity<>(diary.getFileData(), headers, HttpStatus.OK);
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/diary/file/{id}/download")
	public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
		try {
			Diary diary = diaryService.getDiaryById(id);
			if (diary != null && diary.getFileData() != null) {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.parseMediaType(diary.getFileType()));
				headers.setContentDisposition(
						ContentDisposition.builder("attachment").filename(diary.getFileName()).build());
				return new ResponseEntity<>(diary.getFileData(), headers, HttpStatus.OK);
			} else {
				return ResponseEntity.notFound().build(); // 파일이 없을 경우
			}
		} catch (Exception e) {
			e.printStackTrace(); // 예외 로그 출력
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 서버 오류 처리
		}
	}

	@GetMapping("/diary/delete/{id}")
	public String deleteDiary(@PathVariable Long id) {
		diaryService.deleteDiaryById(id);
		return "redirect:/diary"; // 다이어리 목록 페이지로 리다이렉트
	}

}