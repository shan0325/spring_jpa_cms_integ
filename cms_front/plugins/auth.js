export default function ({ $axios, store, redirect }) {
	console.log('plugins auth.js');

	// 새로고침 시 토큰 재발급
	// return store
	// 	.dispatch('auth/refreshtoken')
	// 	.then(data => {
	// 		console.log('accessToken reissue 성공');
	// 	})
	// 	.catch(() => {
	// 		redirect('/login');
	// 	});

	if (!store.state.auth.setTimeoutObj) {
		const timeoutObj = setTimeout(function () {
			store.dispatch('auth/refreshtoken').then(response => {
				console.log('accessToken reissue 성공');

				store.commit('auth/removeSetTimeoutObj');
			});
		}, 5000);
		store.commit('auth/setSetTimeoutObj', timeoutObj);
	}
}
