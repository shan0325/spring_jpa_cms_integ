export default function ({ $axios, store, redirect }) {
	$axios.onRequest(config => {});

	// $axios.onError(error => {
	// 	const code = parseInt(error.response && error.response.status);
	// 	if (code === 400) {
	// 		redirect('/400'); // 400 에러 발생시 /400으로 리다이렉트
	// 	}
	// });
}
