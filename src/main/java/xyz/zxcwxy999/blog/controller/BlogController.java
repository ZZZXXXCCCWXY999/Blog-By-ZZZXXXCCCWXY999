package xyz.zxcwxy999.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 主页控制器
 */
@Controller
@RequestMapping("/blogs")
public class BlogController {
    /**
     * 查询博客列表
     * @param order 排序规则
     * @param keyword 关键字
     * @return
     */
    @GetMapping
    public String listBlogs(@RequestParam(value = "order",required = false,defaultValue = "new") String order,
                            @RequestParam(value = "keyowrd",required = false,defaultValue = "") String keyword) {
        System.out.println("order:"+order+";keyword:"+keyword);
        return "redirect:/index?order="+order+"&keyword="+keyword;
    }


}
