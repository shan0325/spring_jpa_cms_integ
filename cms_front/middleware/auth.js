import jwtDecode from 'jwt-decode';
import moment from 'moment';

export default function ({ store, $axios, redirect, route, $cookies }) {
	console.log('middleware auth.js');

	// const BYPASS_LIST = ['/login'];
	// if (BYPASS_LIST.includes(route.path)) {
	// 	return;
	// }

	// let isReissue = false;
	// if (process.server) {
	// 	isReissue = true;
	// }

	// const accessToken = store.state.auth.accessToken;
	// if (accessToken) {
	// 	const decodedToken = jwtDecode(accessToken);
	// 	const tokenExp = decodedToken.exp;

	// 	const tokenExpDate = moment(tokenExp * 1000);
	// 	const curDate = moment();

	// 	const timeDiff = tokenExpDate.diff(curDate);
	// 	const minutesDiff = moment.duration(timeDiff).minutes();

	// 	// refreshToken 만료된 경우
	// 	if (minutesDiff < -store.state.auth.REFRESH_TOKEN_EXPIRE_TIME) {
	// 		redirect('/login');
	// 	}

	// 	// accessToken 남은 시간이 15분 이하일 경우 재발급
	// 	if (minutesDiff <= 15) {
	// 		isReissue = true;
	// 	}
	// }

	// if (isReissue) {
	// 	await store
	// 		.dispatch('auth/refreshtoken')
	// 		.then(data => {
	// 			$cookies.set('refreshToken', data.refreshToken, {
	// 				path: '/',
	// 				// secure: true,
	// 				httpOnly: true,
	// 				maxAge: store.state.auth.REFRESH_TOKEN_EXPIRE_TIME / 1000,
	// 			});
	// 		})
	// 		.catch(() => {
	// 			redirect('/login');
	// 		});
	// }

	// if (store.state.auth.authStatus !== 'success') {
	// 	redirect('/login');
	// }
}
