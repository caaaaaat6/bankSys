package com.example.banksys.model;

import lombok.Data;

@Data
public abstract class CurrentUserAccount extends BaseUserAccount implements CurrentAccountRight {

    @Override
    public void save() {

    }
}
