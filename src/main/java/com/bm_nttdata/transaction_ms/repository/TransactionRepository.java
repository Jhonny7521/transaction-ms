package com.bm_nttdata.transaction_ms.repository;

import com.bm_nttdata.transaction_ms.entity.Transaction;
import com.bm_nttdata.transaction_ms.enums.ProductTypeEnum;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {

    List<Transaction> findByProductTypeAndCreditId(ProductTypeEnum productType, String productId);
    List<Transaction> findByProductTypeAndSourceAccountId(ProductTypeEnum productType, String productId);
    List<Transaction> findByProductTypeAndTargetAccountId(ProductTypeEnum productType, String productId);

}
