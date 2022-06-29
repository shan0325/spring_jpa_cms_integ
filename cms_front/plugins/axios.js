export default function ({ $axios, store, redirect }) {
	$axios.onRequest(config => {
		const BYPASS_LIST = ['/api/auth/login', '/api/auth/silentReissue'];
		if (BYPASS_LIST.includes(config.url)) {
			return;
		}

		const accessToken = store.state.auth.accessToken;
		if (!accessToken) {
			redirect('/login');
		}
		config.headers.Authorization = `Bearer ${accessToken}`;
	});

	$axios.onResponse(response => {});

	// $axios.onError(error => {
	// 	const code = parseInt(error.response && error.response.status);
	// 	if (code === 400) {
	// 		redirect('/400'); // 400 에러 발생시 /400으로 리다이렉트
	// 	}
	// });
}
