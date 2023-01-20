export default async function ({
	$axios,
	store,
	redirect,
	$ADMIN_MENU_GROUP_ID,
}) {
	console.log('플러그인 입니다.');

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
