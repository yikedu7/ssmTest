package cn.damonto.ssmTest.common;

import cn.damonto.ssmTest.context.UserContext;
import cn.damonto.ssmTest.dto.Accordion;
import cn.damonto.ssmTest.entity.User;

import cn.damonto.ssmTest.context.ResponseContext;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.util.*;

@Service
public class LoginUserCache {

    // 使用cookie
    private static Map<String,User> cache = new HashMap<String,User>();
    private static Map<String,List<Accordion>> userAccordionMap = new HashMap<String,List<Accordion>>();


    public static void put(User user) {
        cache.put(user.getName(), user);
        UserContext.setCurrent(user);
        setCookie(user);
    }

    public static User get(String username){
        return cache.get(username);
    }

    public static void setCookie(User user){
        int expire = 1800;//秒
        String source = user.getName() + "$"+user.getPwd();
        byte[] result = Base64.getEncoder().encode(source.getBytes());
        Cookie cookie = new Cookie("auth", new String(result));
        cookie.setMaxAge(expire);
        ResponseContext.getCurrent().addCookie(cookie);
    }

    public static void remove(String username){
        cache.remove(username);
        Cookie cookie = new Cookie("auth",null);
        ResponseContext.getCurrent().addCookie(cookie);
        UserContext.setCurrent(null);
    }

    public static void setAccordions(String username,List<Accordion> accordions) {
        userAccordionMap.put(username, accordions);
    }

    public static List<Accordion> getAccordions(String username) {
        return userAccordionMap.get(username);
    }


}
