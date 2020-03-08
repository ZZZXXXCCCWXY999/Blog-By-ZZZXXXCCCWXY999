package xyz.zxcwxy999.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.zxcwxy999.blog.Service.EsBlogService;
import xyz.zxcwxy999.blog.domain.User;
import xyz.zxcwxy999.blog.domain.es.EsBlog;
import xyz.zxcwxy999.blog.vo.TagVO;

import java.util.List;

/**
 * 主页控制器
 */
@Controller
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    private EsBlogService esBlogService;

    /**
     * 查询博客列表
     * @param order 排序规则
     * @param keyword 关键字
     * @return
     */
    @GetMapping
    public String listBlogs(@RequestParam(value = "order",required = false,defaultValue = "new") String order,
                            @RequestParam(value = "keyowrd",required = false,defaultValue = "") String keyword,
                            @RequestParam(value="async",required = false) boolean async,
                            @RequestParam(value = "pageIndex",required = false,defaultValue = "0")int pageIndex,
                            @RequestParam(value = "pageSize",required = false,defaultValue = "10")int pageSize,
                            Model model) {
        Page<EsBlog>page=null;
        List<EsBlog>list=null;
        boolean isEmpty=true;//系统初始化时，没有博客数据

        try {
            if(order.equals("hot")){//最热查询
                Sort sort= Sort.by(Sort.Direction.DESC, "readSize","commentSize","voteSize","createTime");
                Pageable pageable= PageRequest.of(pageIndex,pageSize);
                page=esBlogService.listHotestEsBlogs(keyword,pageable);
            }else if(order.equals("new")){//最新查询
                Sort sort= Sort.by(Sort.Direction.DESC, "createTime");
                Pageable pageable= PageRequest.of(pageIndex,pageSize);
                page=esBlogService.listHotestEsBlogs(keyword,pageable);
            }
            isEmpty=false;
        } catch (Exception e) {
            Pageable pageable=PageRequest.of(pageIndex,pageSize);
            page=esBlogService.listEsBlogs(pageable);
        }
        list=page.getContent();

        model.addAttribute("order",order);
        model.addAttribute("keyword",keyword);
        model.addAttribute("page",page);
        model.addAttribute("blogList",list);

        //首次访问页面才加载
        if(!async&&!isEmpty){
            List<EsBlog>newest=esBlogService.listTop5NewestEsBlogs();
            model.addAttribute("newest",newest);
            List<EsBlog>hotest=esBlogService.listTop5HotestEsBlogs();
            model.addAttribute("hotest",hotest);
            List<TagVO>tags=esBlogService.listTop30Tags();
            model.addAttribute("tags",tags);
            List<User> users=esBlogService.listTop12Users();
            model.addAttribute("users",users);
        }


        return (async==true?"/index :: #mainContainerRepleace":"/index");
    }

}
