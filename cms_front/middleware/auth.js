import jwtDecode from 'jwt-decode';

// 새로고침 or 페이지 이동 시 accessToken 재발급 처리
export default async function ({ store, redirect, route, $cookies, $moment }) {
	// eslint-disable-next-line no-console
	console.info('%c1. middleware auth.js 시작', 'color:#EF9A9A');

	const BYPASS_LIST = ['/login'];
	if (BYPASS_LIST.includes(route.path)) {
		return;
	}

	let isReissue = false;
	if (process.server) {
		// 새로고침 시 재발급 대상
		isReissue = true;
	} else {
		const accessToken = store.state.auth.accessToken;
		if (accessToken) {
			const decodedToken = jwtDecode(accessToken);
			const tokenExp = decodedToken.exp;

			const tokenExpDate = $moment(tokenExp * 1000);
			const curDate = $moment();

			const timeDiff = tokenExpDate.diff(curDate);

			// accessToken 만료된 경우 재발급 대상
			if (timeDiff <= 0) {
				isReissue = true;
			}
		}
	}

	if (!isReissue) return;

	try {
		const data = await store.dispatch('auth/refreshtoken');
		// eslint-disable-next-line no-console
		console.info('%c - refreshToken 재발급 완료', 'color:#EF9A9A');

		if (process.server) {
			// eslint-disable-next-line no-console
			console.info('%c - 웹서버 refreshToken 쿠키 발급', 'color:#EF9A9A');
			setRefreshTokenCookie($cookies, store, data.refreshToken);
		}
	} catch (error) {
		return redirect('/login');
	}
}

const setRefreshTokenCookie = ($cookies, store, refreshToken) => {
	$cookies.set('refreshToken', refreshToken, {
		path: '/',
		// secure: true,
		httpOnly: true,
		maxAge: store.state.auth.REFRESH_TOKEN_EXPIRE_TIME / 1000,
	});
};
