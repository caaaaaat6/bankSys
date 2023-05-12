package com.example.banksys.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@NoArgsConstructor(force = true)
public class CurrentAccountHead extends Manager {
}
