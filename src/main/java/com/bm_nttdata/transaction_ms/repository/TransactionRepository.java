package com.bm_nttdata.transaction_ms.repository;

import com.bm_nttdata.transaction_ms.entity.Transaction;
import com.bm_nttdata.transaction_ms.enums.ProductTypeEnum;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repositorio para la gestión de transacciones en MongoDB.
 * Proporciona operaciones de acceso a datos para la entidad Transaction.
 */
public interface TransactionRepository extends MongoRepository<Transaction, String> {

    /**
     * Busca todas las transacciones asociadas a un crédito específico según su tipo de producto.
     *
     * @param productType Tipo de producto bancario
     * @param productId ID de producto de crédito
     * @return Lista de transacciones que coinciden con los criterios de búsqueda
     */
    List<Transaction> findByProductTypeAndCreditId(
            ProductTypeEnum productType, String productId);

    /**
     * Busca todas las transacciones donde una cuenta específica actúa como origen
     * de la transacción, filtradas por tipo de producto.
     *
     * @param productType Tipo de producto bancario
     * @param productId ID de la cuenta origen
     * @return Lista de transacciones que coinciden con los criterios de búsqueda
     */
    List<Transaction> findByProductTypeAndSourceAccountId(
            ProductTypeEnum productType, String productId);

    /**
     * Busca todas las transacciones donde una cuenta específica actúa como destino
     * de la transacción, filtradas por tipo de producto.
     *
     * @param productType Tipo de producto bancario
     * @param productId ID de producto de crédito
     * @return Lista de transacciones que coinciden con los criterios de búsqueda
     */
    List<Transaction> findByProductTypeAndTargetAccountId(
            ProductTypeEnum productType, String productId);

}
