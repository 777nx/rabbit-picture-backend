package com.fantasy.rabbitpicturebackend.model.dto.space;

import lombok.Data;

import java.io.Serializable;

/**
 * 编辑空间请求
 */
@Data
public class SpaceEditRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 空间 id
     */
    private Long id;

    /**
     * 空间名称
     */
    private String spaceName;
}
