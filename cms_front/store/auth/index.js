// 로그인 관련 참고 URL
// https://velog.io/@yaytomato/%ED%94%84%EB%A1%A0%ED%8A%B8%EC%97%90%EC%84%9C-%EC%95%88%EC%A0%84%ED%95%98%EA%B2%8C-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EC%B2%98%EB%A6%AC%ED%95%98%EA%B8%B0

export const state = () => ({
	authStatus: '',
	user: {},
});

export const getters = {
	getAuthorized: state => !!state.token,
	getAuthstatus: state => state.authStatus,
};

export const mutations = {
	setAuthStatusRequest: state => {
		state.authStatus = 'loading';
	},
	setAuthStatusSuccess(state) {
		state.authStatus = 'success';
	},
	setAuthStatusError(state) {
		state.authStatus = 'error';
	},
	setLogout(state) {
		state.authStatus = '';
	},
};

export const actions = {
	async login({ commit, dispatch }, payload) {
		commit('setAuthStatusRequest');

		try {
			const token = await this.$axios.$post('/api/auth/login', payload);
			dispatch('onAuthSuccess', token);
			return token;
		} catch (error) {
			commit('setAuthStatusError');

			const errorStatus = error.response.status;
			const errorData = error.response.data;
			if (errorStatus === 400) {
				if (errorData && errorData.apierror) {
					throw new Error(errorData.apierror.message);
				}
			} else if (errorStatus === 401) {
				throw new Error('인증에 실패하였습니다.');
			}
			throw new Error(
				'시스템 오류가 발생하였습니다. 잠시후 다시 시도해주세요',
			);
		}
	},
	logout({ commit }) {
		return new Promise(resolve => {
			delete this.$axios.defaults.headers.common.Authorization;
			commit('setLogout');
			resolve();
		});
	},
	async refreshtoken({ commit, dispatch }) {
		commit('setAuthStatusRequest');

		try {
			const token = await this.$axios.$post('/api/auth/silentReissue');
			dispatch('onAuthSuccess', token);
			return token;
		} catch (error) {
			commit('setAuthStatusError');

			const errorData = error.response.data;
			if (errorData && errorData.apierror) {
				throw new Error(errorData.apierror.message);
			}
			throw new Error(
				'시스템 오류가 발생하였습니다. 관리자에게 문의해주세요',
			);
		}
	},
	onAuthSuccess({ commit, dispatch }, token) {
		console.log(token);
		this.$axios.defaults.headers.common.Authorization = `Bearer ${token.accessToken}`;
		commit('setAuthStatusSuccess');

		// accessToken 만료하기 1분 전에 로그인 연장
		// setTimeout(function () {
		// 	dispatch('refreshtoken');
		// }, 1000 * 60 * 30 - 60000);
	},
	autoRefreshToken({ dispatch }) {
		setInterval(function () {
			dispatch('refreshtoken').then(response => {
				console.log(response);
			});
		}, 5000);
	},
};
