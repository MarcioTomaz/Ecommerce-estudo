package com.ec.ecommercev3.Entity.Enums;

public enum ExchangeStatus {
    REQUESTED,        // Cliente solicitou a troca
    UNDER_REVIEW,     // Em análise
    APPROVED,         // Troca aprovada
    REJECTED,         // Troca recusada
    AWAITING_RETURN,  // Aguardando cliente enviar o produto
    IN_TRANSIT,       // Produto em transporte
    RECEIVED,         // Produto recebido
    EXCHANGED,        // Troca realizada (novo item enviado)
    COMPLETED,        // Processo finalizado
    CANCELED          // Solicitação cancelada
}
