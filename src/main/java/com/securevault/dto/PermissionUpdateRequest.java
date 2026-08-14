package com.securevault.dto;

import com.securevault.entity.Permission;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionUpdateRequest {

    private Permission permission;
}