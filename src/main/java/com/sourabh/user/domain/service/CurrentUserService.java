package com.sourabh.user.domain.service;


import com.sourabh.user.domain.entity.User;

public interface CurrentUserService {

    User getCurrentUser();

    Long getCurrentUserId();

    String getCurrentUserEmail();
}