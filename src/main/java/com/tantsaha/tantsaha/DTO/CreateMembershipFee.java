package com.tantsaha.tantsaha.DTO;

import com.tantsaha.tantsaha.enums.Frequency;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateMembershipFee {
    private LocalDate eligibleForm;
    private Frequency frequency;
    private Double amount;
    private String label;
}
