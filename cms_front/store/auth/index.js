// 로그인 관련 참고 URL
// https://velog.io/@yaytomato/%ED%94%84%EB%A1%A0%ED%8A%B8%EC%97%90%EC%84%9C-%EC%95%88%EC%A0%84%ED%95%98%EA%B2%8C-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EC%B2%98%EB%A6%AC%ED%95%98%EA%B8%B0
// https://medium.com/@d971106b/%EC%82%BD%EC%A7%88-%EA%B8%B0%EB%A1%9D-1-auth-%EC%A0%81%EC%9A%A9-axios-default-header-%EC%B6%94%EA%B0%80-plugin-%EC%B6%94%EA%B0%80-a15d0beba330

import jwtDecode from 'jwt-decode';

export const state = () => ({
	ACCESS_TOKEN_EXPIRE_TIME: 1000 * 60 * 30, // 30분
	REFRESH_TOKEN_EXPIRE_TIME: 1000 * 60 * 60, // 60분
	authStatus: '',
	manager: '',
});

export const getters = {
	getAuthstatus: state => state.authStatus,
	getManager: state => state.manager,
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
		state.manager = '';
	},
	setLogout(state) {
		state.authStatus = '';
		state.manager = '';
	},
	setManager(state, manager) {
		state.manager = manager;
	},
	removeManager(state) {
		state.manager = '';
	},
};

export const actions = {
	async login({ commit, dispatch }, payload) {
		commit('setAuthStatusRequest');

		try {
			const token = await this.$axios.$post('/api/auth/login', payload);
			this.$axios.defaults.headers.common.Authorization = `Bearer ${token.accessToken}`;

			await dispatch('setManager', token);
			await dispatch('onAuthSuccess', token);

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
	async logout({ commit }) {
		try {
			await this.$axios.$get('/api/auth/logout');
		} catch (error) {}
		delete this.$axios.defaults.headers.common.Authorization;
		commit('setLogout');
	},
	async refreshtoken({ commit, dispatch }, payload) {
		commit('setAuthStatusRequest');

		try {
			const token = await this.$axios.$post('/api/auth/silentReissue');
			this.$axios.defaults.headers.common.Authorization = `Bearer ${token.accessToken}`;

			await dispatch('setManager', token);
			await dispatch('onAuthSuccess', token);

			return token;
		} catch (error) {
			commit('setAuthStatusError');

			const errorData = error.response.data;
			if (errorData && errorData.apierror) {
				throw new Error(errorData.apierror.message);
			} else {
				throw new Error(
					'시스템 오류가 발생하였습니다. 잠시후 다시 시도해주세요',
				);
			}
		}
	},
	async setManager({ commit }, token) {
		try {
			const decodedToken = jwtDecode(token.accessToken);

			const manager = await this.$axios.$get(
				`/api/manager/auth/${decodedToken.sub}`,
			);
			commit('setManager', manager);

			return manager;
		} catch (error) {
			commit('removeManager');
			throw error;
		}
	},
	onAuthSuccess({ commit, dispatch, state }, token) {
		// accessToken 만료하기 1분 전에 로그인 연장(state.ACCESS_TOKEN_EXPIRE_TIME - 60000)
		setTimeout(function () {
			dispatch('refreshtoken')
				.then(response => {
					console.log('accessToken reissue 성공');
				})
				.catch(error => {
					console.log(error.message);
				});
		}, state.ACCESS_TOKEN_EXPIRE_TIME - 60000);

		commit('setAuthStatusSuccess');
	},
};
