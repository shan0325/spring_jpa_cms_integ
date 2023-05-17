import jwtDecode from 'jwt-decode';

// 페이지 이동 시 accessToken 재발급 처리
export default async function (context) {
	const { store, redirect, route, $cookies, $moment } = context;

	if (process.server) return;

	// eslint-disable-next-line no-console
	console.info('%c1. middleware auth.js 시작', 'color:#EF9A9A');

	const BYPASS_LIST = ['/login'];
	if (BYPASS_LIST.includes(route.path)) {
		return;
	}

	// accessToken 만료된 경우 재발급 대상
	let isReissue = false;
	const accessToken = store.state.auth.accessToken;
	if (!accessToken) {
		return redirect('/login');
	}

	if (accessToken) {
		const decodedToken = jwtDecode(accessToken);
		const tokenExp = decodedToken.exp;

		const tokenExpDate = $moment(tokenExp * 1000);
		const curDate = $moment();

		const timeDiff = tokenExpDate.diff(curDate);
		if (timeDiff <= 0) {
			isReissue = true;
		}
	}

	if (!isReissue) return;

	try {
		await store.dispatch('auth/refreshtoken');
		// eslint-disable-next-line no-console
		console.info('%c - refreshToken 재발급 완료', 'color:#EF9A9A');
	} catch (error) {
		redirect('/login');
	}
}
