package com.vs.auth.service.impl;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vs.auth.entity.UserAuth;
import com.vs.auth.entity.UserInfo;
import com.vs.auth.entity.UserRole;
import com.vs.auth.enums.RoleEnum;
import com.vs.auth.exception.CustomException;
import com.vs.auth.mapper.UserAuthMapper;
import com.vs.auth.model.dto.UserInfoDTO;
import com.vs.auth.model.vo.UserAuthVO;
import com.vs.auth.service.UserAuthService;
import com.vs.auth.service.UserInfoService;
import com.vs.auth.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthServiceImpl extends ServiceImpl<UserAuthMapper, UserAuth> implements UserAuthService {

    private final UserInfoService userInfoService;

    private final UserRoleService userRoleService;

    // 邮箱登录
    @Override
    public UserInfoDTO loginEmail(UserAuthVO auth) {
        UserInfo userInfo = userInfoService.lambdaQuery().eq(UserInfo::getEmail, auth.getUsername()).one();
        if(Objects.isNull(userInfo)) throw new CustomException("404", "用户不存在", HttpStatus.NOT_FOUND);
        UserAuth userAuth = lambdaQuery().eq(UserAuth::getUserInfoId, userInfo.getId()).one();
        if(Objects.isNull(userAuth)) throw new CustomException("404", "用户不存在", HttpStatus.NOT_FOUND);
        if(!userAuth.getPassword().equals(auth.getPassword()))
            throw new CustomException("400", "密码错误", HttpStatus.UNAUTHORIZED);
        // 资源拷贝
        UserInfoDTO data = new UserInfoDTO();
        BeanUtil.copyProperties(userInfo, data, CopyOptions.create().setIgnoreNullValue(true));
        BeanUtil.copyProperties(userAuth, data, CopyOptions.create().setIgnoreNullValue(true));
        // 记录登录状态(7天有效)
        StpUtil.login(userAuth.getId(), new SaLoginModel().setTimeout(60 * 60 * 24 * 7));
        log.info("登录状态: {}", StpUtil.isLogin());
        data.setToken("satoken=" + StpUtil.getTokenValue());
        return data;
    }

    @SneakyThrows
    @Override
    public boolean checkIsAdmin(Integer loginId) {
        UserRole loginUser = userRoleService.lambdaQuery().eq(UserRole::getUserId, loginId).one();
        return Objects.equals(loginUser.getRoleId(), RoleEnum.ADMIN.getRoleId());
    }
}
