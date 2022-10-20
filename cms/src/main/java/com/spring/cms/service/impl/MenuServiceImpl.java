package com.spring.cms.service.impl;

import com.spring.cms.domain.Contents;
import com.spring.cms.domain.MenuLink;
import com.spring.cms.domain.board.BoardManager;
import com.spring.cms.domain.menu.Menu;
import com.spring.cms.domain.menu.MenuGroup;
import com.spring.cms.dto.menu.MenuDto;
import com.spring.cms.dto.menu.MenuQueryDto;
import com.spring.cms.enums.MenuLinkTarget;
import com.spring.cms.enums.MenuType;
import com.spring.cms.exception.BoardManagerException;
import com.spring.cms.exception.ContentsException;
import com.spring.cms.exception.MenuException;
import com.spring.cms.exception.MenuGroupException;
import com.spring.cms.repository.BoardManagerRepository;
import com.spring.cms.repository.ContentsRepository;
import com.spring.cms.repository.menu.MenuGroupRepository;
import com.spring.cms.repository.menu.MenuLinkRepository;
import com.spring.cms.repository.menu.MenuRepository;
import com.spring.cms.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.spring.cms.exception.BoardManagerException.BoardManagerExceptionType.BOARD_MANAGER_ID_IS_NULL;
import static com.spring.cms.exception.BoardManagerException.BoardManagerExceptionType.BOARD_MANAGER_NOT_FOUND;
import static com.spring.cms.exception.ContentsException.ContentsExceptionType.CONTENTS_ID_IS_NULL;
import static com.spring.cms.exception.ContentsException.ContentsExceptionType.CONTENTS_NOT_FOUND;
import static com.spring.cms.exception.MenuException.MenuExceptionType.*;
import static com.spring.cms.exception.MenuGroupException.MenuGroupExceptionType.NOT_FOUND_MENU_GROUP;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final BoardManagerRepository boardManagerRepository;
    private final ContentsRepository contentsRepository;
    private final MenuLinkRepository menuLinkRepository;
    private final ModelMapper modelMapper;


    @Transactional
    public void createMenus(MenuDto.Create create) {
        Menu parentMenu = null;
        Menu topMenu = null;
        BoardManager boardManager = null;
        MenuLink menuLink = null;
        Contents contents = null;

        MenuGroup menuGroup = menuGroupRepository.findById(create.getMenuGroupId())
                .orElseThrow(() -> new MenuGroupException(NOT_FOUND_MENU_GROUP));

        Long parentId = create.getParentId();
        if (parentId != null) {
            parentMenu = menuRepository.findById(parentId)
                    .orElseThrow(() -> new MenuException(NOT_FOUND_PARENT_MENU));

            topMenu = menuRepository.findById(create.getTopId())
                    .orElseThrow(() -> new MenuException(NOT_FOUND_TOP_MENU));
        }

        MenuType menuType = MenuType.valueOf(create.getMenuType());
        if (menuType.equals(MenuType.MT_BOARD)) {
            Long boardManagerId = create.getBoardManagerId();
            if (boardManagerId == null) {
                throw new BoardManagerException(BOARD_MANAGER_ID_IS_NULL);
            }
            boardManager = boardManagerRepository.findById(boardManagerId)
                    .orElseThrow(() -> new BoardManagerException(BOARD_MANAGER_NOT_FOUND));
        } else if (menuType.equals(MenuType.MT_LINK)) {
            menuLink = MenuLink.createMenuLink(create.getLink(), MenuLinkTarget.valueOf(create.getLinkTarget()));
            menuLinkRepository.save(menuLink);
        } else if (menuType.equals(MenuType.MT_CONTENTS)) {
            Long contentsId = create.getContentsId();
            if (contentsId == null) {
                throw new ContentsException(CONTENTS_ID_IS_NULL);
            }
            contents = contentsRepository.findById(contentsId)
                    .orElseThrow(() -> new ContentsException(CONTENTS_NOT_FOUND));
        }

        Menu menu = Menu.createMenu(create, menuGroup, parentMenu, topMenu, boardManager, menuLink, contents);
        menuRepository.save(menu);
    }

    public List<MenuDto.AllMenusResponse> getAllMenus() {
        return menuRepository.findByParentOrderByOrd(null)
                .orElseThrow(() -> new MenuException(NOT_FOUND_MENU))
                .stream()
                .map(MenuDto.AllMenusResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * ToOne 관계들을 먼저 조회하고, 여기서 얻은 식별자 menuId로 ToMany 관계인 child를 한꺼번에 조회
     * MAP을 사용해서 매칭 성능 향상(O(1))
     */
    public List<MenuQueryDto.AllMenusResponseQuery> getAllMenusOpti() {
        List<MenuQueryDto.AllMenusResponseQuery> menus = menuRepository.findMenus(true, null);
        setChildMenusByRecursive(menus);
        return menus;
    }

    public void setChildMenusByRecursive(List<MenuQueryDto.AllMenusResponseQuery> menus) {
        if (menus == null || menus.isEmpty()) {
            return;
        }

        List<MenuQueryDto.AllMenusResponseQuery> childAllMenus = new ArrayList<>();

        Map<Long, List<MenuQueryDto.AllMenusResponseQuery>> menusChildMap = findMenusChildMap(toMenusIds(menus));
        menus.forEach(m -> {
            List<MenuQueryDto.AllMenusResponseQuery> childMenus = menusChildMap.get(m.getId());
            m.setChildMenus(childMenus);
            if (childMenus != null) {
                childAllMenus.addAll(childMenus);
            }
        });
        setChildMenusByRecursive(childAllMenus);
    }

    public Map<Long, List<MenuQueryDto.AllMenusResponseQuery>> findMenusChildMap(List<Long> menuIds) {
        return menuRepository.findMenus( false, menuIds).stream()
                .collect(Collectors.groupingBy(allMenusResponseQuery -> allMenusResponseQuery.getParentId()));
    }

    private List<Long> toMenusIds(List<MenuQueryDto.AllMenusResponseQuery> menus) {
        return menus.stream()
                .map(m -> m.getId())
                .collect(Collectors.toList());
    }

    public MenuDto.MenuDetailResponse getMenuDetail(Long menuId) {
        Menu findMenu = menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuException(NOT_FOUND_MENU));
        return modelMapper.map(findMenu, MenuDto.MenuDetailResponse.class);
    }

    @Transactional
    public void updateMenu(Long menuId, MenuDto.Update update) {
        BoardManager boardManager = null;
        MenuLink menuLink = null;
        Contents contents = null;

        Menu findMenu = menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuException(NOT_FOUND_MENU));

        MenuGroup menuGroup = menuGroupRepository.findById(update.getMenuGroupId())
                .orElseThrow(() -> new MenuGroupException(NOT_FOUND_MENU_GROUP));

        MenuType menuType = MenuType.valueOf(update.getMenuType());
        if (!findMenu.getMenuType().equals(menuType)) {
            if (menuType.equals(MenuType.MT_BOARD)) {
                Long boardManagerId = update.getBoardManagerId();
                if (boardManagerId == null) {
                    throw new BoardManagerException(BOARD_MANAGER_ID_IS_NULL);
                }
                boardManager = boardManagerRepository.findById(boardManagerId)
                        .orElseThrow(() -> new BoardManagerException(BOARD_MANAGER_NOT_FOUND));
            } else if (menuType.equals(MenuType.MT_LINK)) {
                menuLink = MenuLink.createMenuLink(update.getLink(), MenuLinkTarget.valueOf(update.getLinkTarget()));
                menuLinkRepository.save(menuLink);
            } else if (menuType.equals(MenuType.MT_CONTENTS)) {
                Long contentsId = update.getContentsId();
                if (contentsId == null) {
                    throw new ContentsException(CONTENTS_ID_IS_NULL);
                }
                contents = contentsRepository.findById(contentsId)
                        .orElseThrow(() -> new ContentsException(CONTENTS_NOT_FOUND));
            }
        }
        findMenu.updateMenu(update, menuGroup, boardManager, menuLink, contents);
    }

    @Transactional
    public void deleteMenu(Long menuId) {
        Menu findMenu = menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuException(NOT_FOUND_MENU));

        Integer childCount = menuRepository.countByParent(findMenu);
        if (childCount > 0) {
            throw new MenuException(CHILD_MENU_EXISTS_CANNOT_DELETE);
        }

        menuRepository.delete(findMenu);
    }
}
