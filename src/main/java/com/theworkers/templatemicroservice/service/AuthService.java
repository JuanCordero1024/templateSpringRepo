package com.theworkers.templatemicroservice.service;

import com.theworkers.templatemicroservice.model.input.UserLoginInput;
import com.theworkers.templatemicroservice.model.output.WebResponse;

public interface AuthService {
    WebResponse login(UserLoginInput credentials);
}
