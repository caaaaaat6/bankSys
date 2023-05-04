package com.example.banksys.model;

import lombok.Data;

@Data
public class OrdinaryCurrentUserAccount extends BaseUserAccount implements CurrentAccountRight {

    @Override
    public void save() {

    }
}
