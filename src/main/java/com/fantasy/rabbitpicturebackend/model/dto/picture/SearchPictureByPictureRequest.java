package com.fantasy.rabbitpicturebackend.model.dto.picture;

import lombok.Data;

import java.io.Serializable;

/**
 * 以图搜图请求
 */
@Data
public class SearchPictureByPictureRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 图片 id
     */
    private Long pictureId;
}
