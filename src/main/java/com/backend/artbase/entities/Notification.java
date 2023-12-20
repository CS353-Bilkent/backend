package com.backend.artbase.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Notification {

    private Integer userId;

    private Integer notificationId;

    private String content;

}
