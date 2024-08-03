package com.QTchallenge.blog.controller;

import com.QTchallenge.blog.dto.CommentDTO;
import com.QTchallenge.blog.model.Comment;
import com.QTchallenge.blog.services.CommentService;
import com.QTchallenge.blog.utily.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentService commentService;

    @Autowired
    private JwtTokenUtil jwtUtil;

    @GetMapping
    public List<CommentDTO> getAllComments() {
        return commentService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable UUID id) {
        return commentService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<CommentDTO> createComment(@RequestBody Comment comment, @RequestParam Long postId, HttpServletRequest request) {
        try {
            if (comment == null || request.getHeader("Authorization") == null) {
                logger.error("Comment or Authorization header is null");
                return ResponseEntity.badRequest().build();
            }

            String token = request.getHeader("Authorization").substring(7);
            String username = jwtUtil.getUsernameFromToken(token);
            CommentDTO savedComment = commentService.save(comment, postId, username);
            return ResponseEntity.ok(savedComment);
        } catch (Exception e) {
            logger.error("Error creating comment", e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable UUID id, @RequestBody CommentDTO commentDetails, @RequestParam Long postId, HttpServletRequest request) {
        try {
            if (commentDetails == null || request.getHeader("Authorization") == null) {
                logger.error("Comment details or Authorization header is null");
                return ResponseEntity.badRequest().build();
            }

            String token = request.getHeader("Authorization").substring(7);
            String username = jwtUtil.getUsernameFromToken(token);
            return commentService.findById(id)
                    .map(existingCommentDTO -> {
                        Comment existingComment = commentService.convertToEntity(existingCommentDTO);
                        existingComment.setContent(commentDetails.getContent());
                        existingComment.setUpdatedAt(LocalDateTime.now());
                        CommentDTO updatedComment = commentService.save(existingComment, postId, username);
                        return ResponseEntity.ok(updatedComment);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Error updating comment", e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable UUID id, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization").substring(7);
            String username = jwtUtil.getUsernameFromToken(token);
            return commentService.findById(id)
                    .map(comment -> {
                        commentService.deleteById(id);
                        return ResponseEntity.noContent().build();
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Error deleting comment", e);
            return ResponseEntity.badRequest().body(null);
        }
    }
}