package com.bm_nttdata.transaction_ms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase DTO que representa la respuesta de una operación.
 * Se utiliza para proporcionar información sobre el resultado de una operación,
 * incluyendo su estado, mensaje y posibles errores.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDto {

    private String status;
    private String message;
    private String error;
}
