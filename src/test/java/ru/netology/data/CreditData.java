package ru.netology.data;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CreditData {
    private String id;
    private String bank_id;
    private String created;
    private String status;
}