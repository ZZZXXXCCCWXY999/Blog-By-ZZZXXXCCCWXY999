package xyz.zxcwxy999.blog.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.zxcwxy999.blog.domain.Comment;
import xyz.zxcwxy999.blog.repository.CommentRepository;

import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    public void removeComment(Long id) {
        commentRepository.deleteById(id);
    }
}
