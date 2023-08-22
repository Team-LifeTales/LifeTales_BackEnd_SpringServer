package com.LifeTales.domain.user.domain;

public enum UserRole {

    FAMILY_LEADER("familyLeader"),
    FAMILY_MEMBER("familyMember"),
    TEMP("temp");

    private final String roleName;

    UserRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

}
