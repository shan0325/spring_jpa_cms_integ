import jwtDecode from 'jwt-decode';
import moment from 'moment';

// 새로고침 or 페이지 이동 시 accessToken 재발급 처리
export default async function ({ store, $axios, redirect, route, $cookies }) {
	console.log('middleware auth.js');

	const BYPASS_LIST = ['/login'];
	if (BYPASS_LIST.includes(route.path)) {
		return;
	}

	let isReissue = false;
	if (process.server) {
		// 새로고침 시 재발급 대상
		isReissue = true;
	}

	const accessToken = store.state.auth.accessToken;
	if (accessToken) {
		const decodedToken = jwtDecode(accessToken);
		const tokenExp = decodedToken.exp;

		const tokenExpDate = moment(tokenExp * 1000);
		const curDate = moment();

		const timeDiff = tokenExpDate.diff(curDate);

		// accessToken 만료된 경우 재발급 대상
		if (timeDiff <= 0) {
			isReissue = true;
		}
	}

	if (isReissue) {
		await store
			.dispatch('auth/refreshtoken')
			.then(data => {
				console.log('refreshToken 재발급 완료');
				if (process.server) {
					console.log('프론트 서버에서 refreshToken 쿠키 발급');
					$cookies.set('refreshToken', data.refreshToken, {
						path: '/',
						// secure: true,
						httpOnly: true,
						maxAge:
							store.state.auth.REFRESH_TOKEN_EXPIRE_TIME / 1000,
					});
				}
			})
			.catch(() => {
				redirect('/login');
			});
	}
}
