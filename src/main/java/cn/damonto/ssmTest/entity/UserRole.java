package cn.damonto.ssmTest.entity;

import cn.damonto.ssmTest.common.BaseEntity;

public class UserRole extends BaseEntity{

    private Long userId;
    private Long roleId;

    @Override
    public String toString() {
        return "UserRole [userId=" + userId + ", roleId=" + roleId + "]";
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }


}
