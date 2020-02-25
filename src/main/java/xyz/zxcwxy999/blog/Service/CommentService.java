package xyz.zxcwxy999.blog.Service;

import xyz.zxcwxy999.blog.domain.Comment;

import java.util.Optional;

public interface CommentService {

    /**
     * 根据id获取Comment
     * @param id
     * @return
     */
    Optional<Comment> getCommentById(Long id);

    /**
     * 删除评论
     * @param id
     */
    void removeComment(Long id);



}
