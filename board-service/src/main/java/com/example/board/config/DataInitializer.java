package com.example.board.config;

import com.example.board.entity.Comment;
import com.example.board.entity.Post;
import com.example.board.entity.PostType;
import com.example.board.repository.CommentRepository;
import com.example.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Random;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            Random random = new Random();
            PostType[] postTypes = PostType.values(); // PostType 배열

            for (int i = 1; i <= 50; i++) {
                Post post = new Post();
                post.setTitle("테스트 게시글 " + i);
                post.setContent("이것은 테스트 게시글 " + i + "의 내용입니다. ...");

                // --- 수정/추가된 설정 ---
                post.setCreatedAt(LocalDateTime.now().minusDays(random.nextInt(30)));
                post.setPostType(postTypes[random.nextInt(postTypes.length)]); // 랜덤 유형
                post.setViewCount(random.nextInt(100)); // 랜덤 조회수
                post.setAuthorNickname("테스터" + (i % 5)); // 닉네임
                post.setPassword("1234"); // 테스트용 비밀번호
                // --- ---

                postRepository.save(post);

                // 각 게시글에 댓글 추가
                for (int j = 1; j <= random.nextInt(5) + 1; j++) {
                    Comment comment = new Comment();
                    comment.setContent("테스트 댓글 " + j + "입니다.");
                    comment.setPost(post);

                    // --- 수정/추가된 설정 ---
                    comment.setCreatedAt(LocalDateTime.now().minusDays(random.nextInt(10)));
                    comment.setAuthorNickname("댓글러" + (j % 3)); // 댓글 닉네임
                    comment.setPassword("1234"); // 테스트용 비밀번호
                    // --- ---

                    commentRepository.save(comment);
                }
            }
        };
    }
} 