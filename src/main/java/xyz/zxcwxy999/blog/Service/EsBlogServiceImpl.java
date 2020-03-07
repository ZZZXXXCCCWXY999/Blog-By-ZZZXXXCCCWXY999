package xyz.zxcwxy999.blog.Service;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.ml.job.results.Bucket;
import org.elasticsearch.search.SearchParseException;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import xyz.zxcwxy999.blog.domain.User;
import xyz.zxcwxy999.blog.domain.es.EsBlog;
import xyz.zxcwxy999.blog.repository.es.EsBlogRepository;
import xyz.zxcwxy999.blog.vo.TagVO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

@Service
public class EsBlogServiceImpl implements EsBlogService {

    @Autowired
    private EsBlogRepository esBlogRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private UserService userService;

    private static final Pageable TOP_5_PAGEABLE = PageRequest.of(0, 5);
    private static final String EMPTY_KEYWORD = "";

    @Override
    public void removeEsBlog(String id) {
        esBlogRepository.deleteById(id);
    }

    @Override
    public EsBlog updateEsBlog(EsBlog esBlog) {
        return esBlogRepository.save(esBlog);
    }

    @Override
    public EsBlog getEsBlogByBlogId(Long blogId) {
        return esBlogRepository.findByBlogId(blogId);
    }

    @Override
    public Page<EsBlog> listNewestEsBlogs(String keyword, Pageable pageable) throws SearchParseException {
        Page<EsBlog> pages = null;
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        pages = esBlogRepository.findByTitleContainingOrSummaryContainingRoCOrContentContainingOrTagsContaining(keyword, keyword, keyword, keyword, pageable);
        return pages;
    }

    @Override
    public Page<EsBlog> listHotestEsBlogs(String keyword, Pageable pageable) {
        Sort sort = Sort.by(Sort.Direction.DESC, "readSize", "commentSize", "voteSize", "createTime");
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        return esBlogRepository.findByTitleContainingOrSummaryContainingRoCOrContentContainingOrTagsContaining(keyword, keyword, keyword, keyword, pageable);
    }

    @Override
    public Page<EsBlog> listEsBlogs(Pageable pageable) {
        return esBlogRepository.findAll(pageable);
    }

    @Override
    public List<EsBlog> listTop5NewestEsBlogs() {
        Page<EsBlog> page = this.listNewestEsBlogs(EMPTY_KEYWORD, TOP_5_PAGEABLE);
        return page.getContent();
    }

    @Override
    public List<EsBlog> listTop5HotestEsBlogs() {
        Page<EsBlog> page = this.listHotestEsBlogs(EMPTY_KEYWORD, TOP_5_PAGEABLE);
        return page.getContent();
    }

    @Override
    public List<TagVO> listTop30Tags() {
        List<TagVO> list = new ArrayList<>();
        //查询条件
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withSearchType(SearchType.QUERY_THEN_FETCH)
                .withIndices("blog")
                .withTypes("blog")
                .addAggregation(terms("tags").field("tags").order(Terms.Order.count(false)).size(30))
                .build();
        //聚合
        Aggregations aggregations=elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });
        StringTerms modelTerms=(StringTerms)aggregations.asMap().get("tags");
        Iterator<StringTerms.Bucket> modelBucketIt=modelTerms.getBuckets().iterator();
        while (modelBucketIt.hasNext()){
            Bucket actiontypeBucket=modelBucketIt.next();
            list.add(new TagVO(actiontypeBucket.getKey().toString(),actiontypeBucket.getDocCount()));
        }
        return list;
    }

    @Override
    public List<User> listTop12Users() {
        List<String> usernamelist=new ArrayList<>();//存储排序后的用户账号

        //查询条件
        SearchQuery searchQuery=new NativeSearchQueryBuilder().withQuery(matchAllQuery())
                .withSearchType(SearchType.QUERY_THEN_FETCH)
                .withIndices("blog")
                .withTypes("blog")
                .addAggregation(terms("users").field("username").order(Terms.Order.count(false)).size(12))
                .build();

        //聚合
        Aggregations aggregations=elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });
        StringTerms modelTerms=(StringTerms)aggregations.asMap().get("users");
        Iterator<StringTerms.Bucket> modelBucketIt=modelTerms.getBuckets().iterator();
        while (modelBucketIt.hasNext()){
            Bucket actiontypeBucket=modelBucketIt.next();
            String username=actiontypeBucket.getKey().toString();
            usernamelist.add(username);
        }

        //根据用户名查出用户的详细信息
        List<User>list=userService.listUsersByUsernames(usernamelist);
        //按照usernamelist的顺序返回用户对象
        List<User>returnList=new ArrayList<>();

        for (String username:usernamelist   ) {
            for (User user:list         ) {
                if(username.equals(user.getUsername())){
                    returnList.add(user);
                    break;
                }
            }
        }
        return returnList;
    }
}
