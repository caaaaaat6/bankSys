package com.example.banksys.presentationlayer.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DepositFixedForm extends DepositCurrentForm {

    @NotNull(message = "不能为空")
    @Min(value = 1, message = "不能为非正数")
    private Integer depositDays;
}
