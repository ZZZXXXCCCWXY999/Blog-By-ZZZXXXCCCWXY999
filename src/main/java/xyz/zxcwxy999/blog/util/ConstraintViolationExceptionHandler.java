package xyz.zxcwxy999.blog.util;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * ConstraintViolationException处理器
 * 用于处理ConstraintViolationException异常
 */
public class ConstraintViolationExceptionHandler {
    public static String getMessage(ConstraintViolationException e){
        List<String> msgList=new ArrayList<>();
        for(ConstraintViolation<?> constraintViolation:e.getConstraintViolations()){
            msgList.add(constraintViolation.getMessage());
        }
        String messages= StringUtils.join(msgList.toArray(),";");
        return messages;
    }
}
