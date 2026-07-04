/**
 * Module payment — Payment: Payment, PaymentMethod strategy, gateway adapter (VNPay/MoMo/COD).
 *
 * <p>Layout chuan khi phat trien module: {@code api} (Controller, Request/Response DTO), {@code
 * app} (Application Service, use-case orchestration, transaction boundary), {@code domain}
 * (Entity, Value Object, domain rule), {@code infra} (Repository, adapter ngoai).
 *
 * <p>Boundary rule: module khac KHONG duoc inject truc tiep repository cua module nay. Can du
 * lieu/hanh vi cua module nay thi goi qua application service / interface cong khai trong {@code
 * app}.
 */
package com.electrostore.payment;
