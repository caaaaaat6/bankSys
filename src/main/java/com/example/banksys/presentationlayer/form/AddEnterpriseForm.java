package com.example.banksys.presentationlayer.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddEnterpriseForm {
    @NotBlank(message = "企业名称不能为空！")
    private String enterpriseName;
}
