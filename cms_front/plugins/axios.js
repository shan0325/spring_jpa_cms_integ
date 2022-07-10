// axios interceptors 토큰 재발급 참고 URL
// https://maruzzing.github.io/study/rnative/axios-interceptors%EB%A1%9C-%ED%86%A0%ED%81%B0-%EB%A6%AC%ED%94%84%EB%A0%88%EC%8B%9C-%ED%95%98%EA%B8%B0/

export default function ({ $axios, store, redirect }) {
	let isTokenRefreshing = false;
	let refreshSubscribers = [];

	const onTokenRefreshed = accessToken => {
		refreshSubscribers.map(callback => callback(accessToken));
		refreshSubscribers = [];
	};

	const addRefreshSubscriber = callback => {
		refreshSubscribers.push(callback);
	};

	const retryOriginalRequests = originalRequest => {
		return new Promise(resolve => {
			addRefreshSubscriber(accessToken => {
				originalRequest.headers.Authorization = `Bearer ${accessToken}`;
				resolve($axios(originalRequest));
			});
		});
	};

	$axios.onRequest(config => {
		// const BYPASS_LIST = ['/api/auth/login', '/api/auth/silentReissue'];
		// if (BYPASS_LIST.includes(config.url)) {
		// 	return;
		// }
		// const accessToken = store.state.auth.accessToken;
		// if (!accessToken) {
		// 	redirect('/login');
		// }
		// config.headers.Authorization = `Bearer ${accessToken}`;
	});

	$axios.onResponse(response => {});

	$axios.onError(async error => {
		const {
			config,
			response: { status, data },
		} = error;
		const originalRequest = config;

		console.log(error.response);

		if (status === 401) {
			if (data.apierror && data.apierror.errorCode === 'EXPIRED_JWT') {
				if (!isTokenRefreshing) {
					// isTokenRefreshing이 false인 경우에만 token refresh 요청
					isTokenRefreshing = true;
					try {
						const refreshData = await store.dispatch(
							'auth/refreshtoken',
						);

						isTokenRefreshing = false;
						// $axios.defaults.headers.common.Authorization = `Bearer ${refreshData.accessToken}`;

						const retryOriginalRequest =
							retryOriginalRequests(originalRequest);

						// 새로운 토큰으로 지연되었던 요청 진행
						onTokenRefreshed(refreshData.accessToken);
						return retryOriginalRequest;
					} catch (error) {
						isTokenRefreshing = false;
						redirect('/login');
					}
				}

				// token이 재발급 되는 동안의 요청은 refreshSubscribers에 저장
				return retryOriginalRequests(originalRequest);
			} else {
				isTokenRefreshing = false;

				console.log('move login');
				redirect('/login');
			}
		}
		return Promise.reject(error);
	});
}
