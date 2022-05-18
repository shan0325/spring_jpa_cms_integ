// 로그인 관련 참고 URL
// https://velog.io/@yaytomato/%ED%94%84%EB%A1%A0%ED%8A%B8%EC%97%90%EC%84%9C-%EC%95%88%EC%A0%84%ED%95%98%EA%B2%8C-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EC%B2%98%EB%A6%AC%ED%95%98%EA%B8%B0

import jwtDecode from 'jwt-decode';

export const state = () => ({
	ACCESS_TOKEN_EXPIRE_TIME: 1000 * 60 * 30, // 30분
	REFRESH_TOKEN_EXPIRE_TIME: 1000 * 60 * 60, // 60분
	authStatus: '',
	member: '',
});

export const getters = {
	getAuthstatus: state => state.authStatus,
	getMember: state => state.member,
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
		state.member = '';
	},
	setMember(state, member) {
		state.member = member;
	},
	removeMember(state) {
		state.member = '';
	},
};

export const actions = {
	async login({ commit, dispatch }, payload) {
		commit('setAuthStatusRequest');

		try {
			const token = await this.$axios.$post('/api/auth/login', payload);
			return dispatch('onAuthSuccess', token);
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

			delete this.$axios.defaults.headers.common.Authorization;
			commit('setLogout');
		} catch (error) {}
	},
	async refreshtoken({ commit, dispatch }) {
		commit('setAuthStatusRequest');

		try {
			const token = await this.$axios.$post('/api/auth/silentReissue');
			return dispatch('onAuthSuccess', token);
		} catch (error) {
			commit('setAuthStatusError');
			commit('removeMember');

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
	async onAuthSuccess({ commit, dispatch, state }, token) {
		this.$axios.defaults.headers.common.Authorization = `Bearer ${token.accessToken}`;

		// TODO 회원정보 가져오기
		const tokenDecoded = jwtDecode(token.accessToken);

		const member = await this.$axios.$get(
			`/api/members/auth/${tokenDecoded.sub}`,
		);
		commit('setMember', member);
		commit('setAuthStatusSuccess');

		// accessToken 만료하기 1분 전에 로그인 연장
		setTimeout(function () {
			dispatch('refreshtoken').then(response => {
				console.log('accessToken reissue 성공');
			});
		}, 5000);

		return token;
	},
};
