package cn.damonto.ssmTest.context;

import cn.damonto.ssmTest.entity.User;

public class UserContext {

    /**
     * 将用户信息写入线程
     */
    private static ThreadLocal<UserContext> tl = new ThreadLocal<UserContext>();

    private User user;

    private UserContext(User user){
        this.user = user;
    }

    public static void setCurrent(User user){
        tl.set(new UserContext(user));
    }

    public static UserContext getCurrent(){
        return tl.get();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
