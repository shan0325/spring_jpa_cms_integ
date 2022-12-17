export const state = () => ({
	naviMenus: null,
});

export const getters = {};

export const mutations = {
	setNaviMenus(state, naviMenus) {
		state.naviMenus = naviMenus;
	},
};

export const actions = {
	async getMenus({ commit, dispatch }, payload) {
		try {
			const data = await this.$axios.$get(
				`/api/menus/navi-menus?menuGroupId=${payload.menuGroupId}&managerId=${payload.managerId}`,
			);

			commit('setNaviMenus', data);

			return data;
		} catch (e) {}
	},
};
