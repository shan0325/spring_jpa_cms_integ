package com.spring.cms.service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface MenuAuthorityService {

    boolean isPermitMenu(HttpServletRequest request, Long menuId);
}
