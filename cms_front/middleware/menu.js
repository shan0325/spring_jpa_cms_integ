// 이동할 메뉴 권한 확인
export default async function ({ store, route, redirect, $axios }) {
	// eslint-disable-next-line no-console
	console.info('%c2. middleware menu.js 시작', 'color:#CE93D8');

	// 파라미터 menuid 값 초기화
	store.commit('menu/setCurrentMenuId', null);

	const BYPASS_LIST = ['/', '/login'];
	if (BYPASS_LIST.includes(route.path)) {
		return;
	}

	const menuId = route.query.menuId;
	if (!menuId) {
		return redirect('/');
	}

	// 메뉴 권한 확인
	try {
		const isPermitMenu = await $axios.$get(
			`/api/menu-authorities/is-permit-menu?menuId=${menuId}`,
		);

		if (!isPermitMenu) {
			// eslint-disable-next-line no-console
			console.log('%c - 메뉴 권한이 없습니다.', 'color:#CE93D8');
			return redirect('/');
		}

		store.commit('menu/setCurrentMenuId', menuId);
	} catch (error) {
		return redirect('/');
	}
}
