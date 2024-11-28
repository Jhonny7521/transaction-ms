package com.bm_nttdata.transaction_ms.repository;

import com.bm_nttdata.transaction_ms.entity.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
}
