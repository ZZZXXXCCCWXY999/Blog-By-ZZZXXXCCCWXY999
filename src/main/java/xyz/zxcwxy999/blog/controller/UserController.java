package xyz.zxcwxy999.blog.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import xyz.zxcwxy999.blog.Service.UserService;
import xyz.zxcwxy999.blog.domain.User;
import xyz.zxcwxy999.blog.util.ConstraintViolationExceptionHandler;
import xyz.zxcwxy999.blog.vo.Response;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 查询所有用户
     * @param async
     * @param pageIndex
     * @param pageSize
     * @param name
     * @param model
     * @return
     */
    @GetMapping
    public ModelAndView list(@RequestParam(value = "async",required = false) boolean async,
                             @RequestParam(value = "pageIndex",required = false,defaultValue = "0") int pageIndex,
                             @RequestParam(value = "pageSize",required = false,defaultValue = "10") int pageSize,
                             @RequestParam(value = "name",required = false,defaultValue = "") String name, Model model){
        Pageable pageable= PageRequest.of(pageIndex,pageSize);
        Page<User> page=userService.listUsersByNameLike(name,pageable);
        List<User> list=page.getContent();//当前所在页面数据列表

        model.addAttribute("page",page);
        model.addAttribute("userList",list);
        return new ModelAndView(async==true? "users/list :: #mainContainerRepleace":"users/list","userModel",model);
    }

    /**
     * 获取创建表单页面
     * @param model
     * @return
     */
    @GetMapping("/add")
    public ModelAndView createForm(Model model){
        model.addAttribute("user",new User(null,null,null,null));
        return new ModelAndView("users/add","userModel",model);
    }

    /**
     * 保存或修改用户
     * @param user
     * @return
     */
    @PostMapping
    public ResponseEntity<Response> saveOrUpdateUser(User user){
        try{
            userService.saveOrUpdateUser(user);
        }catch (ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }
        return ResponseEntity.ok().body(new Response(true,"处理成功",user));
    }

    /**
     * 删除用户
     * @param id
     * @param model
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Response> delete(@PathVariable("id") Long id,Model model){
        try{
            userService.removeUser(id);
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true,"处理成功"));
    }

    @GetMapping(value = "edit/{id}")
    public ModelAndView modifyForm(@PathVariable("id") Long id,Model model){
        Optional<User> user=userService.getUserById(id);
        model.addAttribute("user",user.get());
        return new ModelAndView("users/edit","userModel",model);
    }
}



























