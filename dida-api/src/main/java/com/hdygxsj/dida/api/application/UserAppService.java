package com.hdygxsj.dida.api.application;

import com.hdygxsj.dida.api.domain.entity.UserDO;
import com.hdygxsj.dida.api.domain.service.UserDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserAppService {

    @Autowired
    private UserDomainService userDomainService;

    @GetMapping
    public List<UserDO> listAll(){
        return userDomainService.listAll();
    }
}
