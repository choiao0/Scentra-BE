package com.apollo.scentraapi.apiPayload.code.status;

import com.apollo.scentraapi.apiPayload.code.BaseErrorCode;
import com.apollo.scentraapi.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // 멤버 관련 에러
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER4001", "존재하지 않는 유저입니다."),
    USER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "USER4002", "이미 존재하는 유저입니다."),

    // 토큰 관련 에러
    NOT_VALID_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN4001", "유효하지 않은 토큰입니다."),
    WRONG_TYPE_SIGNATURE(HttpStatus.UNAUTHORIZED, "TOKEN4002", "잘못된 JWT 서명입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "TOKEN4003", "토큰이 만료되었습니다."),
    WRONG_TYPE_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN4004", "지원되지 않는 JWT 토큰입니다."),

    // 상품 관련 에러
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCT4001", "상품이 존재하지 않습니다."),
    PRODUCT_BAD_REQUEST(HttpStatus.BAD_REQUEST, "PRODUCT4002", "상품 업로드에 대한 요청이 올바르지 않습니다."),
    PRODUCT_NOT_FOUND_ON_SEARCH(HttpStatus.NOT_FOUND, "PRODUCT4003", "해당 검색어에 해당하는 상품이 없습니다."),
    INVALID_SEARCH_KEYWORD(HttpStatus.BAD_REQUEST, "PRODUCT4004", "검색어를 입력해야 합니다."),
    NO_LIKED_PRODUCTS(HttpStatus.BAD_REQUEST, "PRODUCT4005", "좋아요한 상품이 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "PRODUCT5001", "상품 검색 중 오류 발생"),

    // 브랜드 관련 에러
    BRAND_NOT_FOUND(HttpStatus.NOT_FOUND, "BRAND4001", "브랜드가 존재하지 않습니다."),
    BRAND_BAD_REQUEST(HttpStatus.BAD_REQUEST, "BRAND4002", "브랜드 생성에 대한 요청이 올바르지 않습니다."),
    NO_LIKED_BRANDS(HttpStatus.BAD_REQUEST, "BRAND4003", "좋아요한 브랜드가 없습니다."),
    BRAND_NOT_LIKED(HttpStatus.BAD_REQUEST, "BRAND4004", "좋아요한 브랜드가 아닙니다."),
    BRAND_ALREADY_LIKED(HttpStatus.BAD_REQUEST, "BRAND4005", "이미 좋아요한 브랜드입니다."),

    // 카테고리 관련 에러
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "CATEGORY4001", "카테고리가 존재하지 않습니다."),

    // 장바구니 에러
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "CART4001", "장바구니가 비어있습니다."),
    PRODUCT_NOT_FOUND_ON_CART(HttpStatus.NOT_FOUND, "CART4002", "상품을 찾을 수 없습니다."),
    CART_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "CART4003", "해당 상품이 장바구니에 없습니다."),
    INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "CART4004", "유효하지 않은 수량입니다."),

    // 리뷰 관련 에러
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "REVIEW4001", "리뷰가 존재하지 않습니다."),
    REVIEW_OWNER_MISMATCH(HttpStatus.BAD_REQUEST, "REVIEW4002", "해당 유저가 작성한 리뷰가 아닙니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}