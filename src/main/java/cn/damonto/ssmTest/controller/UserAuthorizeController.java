package cn.damonto.ssmTest.controller;

import cn.damonto.ssmTest.common.AjaxResult;
import cn.damonto.ssmTest.common.BaseEntity;
import cn.damonto.ssmTest.dto.Authorize;
import cn.damonto.ssmTest.entity.Role;
import cn.damonto.ssmTest.entity.User;
import cn.damonto.ssmTest.entity.UserRole;
import cn.damonto.ssmTest.service.RoleService;
import cn.damonto.ssmTest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/authorize")
public class UserAuthorizeController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    /**
     * 跳转到用户授权页面
     */
    @RequestMapping("/index")
    public String authorize(){
        return "/security/authorize/authorize_list";
    }

    /**
     * 跳转到用户角色页面
     */
    @RequestMapping("/userRole")
    public String userRoleIndex(){
        return "/security/authorize/user_role_list";
    }

    /**
     * 查询权限信息
     */
    @PostMapping("/getAuthorizes")
    @ResponseBody
    public List<Authorize> getAuthorizes(Integer page, Integer rows){
        List<UserRole> userRoles = userService.getUserRolesByPage(page, rows);
        Collection<Long> userIds = new HashSet<>();
        Collection<Long> roleIds = new HashSet<>();

        userRoles.forEach((ur) -> {
            userIds.add(ur.getUserId());
            roleIds.add(ur.getRoleId());
        });

        List<User> users = userService.getUsersByIds(userIds);
        List<Role> roles = roleService.getRoles(roleIds);

        Map<Long,User> userMap = BaseEntity.idEntityMap(users);
        Map<Long,Role> roleMap = BaseEntity.idEntityMap(roles);

        List<Authorize> authorizes = new LinkedList<>();
        userRoles.forEach((ur) -> {
            Authorize authorize = new Authorize();
            authorize.setUserRoleId(ur.getId());
            authorize.setUserId(ur.getUserId());
            authorize.setUserName(userMap.get(ur.getUserId()).getName());
            authorize.setRoleId(ur.getRoleId());
            authorize.setRoleNmae(roleMap.get(ur.getRoleId()).getName());
            authorizes.add(authorize);
        });
        return authorizes;
    }

    /**
     * 根据用户ID查询用户角色对应关系
     */
    @PostMapping("/getUserRoleByUserId")
    @ResponseBody
    public List<UserRole> getUserRoleByUserId(Long userId){
        return userService.getUserRolesByUserId(userId);
    }

    /**
     * 设置权限
     */
    @PostMapping("/setAuthorize")
    @ResponseBody
    public AjaxResult setAuthorize(User user, String roleIds){
        String[] temp = roleIds.split(",");
        Long[] roleIdArray = new Long[temp.length];
        for (int i=0;i<roleIdArray.length;i++){
            roleIdArray[i] = Long.valueOf(temp[i]);
        }
        userService.addUserRoles(user.getId(), roleIdArray);
        return AjaxResult.success();
    }
}
