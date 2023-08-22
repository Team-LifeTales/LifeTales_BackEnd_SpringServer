package com.LifeTales.domain.comment.domain;

public enum CommentRole {

    MASTER_COMMENT("masterComment"),
    SLAVE_COMMENT("slaveComment");

    private final String roleName;

    CommentRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }


}
