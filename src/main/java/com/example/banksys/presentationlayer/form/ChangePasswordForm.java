package com.example.banksys.presentationlayer.form;

import com.example.banksys.presentationlayer.utils.validator.CheckItFirst;
import com.example.banksys.presentationlayer.utils.validator.PasswordMatches;
import com.example.banksys.presentationlayer.utils.validator.PidMatches;
import com.example.banksys.presentationlayer.utils.validator.ThenCheckIt;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@PidMatches(groups = ThenCheckIt.class)
@PasswordMatches(groups = ThenCheckIt.class)
public class ChangePasswordForm implements ConfirmPasswordForm {

    private Long userId;
    @NotBlank(message = "身份证号不能为空！", groups = CheckItFirst.class)
    private String userPid;
    @NotBlank(message = "新密码不能为空！", groups = CheckItFirst.class)
    private String password;
    private String confirm;

    public ChangePasswordForm(Long userId) {
        this.userId = userId;
    }
}
