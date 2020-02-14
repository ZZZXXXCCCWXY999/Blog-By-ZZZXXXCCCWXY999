package xyz.zxcwxy999.blog.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import xyz.zxcwxy999.blog.domain.Blog;
import xyz.zxcwxy999.blog.domain.User;
import xyz.zxcwxy999.blog.repository.BlogRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class BlogServiceImpl implements BlogService{

    @Autowired
    private BlogRepository blogRepository;

    @Transactional
    @Override
    public Blog SaveBlog(Blog blog) {
        Blog returnBlog=blogRepository.save(blog);
        return returnBlog;
    }

    @Override
    public void removeBlog(Long id) {
        blogRepository.deleteById(id);
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
            this.SaveBlog(blogNew);
        }
    }
}
