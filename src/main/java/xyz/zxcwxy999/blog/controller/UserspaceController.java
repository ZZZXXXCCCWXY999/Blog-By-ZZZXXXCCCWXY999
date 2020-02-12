package xyz.zxcwxy999.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import xyz.zxcwxy999.blog.Service.UserService;
import xyz.zxcwxy999.blog.domain.User;

/**
 * 用户主页控制器
 */
@Controller
@RequestMapping("/u")
public class UserspaceController {

    @Autowired
    private UserService userService;

    @Qualifier("UserService")
    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${file.server.url}")
    private String fileServerUrl;

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

    /**
     * 获取个人设置页面
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView profile(@PathVariable("username") String username,Model model){
        User user=(User)userDetailsService.loadUserByUsername(username);
        model.addAttribute("user",user);
        model.addAttribute("fileServerUrl",fileServerUrl);//文件服务器地址返回给客户端
        return new ModelAndView("/userspace/profile","userModel",model);
    }


    @PostMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")

}
