package com.hdygxsj.dida.api.domain.service.impl;


import com.hdygxsj.dida.api.domain.entity.UserDO;
import com.hdygxsj.dida.api.domain.service.UserDomainService;
import com.hdygxsj.dida.api.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDomainServiceImpl implements UserDomainService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<UserDO> listAll(){
        return userMapper.selectList(null);
    }

}

