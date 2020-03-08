package xyz.zxcwxy999.blog.repository.es;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import xyz.zxcwxy999.blog.domain.es.EsBlog;

public interface EsBlogRepository extends ElasticsearchRepository<EsBlog, String> {

    /**
     * 模糊查询
     *
     * @param title
     * @param summary
     * @param content
     * @param tags
     * @param pageable
     * @return
     */
    Page<EsBlog> findByTitleContainingOrSummaryContainingRoCOrContentContainingOrTagsContaining(String title, String summary, String content, String tags, Pageable pageable);

    /**
     * 根据Blog的id查询EsBlog
     *
     * @param blogid
     * @return
     */
    EsBlog findByBlogId(Long blogid);
}
