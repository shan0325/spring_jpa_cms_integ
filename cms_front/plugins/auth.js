export default async function ({ $axios, store, redirect }) {
	console.log('plugins auth.js');

	// 새로고침 시 토큰 재발급
	await store
		.dispatch('auth/refreshtoken')
		.then(data => {
			console.log('accessToken reissue 성공');
		})
		.catch(() => {
			redirect('/login');
		});
}
