package com.example.banksys.presentationlayer.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddDepartmentForm {
    @NotBlank
    private String departmentName;
}
