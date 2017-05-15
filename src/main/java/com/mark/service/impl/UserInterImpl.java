package com.mark.service.impl;

import java.util.List;

import javax.annotation.Resource;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mark.entity.User;
import com.mark.mapper.UserMapper;
import com.mark.service.UserInter;

@Service("userInterImpl")
public class UserInterImpl implements UserInter {

	
	@Resource
	UserMapper userMapper;
	

	@Override
	public List<User> findAll() {
		return userMapper.findAll();
	}
}
