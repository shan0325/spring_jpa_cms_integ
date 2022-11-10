package com.spring.cms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MenuType {
    MT_MENU("MT_MENU"),
    MT_BOARD("MT_BOARD"),
    MT_CONTENTS("MT_CONTENTS"),
    MT_LINK("MT_LINK");

    private String value;
}
