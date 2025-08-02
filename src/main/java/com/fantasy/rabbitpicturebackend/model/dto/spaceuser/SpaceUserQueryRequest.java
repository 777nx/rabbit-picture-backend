package com.fantasy.rabbitpicturebackend.model.dto.spaceuser;

import lombok.Data;

import java.io.Serializable;

/**
 * 空间永辉查询请求
 */
@Data
public class SpaceUserQueryRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 空间 ID
     */
    private Long spaceId;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 空间角色：viewer/editor/admin
     */
    private String spaceRole;

}
