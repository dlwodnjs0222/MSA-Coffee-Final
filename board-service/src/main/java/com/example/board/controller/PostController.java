package com.example.board.controller;

import com.example.board.entity.Post;
import com.example.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<Page<Post>> findAll(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(postService.findAll(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> findById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostAndIncrementViewCount(id));
    }

    @PostMapping
    public ResponseEntity<Post> save(@RequestBody Post post) {
        return ResponseEntity.ok(postService.save(post));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> update(@PathVariable Long id, @RequestBody Post post) {
        return ResponseEntity.ok(postService.update(id, post));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       // --- 수정된 부분 (비밀번호 받기) ---
                                       @RequestParam String password) {
        postService.delete(id, password);
        return ResponseEntity.ok().build();
    }

    // [신규 추가] 비밀번호 확인 API
    @PostMapping("/{id}/check-password")
    public ResponseEntity<Void> checkPassword(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String password = request.get("password");
        if (postService.checkPassword(id, password)) {
            return ResponseEntity.ok().build(); // 200 OK (비밀번호 맞음)
        } else {
            return ResponseEntity.status(403).build(); // 403 Forbidden (비밀번호 틀림)
        }
    }
} 