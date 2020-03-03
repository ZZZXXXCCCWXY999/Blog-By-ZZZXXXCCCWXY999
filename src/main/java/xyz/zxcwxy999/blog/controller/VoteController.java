package xyz.zxcwxy999.blog.controller;

import jdk.nashorn.internal.runtime.options.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import xyz.zxcwxy999.blog.Service.BlogService;
import xyz.zxcwxy999.blog.Service.VoteService;
import xyz.zxcwxy999.blog.domain.User;
import xyz.zxcwxy999.blog.domain.Vote;
import xyz.zxcwxy999.blog.util.ConstraintViolationExceptionHandler;
import xyz.zxcwxy999.blog.vo.Response;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

@Controller
@RequestMapping("/votes")
public class VoteController {
    @Autowired
    private BlogService blogService;

    @Autowired
    private VoteService voteService;

    /**
     * 点赞
     *
     * @param blogId
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Response> createVote(Long blogId) {
        try {
            blogService.createVote(blogId);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        return ResponseEntity.ok().body(new Response(true, "点赞成功", null));
    }

    /**
     * 删除点赞
     *
     * @param id
     * @param blogId
     * @return
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Response> delete(@PathVariable("id") Long id, Long blogId) {
        boolean isOwner = false;
        Optional<Vote> optionalVote = voteService.getVoteById(id);
        User user = null;
        if (optionalVote.isPresent()) {
            user = optionalVote.get().getUser();
        } else {
            return ResponseEntity.ok().body(new Response(false, "不存在该点赞"));
        }
        //判断操作用户是否为点赞的所有者
        if (SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal != null && user.getUsername().equals(principal.getUsername())) {
                isOwner = true;
            }
        }
        if (!isOwner) {
            return ResponseEntity.ok().body(new Response(false, "没有操作权限"));
        }
        try {
            blogService.removeVote(blogId, id);
            voteService.removeVote(id);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "取消点赞成功", null));
    }


}
