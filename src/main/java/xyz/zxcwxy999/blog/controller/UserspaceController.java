package xyz.zxcwxy999.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import xyz.zxcwxy999.blog.Service.BlogService;
import xyz.zxcwxy999.blog.Service.UserService;
import xyz.zxcwxy999.blog.domain.Blog;
import xyz.zxcwxy999.blog.domain.User;
import xyz.zxcwxy999.blog.util.ConstraintViolationExceptionHandler;
import xyz.zxcwxy999.blog.vo.Response;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

/**
 * 用户主页控制器
 */
@Controller
@RequestMapping("/u")
public class UserspaceController {

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Qualifier("UserService")
    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${file.server.url}")
    private String fileServerUrl;

    /**
     * 用户的空间主页
     *
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}")
    public String userSpace(@PathVariable("username") String username, Model model) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user", user);
        return "redirect:/u/" + username + "/blogs";
    }

    /**
     * 获取用户的博客列表
     * @param username
     * @param order
     * @param catalogId
     * @param keyword
     * @param async
     * @param pageIndex
     * @param pageSize
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogs")
    public String listBlogsByOrder(@PathVariable("username") String username,
                                   @RequestParam(value = "order", required = false, defaultValue = "new") String order,
                                   @RequestParam(value = "catalog", required = false) Long catalogId,
                                   @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                   @RequestParam(value = "async", required = false) boolean async,
                                   @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
                                   @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize, Model model) {

        User user = (User) userDetailsService.loadUserByUsername(username);
        Page<Blog> page = null;
        if (catalogId != null && catalogId > 0) {//分类查询

        } else if (order.equals("hot")) {//最热查询
            Sort sort = new Sort(Sort.Direction.DESC, "readSize", "commentSize", "voteSize");
            Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
            page = blogService.listBlogByTitleVote(user, keyword, pageable);
        } else if (order.equals("new")) {
            Pageable pageable = PageRequest.of(pageIndex, pageSize);
            page = blogService.listBlogByTitleVote(user, keyword, pageable);
        }
        List<Blog> list = page.getContent();//当前所在页面数据列表
        model.addAttribute("user", user);
        model.addAttribute("order", order);
        model.addAttribute("catalogId", catalogId);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("blogList", list);
        return (async == true ? "/userspace/u :: #mainContainerRepleace" : "/userspace/u");
    }

    /**
     * 显示用户某篇博客
     * @param username
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogs/{id}")
    public String listBlogByOrder(@PathVariable("username") String username, @PathVariable("id") Long id, Model model) {
        User princepal = null;
        Optional<Blog> blog = blogService.getBlogById(id);

        //每次读取，简单地可以认为阅读量增加1次
        blogService.readingIncrease(id);

        //判断操作用户是否为博客的所有者
        boolean isBlogOwner = false;
        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            princepal=(User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(princepal!=null&&username.equals(princepal.getUsername())){
                isBlogOwner=true;
            }
        }
        model.addAttribute("isBlogOwner",isBlogOwner);
        model.addAttribute("blogModel",blog.get());

        return "/userspace/blog";
    }

    /**
     * 获取新增博客的页面
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogs/edit")
    public ModelAndView createBlog(@PathVariable("username")String username,Model model) {
        model.addAttribute("blog",new Blog(null,null,null));
        model.addAttribute("fileServerUrl",fileServerUrl);
        //文件服务器的地址传给客户端
        return new ModelAndView("/userspace/blogedit","blogModel",model);
    }

    /**
     * 获取新增博客的页面
     * @param username
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogs/edit/{id}")
    public ModelAndView editBlog(@PathVariable("username")String username,@PathVariable("id")Long id,Model model){
        model.addAttribute("blog",blogService.getBlogById(id).get());
        model.addAttribute("fileServerUrl",fileServerUrl);
        //文件服务器的地址返回给客户端
        return new ModelAndView("/userspace/blogedit","blogModel",model);
    }

    /***
     * 保存博客
     * @param username
     * @param blog
     * @return
     */
    @PostMapping("/{username}/blogs/edit")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveBlog(@PathVariable("username")String username,@RequestBody Blog blog{
        try{
            //判断是修改还是新增
            if(blog.getId()!=null){
                Optional<Blog>optionalBlog=blogService.getBlogById(blog.getId());
                if (optionalBlog.isPresent()){
                    Blog orignalBlog=optionalBlog.get();
                    orignalBlog.setTitle(blog.getTitle());
                    orignalBlog.setContent(blog.getContent());
                    orignalBlog.setSummary(blog.getSummary());
                    blogService.saveBlog(orignalBlog);
                }
            }else{
                User user=(User)userDetailsService.loadUserByUsername(username);
                blog.setUser(user);
                blogService.saveBlog(blog);
            }
        }catch (ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }catch(Exception e){
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }
        String redirectUrl="/u/"+username+"/blogs/"+blog.getId();
        return  ResponseEntity.ok().body(new Response(true,"处理成功",redirectUrl));
    }

    /**
     * 删除博客
     * @param username
     * @param id
     * @return
     */
    @DeleteMapping("/{username}/blogs/{id}")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response>deleteBlog(@PathVariable("username")String username,@PathVariable("id")Long id){
        try{
            blogService.removeBlog(id);
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }

        String redirectUrl="/u/"+username+"/blogs";
        return ResponseEntity.ok().body(new Response(true,"处理成功",redirectUrl));
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
