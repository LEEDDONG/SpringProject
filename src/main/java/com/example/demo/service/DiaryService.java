package com.example.demo.service;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;
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

    public void updateDiary(Diary diary) {
        Diary old = diaryRepository.findById(diary.getId()).get();
        old.setTitle(diary.getTitle());
        old.setContent(diary.getContent());
        old.setFilePath(diary.getFilePath());
        diaryRepository.save(old);
    }

    public List<Diary> getDiariesByUser(Users user) {
        return diaryRepository.findByAuthor(user);
    }

    public Diary getDiaryById(Long id) {
        return diaryRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Diary with id " + id + " not found!"));
    }
}

