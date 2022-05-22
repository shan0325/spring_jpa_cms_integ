// 엄격모드 설정(true 시 mutation으로 상태값 변경해야함)
export const strict = true;

export const actions = {
	nuxtServerInit(
		{ state, commit, dispatch },
		{ redirect, $cookies, $axios, res },
	) {
		console.log('store index.js');

		// return dispatch('auth/refreshtoken')
		// 	.then(data => {
		// 		$cookies.set('refreshToken', data.refreshToken, {
		// 			path: '/',
		// 			// secure: true,
		// 			httpOnly: true,
		// 			maxAge: state.auth.REFRESH_TOKEN_EXPIRE_TIME / 1000,
		// 		});
		// 	})
		// 	.catch(() => {
		// 		redirect('/login');
		// 	});
	},
};
