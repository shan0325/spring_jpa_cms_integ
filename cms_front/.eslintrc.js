module.exports = {
	root: true,
	env: {
		browser: true,
		node: true,
	},
	parserOptions: {
		parser: '@babel/eslint-parser',
		requireConfigFile: false,
	},
	extends: ['@nuxtjs', 'plugin:nuxt/recommended', 'prettier'],
	plugins: [],
	// add your custom rules here
	rules: {
		'prettier/prettier': [
			'error',
			{
				endOfLine: 'auto',
			},
		],
		'vue/no-v-html': 'off',
		'vue/no-unused-components': 'off',
		'vue/multi-word-component-names': [
			'error',
			{
				ignores: ['default', 'login'],
			},
		],
	},
};
