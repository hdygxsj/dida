package com.hdygxsj.dida.api.application;

import com.hdygxsj.dida.api.domain.entity.UserDO;
import com.hdygxsj.dida.api.domain.service.UserDomainService;
import com.hdygxsj.dida.api.exceptions.ServiceException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name="user")
@RequestMapping("api/v1/users")
public class UserAppService {

    @Autowired
    private UserDomainService userDomainService;

    @GetMapping
    @Operation
    public List<UserDO> listAll(){
        throw new ServiceException(-1,"系统错误");
    }
}
