package com.mark.mapper;

import java.util.List;

import com.mark.annotation.MybatisDAO;
import com.mark.entity.User;

/**
 * 鍒涘缓涓�涓猆serMapper.java鏂囦欢浣滀负DAO鎺ュ彛銆�
 * @author Mark
 * 閫氳繃璇ユ帴鍙ｏ紝鐩存帴閫氳繃mapper鏂囦欢杩涜瀵瑰簲鐨凷QL璇彞鏄犲皠銆�
 */
@MybatisDAO
public interface UserMapper {
	
	void save(User user);  
	
    boolean update(User user);  
    
    boolean delete(int id);  
    
    User findById(int id);  
    
    List<User> findAll();
	
}
