package com.example.demo.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.domain.Users;
import com.example.demo.persistance.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

	private final UserRepository repo;
	
	public LoginController(UserRepository userRepository) {
	        this.repo = userRepository;
	    }
	  // 로그인 폼 보여주기
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }
    @PostMapping("/login")
    public String login(
        @RequestParam(defaultValue = "") String userName,
        @RequestParam(defaultValue = "") String password,
        HttpSession session) {
        if (userName.isEmpty() || password.isEmpty()) {
            return "redirect:/login?error=missingFields";
        }
        Optional<Users> userOptional = repo.findByUserName(userName);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            if (user.getPassword().equals(password)) {
                session.setAttribute("loggedInUser", user);
                return "redirect:/diary";
            }
        }
        return "redirect:/login?error=invalidCredentials";
    }
    @GetMapping("/signup")
    public String signupForm() {
        return "signup"; // Corresponds to signup.html in templates
    }


    
    @PostMapping("/signup")
    public String signup(@RequestParam String userName, @RequestParam String password) {
        // 중복 사용자 체크
        if (repo.findByUserName(userName).isPresent()) {
            return "redirect:/signup?error=exists";
        }

        // 새로운 사용자 생성 및 저장
        Users newUser = new Users();
        newUser.setUserName(userName);
        newUser.setPassword(password);
        repo.save(newUser);

        return "redirect:/login"; // 회원가입 후 로그인 페이지로 이동
    }


    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
	 
}
