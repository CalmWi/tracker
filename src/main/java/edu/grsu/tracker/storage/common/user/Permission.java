package edu.grsu.tracker.storage.common.user;

import lombok.Getter;

@Getter
public enum Permission {
    Member("member"),
    Edit("edit"),
    Write("write");
    private String permission;

    Permission(String permission) {
        this.permission = permission;
    }
}
