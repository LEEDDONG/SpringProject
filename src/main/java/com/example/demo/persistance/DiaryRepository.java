package com.example.demo.persistance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Diary;
import com.example.demo.domain.Users;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findByAuthor(Users author);
}
