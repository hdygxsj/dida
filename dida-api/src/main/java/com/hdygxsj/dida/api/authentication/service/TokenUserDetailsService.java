/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hdygxsj.dida.api.authentication.service;

import cn.hutool.core.collection.CollectionUtil;
import com.hdygxsj.dida.api.authentication.base.DidaUser;
import com.hdygxsj.dida.api.authentication.base.UserAuthenticationContextHolder;
import com.hdygxsj.dida.api.domain.entity.RoleDO;
import com.hdygxsj.dida.api.domain.entity.UserDO;
import com.hdygxsj.dida.api.domain.service.UserDomainService;
import com.hdygxsj.dida.security.Sm4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TokenUserDetailsService implements UserDetailsService {


    @Autowired
    private UserDomainService userDomainService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDO userDO = userDomainService.get(username);
        UserAuthenticationContextHolder context = UserAuthenticationContextHolder.getContext();
        Set<RoleDO> nowRoles = context.getRoles(username);
        if (CollectionUtil.isEmpty(nowRoles)) {
            List<RoleDO> roles = userDomainService.getRoles(username);
            context.addRole(username, roles);
            nowRoles = new HashSet<>(roles);
        }
        List<SimpleGrantedAuthority> grantedAuthorities = nowRoles.stream()
                .map(e -> new SimpleGrantedAuthority(e.getCode())).collect(Collectors.toList());
        DidaUser didaUser = new DidaUser(userDO.getUsername(), Sm4.execute(userDO.getPassword(), Sm4.DECRYPT), grantedAuthorities);
        didaUser.setUserDO(userDO);
        context.setOpUser(userDO);
        didaUser.setRoleDOList(new ArrayList<>(nowRoles));
        return didaUser;
    }
}
