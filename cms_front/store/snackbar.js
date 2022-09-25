export const state = () => ({
	text: '',
	color: '',
	timeout: 0,
});

export const mutations = {
	SHOW_MESSAGE(state, payload) {
		state.text = payload.text;
		state.color = payload.color;
		state.timeout = payload.timeout || 2500;
	},
};

export const getters = {};

export const actions = {
	showSnack({ commit }, payload) {
		commit('SHOW_MESSAGE', payload);
	},
};
