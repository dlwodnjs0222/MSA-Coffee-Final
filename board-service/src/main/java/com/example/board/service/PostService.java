package com.example.board.service;

import com.example.board.entity.Post;
import com.example.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;

    public Page<Post> findAll(int page) {
        return postRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
    }


    @Transactional
    public Post getPostAndIncrementViewCount(Long id) {
        postRepository.incrementViewCount(id); // 조회수 1 증가
        return findById(id); // 게시글 조회
    }


    @Transactional
    public Post save(Post post) {
        return postRepository.save(post);
    }



    @Transactional
    public Post update(Long id, Post post) {
        Post existingPost = findById(id);
        // --- 비밀번호 검증 ---
        if (post.getPassword() == null || !existingPost.getPassword().equals(post.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        existingPost.setTitle(post.getTitle());
        existingPost.setContent(post.getContent());
        existingPost.setPostType(post.getPostType()); // 유형 업데이트
        existingPost.setAuthorNickname(post.getAuthorNickname()); // 닉네임도 수정 허용 시
        return postRepository.save(existingPost);
    }

    @Transactional
    public void delete(Long id, String password) {
        // --- 비밀번호 검증 ---
        Post post = findById(id);
        if (password == null || !post.getPassword().equals(password)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        postRepository.deleteById(id);
    }

    // [신규 추가] 비밀번호 검증 메서드
    public boolean checkPassword(Long id, String password) {
        Post post = findById(id); // 존재하지 않으면 예외 발생
        return post.getPassword() != null && post.getPassword().equals(password);
    }
} 