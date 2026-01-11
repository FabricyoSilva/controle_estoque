package br.com.estoque.util;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
