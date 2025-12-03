package com.example.board.service;

import com.example.board.dto.CommentDto;
import com.example.board.entity.Comment;
import com.example.board.entity.Post;
import com.example.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService; // PostRepository 대신 PostService 사용

    public List<CommentDto> findByPostId(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtDesc(postId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
    }

    @Transactional
    public CommentDto save(Long postId, Comment comment) {
        Post post = postService.findById(postId);
        comment.setPost(post);
        // (password, authorNickname 등은 comment 객체에 이미 담겨있음)
        return convertToDto(commentRepository.save(comment));
    }

    @Transactional
    public CommentDto update(Long id, Comment comment) {
        Comment existingComment = findById(id);

        // --- 비밀번호 검증 ---
        if (comment.getPassword() == null || !existingComment.getPassword().equals(comment.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        // --- ---

        existingComment.setContent(comment.getContent());
        existingComment.setAuthorNickname(comment.getAuthorNickname()); // 닉네임 수정 허용 시

        return convertToDto(commentRepository.save(existingComment));
    }

    @Transactional
    public void delete(Long id, String password) {
        // --- 비밀번호 검증 ---
        Comment comment = findById(id);
        if (password == null || !comment.getPassword().equals(password)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        // --- ---

        commentRepository.deleteById(id);
    }

    private CommentDto convertToDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());
        dto.setAuthorNickname(comment.getAuthorNickname()); // 닉네임 설정
        return dto;
    }
}