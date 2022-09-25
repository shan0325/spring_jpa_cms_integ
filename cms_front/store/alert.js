export const state = () => ({
	alert: false,
	alert_data: {
		type: 'primary', // type은 success, info, warning, error 이렇게 4가지를 쓸 수 있음
		title: '',
		text: '',
	},
});

export const mutations = {
	SET_ALERT(state, payload) {
		state.alert = payload.alert;
		state.alert_data.type = payload.type;
		state.alert_data.title = payload.title;
		state.alert_data.text = payload.text;
	},
};

export const getters = {
	GET_ALERT(state) {
		return state.alert;
	},
	GET_ALERT_DATA(state) {
		return state.alert_data;
	},
};

export const actions = {
	updateAlert({ commit }, params) {
		commit('SET_ALERT', params);
	},
};
