package com.example.demo.domain;

import java.time.LocalDate;
import java.util.Date;

import jakarta.persistence.*;

@Entity
public class Diary {
	
	@Id@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long id;
	
	private String title;
	private String content;
	private LocalDate date;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private Users author;

	@Lob
	private byte[] fileData;

	private String fileName;
	private String fileType;
	
	public Users getAuthor() {
		return author;
	}

	public void setAuthor(Users author) {
		this.author = author;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

    public byte[] getFileData() {
        return fileData;
    }
    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getFileType() {
        return fileType;
    }
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
