package xyz.zxcwxy999.blog.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.zxcwxy999.blog.domain.User;
import xyz.zxcwxy999.blog.domain.es.EsBlog;
import xyz.zxcwxy999.blog.vo.TagVO;

import java.util.List;

public interface EsBlogService {
    /**
     * 删除EsBlog
     * @param id
     */
    void removeEsBlog(String id);

    /**
     * 更新EsBlog
     * @param esBlog
     * @return
     */
    EsBlog updateEsBlog(EsBlog esBlog);

    /**
     * 根据Blog的id获取EsBlog
     * @param blogId
     * @return
     */
    EsBlog getEsBlogByBlogId(Long blogId);

    /**
     * 最新博客列表，分页
     * @param keyword
     * @param pageable
     * @return
     */
    Page<EsBlog> listNewestEsBlogs(String keyword, Pageable pageable);

    /**
     * 最热博客列表，分页
     * @param keyword
     * @param pageable
     * @return
     */
    Page<EsBlog> listHotestEsBlogs(String keyword,Pageable pageable);

    /**
     * 博客列表，分页
     * @param pageable
     * @return
     */
    Page<EsBlog> listEsBlogs(Pageable pageable);

    /**
     * 最新前5
     * @return
     */
    List<EsBlog> listTop5NewestEsBlogs();

    /**
     * 最热前5
     * @return
     */
    List<EsBlog> listTop5HotestEsBlogs();

    /**
     * 最热前30标签
     * @return
     */
    List<TagVO> listTop30Tags();

    /**
     * 最热前12用户
     * @return
     */
    List<User> listTop12Users();

}
