export const state = () => ({
	naviMenus: null,
	topMenuId: null,
	childMenuId: null,
	selectedNaviMenus: [],
});

export const getters = {
	getTopMenuId: state => state.topMenuId,
	getChildMenuId: state => state.childMenuId,
	getSelectedNaviMenus: state => state.selectedNaviMenus,
};

export const mutations = {
	setNaviMenus(state, naviMenus) {
		state.naviMenus = naviMenus;
	},
	setTopMenuId(state, topMenuId) {
		state.topMenuId = topMenuId;
	},
	setChildMenuId(state, childMenuId) {
		state.childMenuId = childMenuId;
	},
	setSelectedNaviMenus(state, selectedNaviMenus) {
		state.selectedNaviMenus = selectedNaviMenus;
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
