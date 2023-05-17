package com.example.banksys.presentationlayer.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WithdrawForm {

    @NotNull(message = "不能为空")
    @Min(value = 0, message = "取款金额不能为负！")
    private double money;
}
