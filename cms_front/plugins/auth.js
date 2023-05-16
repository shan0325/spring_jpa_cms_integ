export default async function ({ store, route }) {
	// 새로고침 시 refreshToken 재발급 처리

	// eslint-disable-next-line no-console
	console.info('%c1. plugin auth.js 시작', 'color:#EF9A9A');

	const BYPASS_LIST = ['/login'];
	if (BYPASS_LIST.includes(route.path)) {
		return;
	}

	await store.dispatch('auth/refreshtoken');
	// eslint-disable-next-line no-console
	console.info('%c - refreshToken 재발급 완료', 'color:#EF9A9A');
}
