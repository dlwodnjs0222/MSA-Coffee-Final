package com.example.board.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING) // Enum 타입을 문자열로 저장
    @Column(nullable = false)
    private PostType postType; // (유형)

    @Column(columnDefinition = "integer default 0")
    private int viewCount = 0; // (조회수)

    @Column(nullable = false)
    private String authorNickname; // (작성자 닉네임)

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // 응답(JSON)에 비밀번호가 포함되지 않도록 설정
    @Column(nullable = false)
    private String password; // (수정/삭제용 비밀번호)

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
} 