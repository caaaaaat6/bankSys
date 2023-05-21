package com.example.banksys.presentationlayer.form;

import com.example.banksys.presentationlayer.utils.validator.PasswordMatches;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@PasswordMatches
public class AddEnterpriseUserForm implements ConfirmPasswordForm {

    @NotBlank(message = "不能为空")
    private String userName;
    @NotBlank(message = "不能为空")
    private String userPid;
    @NotBlank(message = "不能为空")
    private String password;
    private String confirm;

    // 不需要表单得到的
//    private Long enterpriseId;
//    private EnterpriseCard enterpriseCard;
}
