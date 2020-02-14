package xyz.zxcwxy999.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import xyz.zxcwxy999.blog.Service.UserService;
import xyz.zxcwxy999.blog.domain.User;
import xyz.zxcwxy999.blog.vo.Response;

import java.util.Optional;

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
     *
     * @param username 用户名称（既是路径，又是参数）
     * @return
     */
    @GetMapping("/{username}")
    public String userSpace(@PathVariable("username") String username) {
        System.out.println("username:" + username);
        return "/userspace/u";
    }

    /**
     * 用户博客列表
     *
     * @param username 用户名称
     * @param order    排序规则
     * @param category 分类
     * @param keyword  关键字
     * @return
     */
    @GetMapping("/{username}/blogs")
    public String listBlogsByOrder(@PathVariable("username") String username,
                                   @RequestParam(value = "order", required = false, defaultValue = "new") String order,
                                   @RequestParam(value = "category", required = false) String category,
                                   @RequestParam(value = "keyword", required = false) String keyword) {
        //如果分类不为空，将会重定向到分类的页面
        if (category != null) {
            System.out.println("category:" + category);
            System.out.println("selflink:" + "redirect:/u/" + username + "/blogs?category=" + category);
            return "/userspace/u";
        } else if (keyword != null && keyword.isEmpty() == false) {
            System.out.println("keyword:" + keyword);
            System.out.println("selflink:" + "redirect:/u/" + username + "/blogs?keyword=" + keyword);
            return "/userspace/u";
        }
        System.out.println("order:" + order);
        System.out.println("selflink:" + "redirect:/u/" + username + "/blogs?order=" + order);
        return "/userspace/u";
    }

    /**
     * 显示用户某篇博客
     *
     * @param id
     * @return
     */
    @GetMapping("/{username}/blogs/{id}")
    public String listBlogByOrder(@PathVariable("id") Long id) {
        System.out.println("BlogId:" + id);
        return "/userspace/blog";
    }

    /**
     * 编辑博客
     *
     * @return
     */
    @GetMapping("/{username}/blogs/edit")
    public String editBlog() {
        return "/userspace/blogedit";
    }

    /**
     * 获取个人设置页面
     *
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView profile(@PathVariable("username") String username, Model model) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("fileServerUrl", fileServerUrl);//文件服务器地址返回给客户端
        return new ModelAndView("/userspace/profile", "userModel", model);
    }

    /**
     * 保存个人设置
     *
     * @param username
     * @param user
     * @return
     */
    @PostMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public String savaProfile(@PathVariable("username") String username, User user) {
        User originalUser = userService.getUserById(user.getId()).get();
        originalUser.setEmail(user.getEmail());
        originalUser.setName(user.getName());

        //判断密码是否做了变更
        String rawPassword = originalUser.getPassword();
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodePasswd = encoder.encode(user.getPassword());
        boolean isMatch = encoder.matches(rawPassword, encodePasswd);
        if (!isMatch) {
            originalUser.setEncodePassword(user.getPassword());                  //方法还没有写
        }
        userService.saveOrUpdateUser(originalUser);
        return "redirect:/u/" + username + "/profile";
    }

    /**
     * 获取编辑头像的界面
     *
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView avatar(@PathVariable("username") String username, Model model) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user", user);
        return new ModelAndView("/userspace/avatar", "userModel", model);
    }

    /**
     * 保存头像
     *
     * @param username
     * @param user
     * @return
     */
    @PostMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveAvatar(@PathVariable("username") String username, @RequestBody User user) {
        String avatarUrl = user.getAvatar();
        Optional<User> originalUser = userService.getUserById(user.getId());
        originalUser.get().setAvatar(avatarUrl);
        userService.saveOrUpdateUser(originalUser.get());
        return ResponseEntity.ok().body(new Response(true, "处理成功", avatarUrl));
    }

}
