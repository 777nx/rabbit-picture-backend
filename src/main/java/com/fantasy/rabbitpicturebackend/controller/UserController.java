package com.fantasy.rabbitpicturebackend.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fantasy.rabbitpicturebackend.annotation.AuthCheck;
import com.fantasy.rabbitpicturebackend.common.BaseResponse;
import com.fantasy.rabbitpicturebackend.common.DeleteRequest;
import com.fantasy.rabbitpicturebackend.common.ResultUtils;
import com.fantasy.rabbitpicturebackend.constant.UserConstant;
import com.fantasy.rabbitpicturebackend.exception.BusinessException;
import com.fantasy.rabbitpicturebackend.exception.ErrorCode;
import com.fantasy.rabbitpicturebackend.exception.ThrowUtils;
import com.fantasy.rabbitpicturebackend.model.dto.user.*;
import com.fantasy.rabbitpicturebackend.model.entity.User;
import com.fantasy.rabbitpicturebackend.model.vo.LoginUserVO;
import com.fantasy.rabbitpicturebackend.model.vo.UserVO;
import com.fantasy.rabbitpicturebackend.service.UserService;
import com.fantasy.rabbitpicturebackend.utils.EmailUtils;
import com.fantasy.rabbitpicturebackend.utils.VerificationCodeUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private EmailUtils emailUtils;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        ThrowUtils.throwIf(userRegisterRequest == null, ErrorCode.PARAMS_ERROR);
        long result = userService.userRegister(userRegisterRequest);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     */
    @PostMapping("/user")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userLoginRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userLoginRequest.getUserAccount();
        String password = userLoginRequest.getPassword();
        LoginUserVO loginUserVO = userService.userLogin(userAccount, password, request);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 获取当前登录用户
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(loginUser));
    }

    /**
     * 用户注销
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 创建用户
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        ThrowUtils.throwIf(userAddRequest == null, ErrorCode.PARAMS_ERROR);
        User user = new User();
        BeanUtil.copyProperties(userAddRequest, user);
        // 默认密码
        final String DEFAULT_PASSWORD = "12345678";
        String encryptPassword = userService.getEncryptPassword(DEFAULT_PASSWORD);
        user.setUserPassword(encryptPassword);
        // 插入数据库
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(user.getId());
    }

    /**
     * 根据 id 获取用户（仅管理员）
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<User> getUserById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    /**
     * 根据 id 获取包装类
     */
    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVOById(long id) {
        BaseResponse<User> response = getUserById(id);
        User user = response.getData();
        return ResultUtils.success(userService.getUserVO(user));
    }

    /**
     * 删除用户
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 更新用户
     */
    @PostMapping("/update")
    // @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断是否是管理员，管理员可以更新任意用户，普通用户只能更新自己
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null || !loginUser.getUserRole().equals(UserConstant.ADMIN_ROLE)) {
            userUpdateRequest.setUserRole(UserConstant.DEFAULT_ROLE);
        }
        User user = new User();
        BeanUtil.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 分页获取用户封装列表（仅管理员）
     *
     * @param userQueryRequest 查询请求参数
     */
    @PostMapping("/list/page/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest) {
        ThrowUtils.throwIf(userQueryRequest == null, ErrorCode.PARAMS_ERROR);
        long current = userQueryRequest.getCurrent();
        long pageSize = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, pageSize),
                userService.getQueryWrapper(userQueryRequest));
        Page<UserVO> userVOPage = new Page<>(current, pageSize, userPage.getTotal());
        List<UserVO> userVOList = userService.getUserVOList(userPage.getRecords());
        userVOPage.setRecords(userVOList);
        return ResultUtils.success(userVOPage);
    }

    /**
     * 发送邮箱验证码
     */
    @GetMapping("/sendEmail")
    public BaseResponse<Boolean> sendEmail(String email) {
        // 效验邮箱
        if (!userService.isValidEmail(email)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱格式错误");
        }
        // 构建缓存的 key
        String hashKey = DigestUtils.md5DigestAsHex(email.getBytes());
        String cacheKey = String.format(UserConstant.EMAIL_KEY_PREFIX + hashKey);
        String cacheKeyTimer = String.format(UserConstant.EMAIL_TIMER_KEY_PREFIX + hashKey);
        // 检查发送频率（1分钟内不能重复发送）
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(cacheKeyTimer))) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "操作过于频繁，请稍后再试");
        }
        // 生成验证码
        String code = VerificationCodeUtil.generateCode(6);
        // 发送邮件
        emailUtils.sendVerificationCode(email, code);
        // 存储验证码到 Redis（5分钟有效期）
        stringRedisTemplate.opsForValue().set(cacheKey, code, 5, TimeUnit.MINUTES);
        // 设置1分钟冷却期
        stringRedisTemplate.opsForValue().set(cacheKeyTimer, "cooling", 1, TimeUnit.MINUTES);
        // 返回结果
        return ResultUtils.success(true);
    }

    /**
     * 重置密码
     */
    @PostMapping("/resetPassword")
    public BaseResponse<Boolean> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        ThrowUtils.throwIf(resetPasswordRequest == null, ErrorCode.PARAMS_ERROR);
        boolean result = userService.resetPassword(resetPasswordRequest);
        return ResultUtils.success(result);
    }

    /**
     * 更新用户头像
     */
    @PostMapping("/update/avatar")
    public BaseResponse<String> updateUserAvatar(MultipartFile multipartFile, Long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        String result = userService.updateUserAvatar(multipartFile,id, request);
        return ResultUtils.success(result);
    }
}
