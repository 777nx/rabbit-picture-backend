package com.fantasy.rabbitpicturebackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fantasy.rabbitpicturebackend.model.dto.user.UserQueryRequest;
import com.fantasy.rabbitpicturebackend.model.dto.user.UserRegisterRequest;
import com.fantasy.rabbitpicturebackend.model.dto.user.ResetPasswordRequest;
import com.fantasy.rabbitpicturebackend.model.entity.User;
import com.fantasy.rabbitpicturebackend.model.vo.LoginUserVO;
import com.fantasy.rabbitpicturebackend.model.vo.UserVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Fantasy
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2025-07-26 19:57:41
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userRegisterRequest 用户注册请求
     * @return 新用户 id
     */
    long userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request      httpRequest 请求方便设置 cookie
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取加密后的密码
     *
     * @param password 用户密码
     * @return 加密后的密码
     */
    String getEncryptPassword(String password);

    /**
     * 获取当前登录用户
     *
     * @param request httpRequest 请求方便设置 cookie
     * @return 当前登录用户
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 获取脱敏后的登录用户信息
     *
     * @param user 用户
     * @return 脱敏后的用户信息
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取用户脱敏信息
     *
     * @param user 脱敏前的信息
     * @return 脱敏后的信息
     */
    UserVO getUserVO(User user);

    /**
     * 批量获取用户脱敏信息
     *
     * @param userList 脱敏前的信息
     * @return 脱敏后的 List 列表
     */
    List<UserVO> getUserVOList(List<User> userList);

    /**
     * 用户注销
     *
     * @param request httpRequest 请求方便设置 cookie
     * @return 注销结果
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取查询条件
     *
     * @param userQueryRequest 查询条件
     * @return 查询条件
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    boolean isAdmin(User user);

    /**
     * 校验邮箱格式
     *
     * @param email
     * @return
     */
    boolean isValidEmail(String email);

    /**
     * 校验验证码
     *
     * @param userEmail
     * @param userInputCode
     * @return
     */
    boolean isValidCode(String userEmail, String userInputCode);

    /**
     * 忘记密码
     *
     * @param resetPasswordRequest
     * @return
     */
    boolean resetPassword(ResetPasswordRequest resetPasswordRequest);

    /**
     * 更新用户头像
     * @param multipartFile
     * @param id
     * @param request
     * @return
     */
    String updateUserAvatar(MultipartFile multipartFile, Long id, HttpServletRequest request);
}
