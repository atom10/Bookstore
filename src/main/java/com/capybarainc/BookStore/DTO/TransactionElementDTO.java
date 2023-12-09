package com.capybarainc.BookStore.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionElementDTO {

    private Long id;
    private Integer quantity;
    private BookDTO book;

}