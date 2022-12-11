package com.spring.cms.enums;

/**
 * 코드 리스트를 가져오기 위한 부모 코드 정의 enum 클래스
 */
public enum CodeGroupDefinitionEnum {
    MENU_TYPE(2); // 메뉴 타입 부모 코드 아이디

    private long codeId;

    CodeGroupDefinitionEnum(long codeId) {
        this.codeId = codeId;
    }

    public long getCodeId() {
        return codeId;
    }
}
