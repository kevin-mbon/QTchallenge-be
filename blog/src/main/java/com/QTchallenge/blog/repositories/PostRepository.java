package com.QTchallenge.blog.repositories;

import com.QTchallenge.blog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}

