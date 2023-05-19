package com.example.banksys.presentationlayer.form;

import com.example.banksys.presentationlayer.utils.validator.CheckItFirst;
import com.example.banksys.presentationlayer.utils.validator.OutUserIdNameMatches;
import com.example.banksys.presentationlayer.utils.validator.ThenCheckIt;
import com.example.banksys.presentationlayer.utils.validator.ToUserIdNameMatches;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@OutUserIdNameMatches(message = "转出账户ID与姓名不匹配！",groups = ThenCheckIt.class)
@ToUserIdNameMatches(message = "转入账户ID与姓名不匹配！",groups = ThenCheckIt.class)
public class TransferForm {

    private Long outUserId;
    @NotBlank(message = "不能为空",groups = CheckItFirst.class)
    private String outName;
    @NotNull(message = "不能为空",groups = CheckItFirst.class)
    private Long toUserId;
    @NotBlank(message = "不能为空",groups = CheckItFirst.class)
    private String toName;
    @NotNull(message = "不能为空",groups = CheckItFirst.class)
    @Min(value = 0, message = "转账金额不能为负！",groups = CheckItFirst.class)
    private double money;

    public TransferForm(String outUserId) {
        this.outUserId = Long.parseLong(outUserId);
    }
}
