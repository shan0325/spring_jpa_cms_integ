package com.shan.spring.cms.service.data;

import com.shan.spring.cms.enums.AuthorityEnum;
import com.shan.spring.cms.domain.Authority;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class MenuServiceData {

    @Getter
    @Builder
    public static class NaviMenuData {
        private Long menuGroupId;
        private Authority authority;
        private AuthorityEnum authorityEnum;
        private Boolean parentIsNull;
        private List<Long> menuIds;
        private Character useYn;
    }
}
