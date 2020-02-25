package xyz.zxcwxy999.blog.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.zxcwxy999.blog.domain.Blog;
import xyz.zxcwxy999.blog.domain.User;

import java.util.Optional;

public interface BlogService {

    /**
     * 保存Blog
     * @param blog
     * @return
     */
    Blog saveBlog(Blog blog);

    /**
     * 删除Blog
     * @param id
     */
    void removeBlog(Long id);

    /**
     * 根据id过去Blog
     * @param id
     * @return
     */
    Optional<Blog> getBlogById(Long id);

    /**
     * 根据用户进行博客名称分页模糊查询（最新）
     * @param user
     * @param title
     * @param pageable
     * @return
     */
    Page<Blog> listBlogByTitleVote(User user, String title, Pageable pageable);

    /**
     * 根据用户进行博客名称分页模糊查询（最热）
     * @param user
     * @param title
     * @param pageable
     * @return
     */
    Page<Blog> listBlogByTitleVoteSort(User user, String title, Pageable pageable);

    /**
     * 阅读量递增
     * @param id
     */
    void readingIncrease(Long id);

    /**
     * 发表评论
     * @param blogId
     * @param commentContent
     * @return
     */
    Blog createComment(Long blogId,String commentContent);

    /**
     * 删除评论
     * @param blogid
     * @param commentId
     */
    void removeComment(Long blogid,Long commentId);
}
