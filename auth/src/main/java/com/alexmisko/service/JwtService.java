package com.alexmisko.service;

import com.alexmisko.pojo.User;
import com.alexmisko.vo.JwtToken;
import com.alexmisko.vo.LoginUserInfo;
import com.alexmisko.vo.Result;

public interface JwtService{
    String generateToken(User user) throws Exception;
    Result<JwtToken> signIn(User user) throws Exception;
    Result<JwtToken> signUp(User user, String code) throws Exception;
    Result<User> getUser(Long userId) throws Exception;
}
