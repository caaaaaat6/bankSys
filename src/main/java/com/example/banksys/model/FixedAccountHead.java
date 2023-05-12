package com.example.banksys.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor(force = true)
public class FixedAccountHead extends Manager{
}
