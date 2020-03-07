package xyz.zxcwxy999.blog.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import xyz.zxcwxy999.blog.domain.Blog;
import xyz.zxcwxy999.blog.domain.Comment;
import xyz.zxcwxy999.blog.domain.User;
import xyz.zxcwxy999.blog.domain.es.EsBlog;
import xyz.zxcwxy999.blog.repository.BlogRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class BlogServiceImpl implements BlogService{

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private EsBlogService esBlogService;

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        boolean isNew=(blog.getId()==null);
        EsBlog esBlog=null;
        Blog returnBlog=blogRepository.save(blog);
        if(isNew){
            esBlog=new EsBlog(returnBlog);
        }else{
            esBlog=esBlogService.getEsBlogByBlogId(blog.getId());
            esBlog.update(returnBlog);
        }
       esBlogService.updateEsBlog(esBlog);
        return returnBlog;
    }

    @Transactional
    @Override
    public void removeBlog(Long id) {
        blogRepository.deleteById(id);
        EsBlog esBlog=esBlogService.getEsBlogByBlogId(id);
        esBlogService.removeEsBlog(esBlog.getId());
    }

    @Transactional
    @Override
    public Optional<Blog> getBlogById(Long id) {
        return blogRepository.findById(id);
    }

    @Override
    public Page<Blog> listBlogByTitleVote(User user, String title, Pageable pageable) {
        //模糊查询
        title="%"+title+"%";
        String tags=title;
        Page<Blog> blogs=blogRepository.findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(title,user,tags,user,pageable);
        return blogs;
    }

    @Override
    public Page<Blog> listBlogByTitleVoteSort(User user, String title, Pageable pageable) {
        //模糊查询
        title="%"+title+"%";
        Page<Blog> blogs=blogRepository.findByUserAndTitleLike(user, title, pageable);
        return blogs;
    }

    @Override
    public void readingIncrease(Long id) {
        Optional<Blog>blog=blogRepository.findById(id);
        Blog blogNew=null;
        if(blog.isPresent()){
            blogNew=blog.get();
            blogNew.setReadSize(blogNew.getReadSize()+1);//在原有基础上递增1
            this.saveBlog(blogNew);
        }
    }

    @Override
    public Blog createComment(Long blogId, String commentContent) {
        Optional<Blog> optionalBlog=blogRepository.findById(blogId);
        Blog originalBlog=null;
        if(optionalBlog.isPresent()){
            originalBlog=optionalBlog.get();
            User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Comment comment=new Comment(user,commentContent);
            originalBlog.addComment(comment);
        }
        return this.saveBlog(originalBlog);
    }

    @Override
    public void removeComment(Long blogid, Long commentId) {
        Optional<Blog> optionalBlog=blogRepository.findById(blogid);
        if(optionalBlog.isPresent()){
            Blog originalBlog=optionalBlog.get();
            originalBlog.removeComment(blogid);
            this.saveBlog(originalBlog);
        }
    }
}
