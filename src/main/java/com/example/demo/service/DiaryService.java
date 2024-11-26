package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.domain.Diary;
import com.example.demo.domain.Users;
import com.example.demo.persistance.DiaryRepository;
@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;

    public DiaryService(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }

    public void saveDiary(Diary diary) {
        diaryRepository.save(diary);
    }

    public List<Diary> getDiariesByUser(Users user) {
        return diaryRepository.findByAuthor(user);
    }
}

