export const state = () => ({
	naviMenus: null,
	currentMenuId: null,
});

export const getters = {};

export const mutations = {
	setNaviMenus(state, naviMenus) {
		state.naviMenus = naviMenus;
	},
	setCurrentMenuId(state, currentMenuId) {
		state.currentMenuId = currentMenuId;
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
