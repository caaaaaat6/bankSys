package com.example.banksys.presentationlayer.form;

import com.example.banksys.presentationlayer.utils.OutUserIdNameMatches;
import com.example.banksys.presentationlayer.utils.ToUserIdNameMatches;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@OutUserIdNameMatches
@ToUserIdNameMatches
public class TransferForm {

    private Long outUserId;
    @NotBlank(message = "不能为空")
    private String outName;
    @NotNull(message = "不能为空")
    private Long toUserId;
    @NotBlank(message = "不能为空")
    private String toName;
    @NotNull(message = "不能为空")
    @Min(value = 0, message = "转账金额不能为负！")
    private double money;

    public TransferForm(String outUserId) {
        this.outUserId = Long.parseLong(outUserId);
    }
}
