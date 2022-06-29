export default function ({ $axios, store, redirect, app }) {
	console.log('plugins auth.js');

	// 새로고침 시 토큰 재발급
	// await store
	// 	.dispatch('auth/refreshtoken')
	// 	.then(response => {
	// 		console.log('accessToken reissue 성공');
	// 	})
	// 	.catch(error => {
	// 		console.log(error.message);
	// 	});
}
