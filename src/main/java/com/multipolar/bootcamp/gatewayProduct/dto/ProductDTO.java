package com.multipolar.bootcamp.gatewayProduct.dto;

import java.io.Serializable;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO implements Serializable {
    private String id;
    private String productName;
    private ProductTypeDTO productType;
    private Double interestRate;
    private Double minimumBalance;
    private Double maximumLoanAmount;
    private String termsAndConditions;
    private LocalDateTime dateOfCreation = LocalDateTime.now();
}
