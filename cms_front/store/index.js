// 엄격모드 설정(true 시 mutation으로 상태값 변경해야함)
export const strict = true;

export const actions = {
	nuxtServerInit(
		{ state, commit, dispatch },
		{ redirect, $cookies, $axios, res },
	) {},
};
