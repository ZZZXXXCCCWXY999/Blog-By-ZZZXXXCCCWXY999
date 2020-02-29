package xyz.zxcwxy999.blog.aop;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserspaceAspect {

    @Pointcut("execution(* xyz.zxcwxy999.blog.controller.UserspaceController.*(..))")
    private void pointcut(){

    }

    @Before("xyz.zxcwxy999.blog.aop.UserspaceAspect.pointcut()")
    public void before(){
        System.out.println("Before");
    }

    @After("xyz.zxcwxy999.blog.aop.UserspaceAspect.pointcut()")
    public void after(){
        System.out.println("After");
    }
}
