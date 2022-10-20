package com.spring.cms.dto.menu;

import com.spring.cms.domain.Site;
import com.spring.cms.dto.SiteDto;
import lombok.Data;

public class MenuGroupDto {

    @Data
    public static class AllMenuGroupResponse {
        private Long id;
        private String groupName;
        private SiteDto.SiteDetailResponse site;

        public AllMenuGroupResponse(Long id, String groupName, SiteDto.SiteDetailResponse site) {
            this.id = id;
            this.groupName = groupName;
            this.site = site;
        }
    }
}
