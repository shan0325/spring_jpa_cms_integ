// 이동할 메뉴 권한 확인
export default async function (context) {
	const { $axios, $storage } = context;

	// eslint-disable-next-line no-console
	console.info('%c3. middleware naviMenu.js 시작', 'color:#7986CB');

	// const naviMenus = $storage.getItem('naviMenus');
	// console.log(naviMenus);

	if (process.server) return;

	const data = await $axios.$get('/api/version-info/name/menu');
	console.log(data);
}
