package com.fantasy.rabbitpicturebackend.model.dto.picture;

import lombok.Data;

import java.io.Serializable;

/**
 * 图片审核请求
 */
@Data
public class PictureReviewRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 审核状态：0-待审核; 1-通过; 2-拒绝
     */
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    private String reviewMessage;
}
