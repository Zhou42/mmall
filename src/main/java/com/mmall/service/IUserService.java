package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;

/**
 * Created by yangzhou on 2017-06-14.
 */
public interface IUserService {

    ServerResponse<User> login(String username, String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValid(String str, String type);

    ServerResponse selectQuestion(String username);

    ServerResponse<String> checkAnswer(String username, String question, String answer);

    ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken);

    ServerResponse<String> resetPassword(User user, String passwordNew, String passwordOld);

    ServerResponse<User> update_information(User user);

    ServerResponse<User> getInformation(Integer userId);

    public ServerResponse checkAdminRole(User user);

}
