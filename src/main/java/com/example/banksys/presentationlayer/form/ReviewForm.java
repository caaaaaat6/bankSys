package com.example.banksys.presentationlayer.form;

import lombok.Data;

import java.util.List;

@Data
public class ReviewForm {

    private List<Long> selectedEmployeesId;
}
