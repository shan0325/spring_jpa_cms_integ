export default async function ({ $axios, store, route, $ADMIN_MENU_GROUP_ID }) {
	// eslint-disable-next-line no-console
	console.info('%cplugin navi-menu.js 시작', 'color:#EF9A9A');

	const BYPASS_LIST = ['/login'];
	if (BYPASS_LIST.includes(route.path)) {
		return;
	}

	const localVersion = localStorage.getItem('naviMenus');

	const { version } = await $axios.$get('/api/version-info/name/menu');
	console.log(version);

	if (!localVersion || !version || localVersion !== version) {
		const naviMenus = await store.dispatch('menu/getMenus', {
			menuGroupId: $ADMIN_MENU_GROUP_ID,
			managerId: store.state.auth.manager.id,
		});
		console.log(naviMenus);
		localStorage.setItem('naviMenus', JSON.stringify(naviMenus));
	}
}
