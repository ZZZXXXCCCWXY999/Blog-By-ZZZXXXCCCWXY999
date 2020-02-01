package xyz.zxcwxy999.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户主页控制器
 */
@Controller
@RequestMapping("/u")
public class UserspaceController {
    /**
     * 用户空间主页
     * @param username 用户名称（既是路径，又是参数）
     * @return
     */
    @GetMapping("/{username}")
    public String userSpace(@PathVariable("username")String username){
        System.out.println("username:"+username);
        return "/userspace/u";
    }

    /**
     * 用户博客列表
     * @param username 用户名称
     * @param order 排序规则
     * @param category 分类
     * @param keyword 关键字
     * @return
     */
    @GetMapping("/{username}/blogs")
    public String listBlogsByOrder(@PathVariable("username")String username,
                                   @RequestParam(value="order",required = false,defaultValue = "new")String order,
                                   @RequestParam(value = "category",required = false)String category,
                                   @RequestParam(value = "keyword",required = false)String keyword){
        //如果分类不为空，将会重定向到分类的页面
        if(category!=null){
            System.out.println("category:"+category);
            System.out.println("selflink:"+"redirect:/u/"+username+"/blogs?category="+category);
            return "/userspace/u";
        }else if(keyword!=null&&keyword.isEmpty()==false){
            System.out.println("keyword:"+keyword);
            System.out.println("selflink:"+"redirect:/u/"+username+"/blogs?keyword="+keyword);
            return "/userspace/u";
        }
        System.out.println("order:"+order);
        System.out.println("selflink:"+"redirect:/u/"+username+"/blogs?order="+order);
        return "/userspace/u";
    }

    /**
     * 显示用户某篇博客
     * @param id
     * @return
     */
    @GetMapping("/{username}/blogs/{id}")
    public String listBlogByOrder(@PathVariable("id") Long id){
        System.out.println("BlogId:"+id);
        return "/userspace/blog";
    }

    /**
     * 编辑博客
     * @return
     */
    @GetMapping("/{username}/blogs/edit")
    public String editBlog(){
        return "/userspace/blogedit";
    }
}
