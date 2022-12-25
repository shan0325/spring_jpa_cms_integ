<template>
	<v-main>
		<v-container fluid fill-height>
			<v-layout align-center justify-center>
				<v-flex xs12 sm8 md4>
					<v-card>
						<v-toolbar
							dark
							prominent
							src="https://picsum.photos/700/700"
						>
							<v-toolbar-title style="color: white"
								>CMS</v-toolbar-title
							>
							<v-spacer />
						</v-toolbar>
						<v-card-text>
							<v-form ref="loginForm">
								<v-text-field
									v-model="username"
									prepend-icon="mdi-account"
									:rules="[
										() =>
											!!username ||
											'아이디는 필수 입력입니다.',
									]"
									label="아이디"
									required
									autofocus
									@keydown.enter="doLogin"
								></v-text-field>
								<v-text-field
									v-model="password"
									prepend-icon="mdi-lock"
									:rules="[
										() =>
											!!password ||
											'비밀번호는 필수 입력입니다.',
									]"
									:append-icon="
										showPassword ? 'mdi-eye-off' : 'mdi-eye'
									"
									:type="showPassword ? 'text' : 'password'"
									label="비밀번호"
									counter
									required
									@keydown.enter="doLogin"
									@click:append="showPassword = !showPassword"
								></v-text-field>
							</v-form>
						</v-card-text>
						<v-card-actions>
							<v-btn
								block
								depressed
								large
								text
								color="primary"
								@click="doLogin"
								>Login</v-btn
							>
						</v-card-actions>
						<v-snackbar
							v-model="snackbar"
							:color="color"
							:top="true"
						>
							{{ errorMessages }}
							<template #action="{ attrs }">
								<v-btn
									color="red"
									text
									v-bind="attrs"
									@click="snackbar = false"
								>
									Close
								</v-btn>
							</template>
						</v-snackbar>
					</v-card>
				</v-flex>
			</v-layout>
		</v-container>
	</v-main>
</template>

<script>
export default {
	layout: 'login',
	data() {
		return {
			username: 'admin',
			password: '1234',
			errorMessages: 'Incorrect login info',
			snackbar: false,
			color: 'general',
			showPassword: false,
		};
	},
	methods: {
		doLogin() {
			const validate = this.$refs.loginForm.validate();
			if (!validate) return;

			this.$store
				.dispatch('auth/login', {
					username: this.username,
					password: this.password,
				})
				.then(async response => {
					await this.setNaviMenus();
				})
				.then(response => {
					this.$router.push('/');
				})
				.catch(error => {
					this.errorMessages = error.message;
					this.snackbar = true;
				});
		},
		async setNaviMenus() {
			const naviMenus = await this.$store.dispatch('menu/getMenus', {
				menuGroupId: this.$ADMIN_MENU_GROUP_ID,
				managerId: this.$store.state.auth.manager.id,
			});
			localStorage.setItem('naviMenus', JSON.stringify(naviMenus));
		},
	},
};
</script>
