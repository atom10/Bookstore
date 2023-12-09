package com.capybarainc.BookStore.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {

    private Long id;
    private List<TransactionElementDTO> transactionElements;
    private Float price = 0f;
    private Float paid = 0f;
    private Boolean completed = false;

}