export default function ({ store, $axios, cookie }) {
	console.log('middleware auth.js');

	console.log(
		'Authorization : ' + $axios.defaults.headers.common.Authorization,
	);

	console.log(store.getters['auth/getMember']);
}
