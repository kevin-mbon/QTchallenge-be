package com.QTchallenge.blog.services;

import com.QTchallenge.blog.dto.CommentDTO;
import com.QTchallenge.blog.dto.UserDTO;
import com.QTchallenge.blog.model.Comment;
import com.QTchallenge.blog.model.Post;
import com.QTchallenge.blog.model.Users;
import com.QTchallenge.blog.repositories.CommentRepository;
import com.QTchallenge.blog.repositories.PostRepository;
import com.QTchallenge.blog.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public List<CommentDTO> findAll() {
        return commentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<CommentDTO> findById(UUID id) {
        return commentRepository.findById(id)
                .map(this::convertToDTO);
    }

    public CommentDTO save(Comment comment, Long postId, String username) {
        Optional<Post> postOpt = postRepository.findById(postId);
        Optional<Users> userOpt = userRepository.findByUsername(username);

        if (postOpt.isPresent() && userOpt.isPresent()) {
            Post post = postOpt.get();
            Users user = userOpt.get();

            comment.setPost(post);
            comment.setAuthor(user);
            comment.setCreatedAt(LocalDateTime.now());
            comment.setUpdatedAt(LocalDateTime.now());

            Comment savedComment = commentRepository.save(comment);
            return convertToDTO(savedComment);
        } else {
            throw new RuntimeException("Post or User not found");
        }
    }

    public void deleteById(UUID id) {
        commentRepository.deleteById(id);
    }

    public CommentDTO convertToDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setPostId(comment.getPost().getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setCreatedAt(comment.getCreatedAt());
        commentDTO.setUpdatedAt(comment.getUpdatedAt());

        if (comment.getAuthor() != null) {
            UserDTO authorDTO = new UserDTO();
            authorDTO.setId(comment.getAuthor().getId());
            authorDTO.setUsername(comment.getAuthor().getUsername());
            commentDTO.setAuthor(authorDTO);
        }

        return commentDTO;
    }

    public Comment convertToEntity(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setId(commentDTO.getId());
        comment.setContent(commentDTO.getContent());
        comment.setCreatedAt(commentDTO.getCreatedAt());
        comment.setUpdatedAt(commentDTO.getUpdatedAt());

        return comment;
    }
}
