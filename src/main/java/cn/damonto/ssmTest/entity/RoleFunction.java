package cn.damonto.ssmTest.entity;

import cn.damonto.ssmTest.common.BaseEntity;

public class RoleFunction extends BaseEntity {

    private Long roleId;
    private Long functionId;
    private Integer status;

    @Override
    public String toString() {
        return "RoleFunction [roleId=" + roleId + ", functionId=" + functionId + ", status=" + status + "]";
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Long functionId) {
        this.functionId = functionId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
