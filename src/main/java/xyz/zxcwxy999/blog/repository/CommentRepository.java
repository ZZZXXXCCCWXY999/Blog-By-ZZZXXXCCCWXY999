package xyz.zxcwxy999.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.zxcwxy999.blog.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment,Long> {
}
