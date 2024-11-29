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

    public void updateDiary(Diary diary) {
        Diary data = diaryRepository.findById(diary.getId()).orElseThrow(() -> new IllegalArgumentException("Diary not found with ID: " + diary.getId()));
        data.setTitle(diary.getTitle());
        data.setContent(diary.getContent());
        if (diary.getDate() != null) {
            data.setDate(diary.getDate());
        }
        if (diary.getFileData() != null) {
            data.setFileData(diary.getFileData());
            data.setFileName(diary.getFileName());
            data.setFileType(diary.getFileType());
        }
        diaryRepository.save(data);
    }

    public void deleteDiary(Diary diary) {
        diaryRepository.delete(diary);
    }

    public List<Diary> getDiariesByUser(Users user) {
        return diaryRepository.findByAuthor(user);
    }

    public Diary getDiaryById(Long id) {
        return diaryRepository.findById(id).orElseThrow(() -> new RuntimeException("Diary with id " + id + " not found!"));
    }

    public List<Diary> getAllDiaries() {
        return List.of();
    }
}

