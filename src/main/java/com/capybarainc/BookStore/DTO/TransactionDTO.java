package com.capybarainc.BookStore.DTO;

import com.capybarainc.BookStore.Models.TransactionElement;
import com.capybarainc.BookStore.Models.User;
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
    private List<TransactionElement> transactionElements;
    private Float price = 0f;
    private Long userId;
}