export const state = () => ({
	adminMenus: null,
});

export const getters = {};

export const mutations = {
	setAdminMenus(state, adminMenus) {
		state.adminMenus = adminMenus;
	},
};

export const actions = {
	async getMenus({ commit, dispatch }, payload) {
		try {
			const data = await this.$axios.$get(
				`/api/menus/menu-group/${payload.menuGroupId}/manager/${payload.managerId}`,
			);

			commit('setAdminMenus', data);

			return data;
		} catch (e) {}
	},
};
