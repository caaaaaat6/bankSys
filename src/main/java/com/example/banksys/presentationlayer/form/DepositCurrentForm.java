package com.example.banksys.presentationlayer.form;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class DepositCurrentForm {

    @Min(value = 0, message = "存款金额不能为负")
    private double money;
}
