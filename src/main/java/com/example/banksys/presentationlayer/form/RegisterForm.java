package com.example.banksys.presentationlayer.form;

import com.example.banksys.presentationlayer.utils.validator.PasswordMatches;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@PasswordMatches
public class RegisterForm implements ConfirmPasswordForm {

    @NotBlank(message = "姓名不能为空！")
    private String userName;
    @NotBlank(message = "身份证号不能为空！")
    private String userPid;
    @NotBlank(message = "雇员类型不能为空！")
    private String employeeType;
    @NotNull(message = "部门不能为空！")
    private Long departmentId;
    @NotBlank(message = "密码不能为空！")
    private String password;
    private String confirm;
}
