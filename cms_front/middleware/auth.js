import jwtDecode from 'jwt-decode';
import moment from 'moment';

export default async function ({ store, $axios, redirect, route, $cookies }) {
	console.log('middleware auth.js');

	const BYPASS_LIST = ['/login'];
	if (BYPASS_LIST.includes(route.path)) {
		return;
	}

	if (process.server) {
		// 새로고침 시 토큰 재발급
		await store
			.dispatch('auth/refreshtoken')
			.then(data => {
				$cookies.set('refreshToken', data.refreshToken, {
					path: '/',
					// secure: true,
					httpOnly: true,
					maxAge: store.state.auth.REFRESH_TOKEN_EXPIRE_TIME / 1000,
				});
			})
			.catch(() => {
				redirect('/login');
			});
	}

	// accessToken이 없을 경우
	const accessToken = store.state.auth.accessToken;
	if (!accessToken) {
		await store.dispatch('auth/logout').then(response => {
			redirect('/login');
		});
		return;
	}

	const decodedToken = jwtDecode(accessToken);
	const tokenExp = decodedToken.exp;

	const tokenExpDate = moment(tokenExp * 1000);
	const curDate = moment();

	const timeDiff = tokenExpDate.diff(curDate);
	const minutesDiff = moment.duration(timeDiff).minutes();

	// accessToken 만료된 경우
	if (minutesDiff < 0) {
		await store.dispatch('auth/logout').then(response => {
			redirect('/login');
		});
		return;
	}

	// accessToken 이상 없을 시 클라이언트에 common.Authorization 설정
	$axios.defaults.headers.common.Authorization = `Bearer ${accessToken}`;

	// if (process.client) {
	// 	console.log(minutesDiff - 1 + '분 후에 accessToken이 재 발행됩니다.');

	// 	if (!store.state.auth.setTimeoutObj) {
	// 		// accessToken 만료하기 1분 전에 로그인 연장
	// 		const timeoutObj = setTimeout(function () {
	// 			store.dispatch('auth/refreshtoken').then(response => {
	// 				console.log('accessToken reissue 성공');
	// 			});
	// 		}, timeDiff - 60000);
	// 		store.commit('auth/setSetTimeoutObj', timeoutObj);
	// 	}
	// }
}
