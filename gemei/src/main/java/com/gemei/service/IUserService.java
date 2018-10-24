package com.gemei.service;

import com.gemei.common.ServerResponse;
import com.gemei.pojo.User;

/**
 * 
 * @author asus
 *
 */
public interface IUserService {
	
	ServerResponse<User> login(String username,String password);
	
	ServerResponse<String> register(User user);
	
	ServerResponse<String> checkValid(String str,String type);
	
	ServerResponse selectQuestion(String username);
	
	ServerResponse<String> checkAnswer(String username,String question,String answer);
	
	ServerResponse<String> forgetRestPassword(String username,String passwordNew,String forgetToken);

	ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user);

	ServerResponse<User> updateInformation(User user);
	
	ServerResponse<User> getInformation(Integer userId);
	
	ServerResponse checkAdminRole(User user);


}
