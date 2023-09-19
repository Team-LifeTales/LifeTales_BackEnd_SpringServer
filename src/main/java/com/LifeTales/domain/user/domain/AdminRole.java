package com.LifeTales.domain.user.domain;

public enum AdminRole {

    DEVELOPER("developer"),
    CommunicationTeam("communicationTeam");

    private final String roleName;

    AdminRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
