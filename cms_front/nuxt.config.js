import colors from 'vuetify/es5/util/colors';

export default {
	// Global page headers: https://go.nuxtjs.dev/config-head
	head: {
		titleTemplate: '%s - cms_front',
		title: 'cms_front',
		htmlAttrs: {
			lang: 'en',
		},
		meta: [
			{ charset: 'utf-8' },
			{
				name: 'viewport',
				content: 'width=device-width, initial-scale=1',
			},
			{ hid: 'description', name: 'description', content: '' },
			{ name: 'format-detection', content: 'telephone=no' },
		],
		link: [{ rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' }],
	},

	// Global CSS: https://go.nuxtjs.dev/config-css
	css: [],

	// Plugins to run before rendering page: https://go.nuxtjs.dev/config-plugins
	// 플러그인은 첫진입 또는 새로고침 시 Server, Client 실행되고 페이지 이동은 실행 안됨
	plugins: [
		{ src: '~/plugins/constants.js' },
		{ src: '~/plugins/vue-cookies.js' },
		{ src: '~/plugins/axios.js' },
		{ src: '~/plugins/vee-validate.js' },
	],

	// Auto import components: https://go.nuxtjs.dev/config-components
	components: true,

	// Modules for dev and build (recommended): https://go.nuxtjs.dev/config-modules
	buildModules: [
		// https://go.nuxtjs.dev/eslint
		'@nuxtjs/eslint-module',
		// https://go.nuxtjs.dev/vuetify
		'@nuxtjs/vuetify',
		'@nuxtjs/dotenv',
	],

	// Modules: https://go.nuxtjs.dev/config-modules
	modules: [
		// https://go.nuxtjs.dev/axios
		'@nuxtjs/axios',
		'@nuxtjs/proxy',
		'@nuxtjs/moment',
		'@nuxtjs/universal-storage',
		'cookie-universal-nuxt',
	],

	// Axios module configuration: https://go.nuxtjs.dev/config-axios
	axios: {
		// Workaround to avoid enforcing hard-coded localhost:3000: https://github.com/nuxt-community/axios-module/issues/308
		// baseURL: 'http://localhost:8080',
		credentials: true,
		proxy: false,
		// retry: {
		// 	retries: 4, // 최대 재전송 횟수 4회
		// 	shouldResetTimeout: true, // 재전송 간 타임아웃을 리셋하기
		// 	retryDelay: retry => {
		// 		return retry * 100; // 재전송 횟수 * 0.1초만큼 재전송 시작 시간을 지연시키기
		// 	},
		// 	retryCondition: error => error.response.status === 429, // 서버 혼잡이 일어났을 경우에만 재전송하기
		// },
		// progress: false, // 로딩 바를 사용하지 않음
	},
	proxy: {
		'/api': {
			target: 'http://localhost:8080',
			changeOrigin: true,
			// pathRewrite: {
			// 	'^/api': '', // URL ^/api -> 공백 변경
			// },
		},
	},

	// Vuetify module configuration: https://go.nuxtjs.dev/config-vuetify
	vuetify: {
		customVariables: ['~/assets/variables.scss'],
		theme: {
			dark: false,
			themes: {
				dark: {
					primary: colors.blue.darken2,
					accent: colors.grey.darken3,
					secondary: colors.amber.darken3,
					info: colors.teal.lighten1,
					warning: colors.amber.base,
					error: colors.deepOrange.accent4,
					success: colors.green.accent3,
				},
			},
		},
	},

	// Build Configuration: https://go.nuxtjs.dev/config-build
	build: {
		transpile: ['vee-validate/dist/rules'],
	},

	// middleware는 첫진입 또는 새로고침 시 Server가 실행되고 페이지 이동 시 Client가 실행됨
	router: {
		middleware: ['auth', 'menu-auth'],
	},
};
