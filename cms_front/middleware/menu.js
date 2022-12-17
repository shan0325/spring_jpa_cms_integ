export default function ({ route, redirect }) {
	console.log(route);

	const BYPASS_LIST = ['/', '/login'];
	if (BYPASS_LIST.includes(route.path)) {
		return;
	}

	const menuId = route.query.menuId;
	if (!menuId) {
		redirect('/');
	}

	// 메뉴 권한 확인
}
