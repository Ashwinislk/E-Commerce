package com.electronicstore.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {

    private String roleId;

    private String roleName;
}
