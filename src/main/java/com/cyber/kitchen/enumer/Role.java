package com.cyber.kitchen.enumer;

import org.springframework.security.core.GrantedAuthority;

/**
 * Enum Role- перечисление ролей пользователя
 * **/
public enum Role implements GrantedAuthority {

    /**
     * роли пользователя
     * **/
    USER, ORGANIZER, ADMIN, EXPERT;

    /**
     * метод из GrantedAuthority
     * **/
    @Override
    public String getAuthority() {
        return name();
    }

}