package com.cuit.system.email.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Email {
    /**
     * 发送邮箱
     */
    @javax.validation.constraints.Email(message = "邮箱格式错误")
    private String to;

    /**
     * 主题
     */
    @NotBlank
    private String subject;

    /**
     * 内容
     */
    @NotBlank
    private String content;
}
