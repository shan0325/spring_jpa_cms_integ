package com.shan.spring.cms.service.impl;

import com.shan.spring.cms.dto.MenuDto;
import com.shan.spring.cms.enums.MenuLinkTargetEnum;
import com.shan.spring.cms.enums.MenuTypeEnum;
import com.shan.spring.cms.exception.BoardManagerException;
import com.shan.spring.cms.exception.ContentsException;
import com.shan.spring.cms.exception.MenuException;
import com.shan.spring.cms.exception.MenuGroupException;
import com.shan.spring.cms.repository.BoardManagerRepository;
import com.shan.spring.cms.repository.ContentsRepository;
import com.shan.spring.cms.repository.menu.MenuGroupRepository;
import com.shan.spring.cms.repository.menu.MenuLinkRepository;
import com.shan.spring.cms.repository.menu.MenuRepository;
import com.shan.spring.cms.repository.menu.dto.MenuQueryDto;
import com.shan.spring.cms.domain.Contents;
import com.shan.spring.cms.domain.menu.MenuLink;
import com.shan.spring.cms.domain.board.BoardManager;
import com.shan.spring.cms.domain.menu.Menu;
import com.shan.spring.cms.domain.menu.MenuGroup;
import com.shan.spring.cms.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Override
    public void createMenus(MenuDto.Create create) {
        Menu parentMenu = null;
        Menu topMenu = null;
        BoardManager boardManager = null;
        MenuLink menuLink = null;
        Contents contents = null;

        MenuGroup menuGroup = menuGroupRepository.findById(create.getMenuGroupId())
                .orElseThrow(() -> new MenuGroupException(MenuGroupException.MenuGroupExceptionType.NOT_FOUND_MENU_GROUP));

        Long parentId = create.getParentId();
        if (parentId != null) {
            parentMenu = menuRepository.findById(parentId)
                    .orElseThrow(() -> new MenuException(MenuException.MenuExceptionType.NOT_FOUND_PARENT_MENU));

            topMenu = menuRepository.findById(create.getTopId())
                    .orElseThrow(() -> new MenuException(MenuException.MenuExceptionType.NOT_FOUND_TOP_MENU));
        }

        MenuTypeEnum menuType = create.getMenuType();
        if (menuType.equals(MenuTypeEnum.MT_BOARD)) {
            Long boardManagerId = create.getBoardManagerId();
            if (boardManagerId == null) {
                throw new BoardManagerException(BoardManagerException.BoardManagerExceptionType.BOARD_MANAGER_ID_IS_NULL);
            }
            boardManager = boardManagerRepository.findById(boardManagerId)
                    .orElseThrow(() -> new BoardManagerException(BoardManagerException.BoardManagerExceptionType.BOARD_MANAGER_NOT_FOUND));
        } else if (menuType.equals(MenuTypeEnum.MT_LINK)) {
            menuLink = MenuLink.createMenuLink(create.getLink(), MenuLinkTargetEnum.valueOf(create.getLinkTarget()));
            menuLinkRepository.save(menuLink);
        } else if (menuType.equals(MenuTypeEnum.MT_CONTENTS)) {
            Long contentsId = create.getContentsId();
            if (contentsId == null) {
                throw new ContentsException(ContentsException.ContentsExceptionType.CONTENTS_ID_IS_NULL);
            }
            contents = contentsRepository.findById(contentsId)
                    .orElseThrow(() -> new ContentsException(ContentsException.ContentsExceptionType.CONTENTS_NOT_FOUND));
        }

        Menu menu = Menu.createMenu(create, menuGroup, parentMenu, topMenu, boardManager, menuLink, contents);
        menuRepository.save(menu);
    }

    @Transactional
    @Override
    public void updateMenu(Long menuId, MenuDto.Update update) {
        BoardManager boardManager = null;
        MenuLink menuLink = null;
        Contents contents = null;

        Menu findMenu = menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuException(MenuException.MenuExceptionType.NOT_FOUND_MENU));

        MenuGroup menuGroup = menuGroupRepository.findById(update.getMenuGroupId())
                .orElseThrow(() -> new MenuGroupException(MenuGroupException.MenuGroupExceptionType.NOT_FOUND_MENU_GROUP));

        MenuTypeEnum menuType = update.getMenuType();
        if (!findMenu.getMenuType().equals(menuType)) {
            if (menuType.equals(MenuTypeEnum.MT_BOARD)) {
                Long boardManagerId = update.getBoardManagerId();
                if (boardManagerId == null) {
                    throw new BoardManagerException(BoardManagerException.BoardManagerExceptionType.BOARD_MANAGER_ID_IS_NULL);
                }
                boardManager = boardManagerRepository.findById(boardManagerId)
                        .orElseThrow(() -> new BoardManagerException(BoardManagerException.BoardManagerExceptionType.BOARD_MANAGER_NOT_FOUND));
            } else if (menuType.equals(MenuTypeEnum.MT_LINK)) {
                menuLink = MenuLink.createMenuLink(update.getLink(), MenuLinkTargetEnum.valueOf(update.getLinkTarget()));
                menuLinkRepository.save(menuLink);
            } else if (menuType.equals(MenuTypeEnum.MT_CONTENTS)) {
                Long contentsId = update.getContentsId();
                if (contentsId == null) {
                    throw new ContentsException(ContentsException.ContentsExceptionType.CONTENTS_ID_IS_NULL);
                }
                contents = contentsRepository.findById(contentsId)
                        .orElseThrow(() -> new ContentsException(ContentsException.ContentsExceptionType.CONTENTS_NOT_FOUND));
            }
        }
        findMenu.updateMenu(update, menuGroup, boardManager, menuLink, contents);
    }

    @Transactional
    @Override
    public void deleteMenu(Long menuId) {
        Menu findMenu = menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuException(MenuException.MenuExceptionType.NOT_FOUND_MENU));

        Integer childCount = menuRepository.countByParent(findMenu);
        if (childCount > 0) {
            throw new MenuException(MenuException.MenuExceptionType.CHILD_MENU_EXISTS_CANNOT_DELETE);
        }

        menuRepository.delete(findMenu);
    }

    @Override
    public MenuDto.MenuDetailResponse getMenuDetail(Long menuId) {
        Menu findMenu = menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuException(MenuException.MenuExceptionType.NOT_FOUND_MENU));
        return modelMapper.map(findMenu, MenuDto.MenuDetailResponse.class);
    }

    @Override
    public List<MenuDto.AllMenusResponse> getAllMenus() {
        return menuRepository.findByParentOrderByOrd(null)
                .orElseThrow(() -> new MenuException(MenuException.MenuExceptionType.NOT_FOUND_MENU))
                .stream()
                .map(MenuDto.AllMenusResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuDto.AllMenusResponse> getMenusByMenuGroupId(Long menuGroupId) {
        MenuGroup menuGroup = menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new MenuGroupException(MenuGroupException.MenuGroupExceptionType.NOT_FOUND_MENU_GROUP));

        return menuRepository.findMenusByMenuGroup(null, menuGroup)
                .orElseThrow(() -> new MenuException(MenuException.MenuExceptionType.NOT_FOUND_MENU))
                .stream()
                .map(MenuDto.AllMenusResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * ToOne 관계들을 먼저 조회하고, 여기서 얻은 식별자 menuId로 ToMany 관계인 child를 한꺼번에 조회
     * MAP을 사용해서 매칭 성능 향상(O(1))
     */
    @Override
    public List<MenuQueryDto.AllMenusResponseQuery> getAllMenusOpti() {
        List<MenuQueryDto.AllMenusResponseQuery> menus = menuRepository.findMenus(true, null);
        setChildMenusByRecursive(menus);
        return menus;
    }

    private void setChildMenusByRecursive(List<MenuQueryDto.AllMenusResponseQuery> menus) {
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

    private Map<Long, List<MenuQueryDto.AllMenusResponseQuery>> findMenusChildMap(List<Long> menuIds) {
        return menuRepository.findMenus( false, menuIds).stream()
                .collect(Collectors.groupingBy(allMenusResponseQuery -> allMenusResponseQuery.getParentId()));
    }

    private List<Long> toMenusIds(List<MenuQueryDto.AllMenusResponseQuery> menus) {
        return menus.stream()
                .map(m -> m.getId())
                .collect(Collectors.toList());
    }

}
