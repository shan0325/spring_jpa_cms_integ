<template>
	<v-app style="background: #fafafa">
		<v-navigation-drawer
			v-model="drawer"
			:mini-variant="miniVariant"
			:clipped="clipped"
			fixed
			app
		>
			<v-list>
				<template v-for="(item, i) in items">
					<template v-if="item.children">
						<v-list-group
							:key="i"
							:prepend-icon="item.icon"
							no-action
						>
							<template #activator>
								<v-list-item-content>
									<v-list-item-title
										v-text="item.title"
									></v-list-item-title>
								</v-list-item-content>
							</template>
							<v-list-item
								v-for="(childItem, childI) in item.children"
								:key="childI"
								:to="childItem.to"
							>
								<v-list-item-content>
									<v-list-item-title
										v-text="childItem.title"
									/>
								</v-list-item-content>
							</v-list-item>
						</v-list-group>
					</template>
					<template v-else>
						<v-list-item :key="i" :to="item.to" router exact>
							<v-list-item-action>
								<v-icon>{{ item.icon }}</v-icon>
							</v-list-item-action>
							<v-list-item-content>
								<v-list-item-title v-text="item.title" />
							</v-list-item-content>
						</v-list-item>
					</template>
				</template>
			</v-list>
		</v-navigation-drawer>
		<v-app-bar :clipped-left="clipped" fixed app>
			<v-app-bar-nav-icon @click.stop="drawer = !drawer" />
			<!-- <v-btn icon @click.stop="miniVariant = !miniVariant">
				<v-icon
					>mdi-{{
						`chevron-${miniVariant ? 'right' : 'left'}`
					}}</v-icon
				>
			</v-btn> -->
			<v-btn icon @click="$router.push('/')">
				<v-icon>mdi-home</v-icon>
			</v-btn>
			<!-- <v-btn icon @click.stop="clipped = !clipped">
				<v-icon>mdi-application</v-icon>
			</v-btn> -->
			<!-- <v-btn icon @click.stop="fixed = !fixed">
				<v-icon>mdi-minus</v-icon>
			</v-btn> -->
			<v-toolbar-title v-text="title" />
			<v-spacer />
			<v-btn
				v-if="!$store.state.auth.manager"
				@click="$router.push('/login')"
			>
				로그인
			</v-btn>
			<v-item-group v-else>
				<p class="d-inline-block font-weight-light">
					{{ $store.state.auth.manager.name }}
				</p>
				<v-btn icon @click="doLogout">
					<v-icon>mdi-exit-to-app</v-icon>
				</v-btn>
			</v-item-group>
		</v-app-bar>
		<alert-message />
		<snackbar-message />
		<v-main>
			<v-container>
				<Nuxt />
			</v-container>
		</v-main>
		<v-footer :absolute="!fixed" app>
			<span>&copy; {{ new Date().getFullYear() }}</span>
		</v-footer>
	</v-app>
</template>

<script>
import { mapGetters } from 'vuex';
import AlertMessage from '~/components/common/AlertMessage.vue';
import SnackbarMessage from '~/components/common/SnackbarMessage.vue';
export default {
	name: 'DefaultLayout',
	components: {
		AlertMessage,
		SnackbarMessage,
	},
	data() {
		return {
			clipped: true,
			drawer: false,
			fixed: true,
			items: [
				{
					icon: 'mdi-account-search',
					title: '회원',
					children: [
						{
							title: '회원 검색',
							to: '/member',
						},
					],
				},
				{
					icon: 'mdi-laptop',
					title: '시스템',
					children: [
						{
							title: '코드 설정',
							to: '/system/code',
						},
						// {
						// 	title: '카테고리 설정',
						// 	to: '/system/category',
						// },
					],
				},
			],
			miniVariant: false,
			title: 'CMS',
		};
	},
	computed: {
		...mapGetters({}),
	},
	methods: {
		doLogout() {
			this.$store.dispatch('auth/logout').then(response => {
				this.$router.push('/login');
			});
		},
	},
};
</script>
