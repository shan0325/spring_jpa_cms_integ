<template>
	<v-app dark>
		<v-navigation-drawer
			v-model="drawer"
			:mini-variant="miniVariant"
			:clipped="clipped"
			fixed
			app
		>
			<v-list>
				<v-list-item
					v-for="(item, i) in items"
					:key="i"
					:to="item.to"
					router
					exact
				>
					<v-list-item-action>
						<v-icon>{{ item.icon }}</v-icon>
					</v-list-item-action>
					<v-list-item-content>
						<v-list-item-title v-text="item.title" />
					</v-list-item-content>
				</v-list-item>
			</v-list>
		</v-navigation-drawer>
		<v-app-bar :clipped-left="clipped" fixed app>
			<v-app-bar-nav-icon @click.stop="drawer = !drawer" />
			<v-btn icon @click.stop="miniVariant = !miniVariant">
				<v-icon
					>mdi-{{
						`chevron-${miniVariant ? 'right' : 'left'}`
					}}</v-icon
				>
			</v-btn>
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
			<v-btn v-if="!getMember" @click="$router.push('/login')">
				로그인
			</v-btn>
			<v-item-group v-else>
				<p class="d-inline-block font-weight-light">
					{{ getMember.name }}
				</p>
				<v-btn icon @click="doLogout">
					<v-icon>mdi-exit-to-app</v-icon>
				</v-btn>
			</v-item-group>

			<!-- <v-btn
        icon
        @click.stop="rightDrawer = !rightDrawer"
      >
        <v-icon>mdi-menu</v-icon>
      </v-btn> -->
		</v-app-bar>
		<v-main>
			<v-container>
				<Nuxt />
			</v-container>
		</v-main>
		<v-navigation-drawer
			v-model="rightDrawer"
			:right="right"
			temporary
			fixed
		>
			<v-list>
				<v-list-item @click.native="right = !right">
					<v-list-item-action>
						<v-icon light> mdi-repeat </v-icon>
					</v-list-item-action>
					<v-list-item-title
						>Switch drawer (click me)</v-list-item-title
					>
				</v-list-item>
			</v-list>
		</v-navigation-drawer>
		<v-footer :absolute="!fixed" app>
			<span>&copy; {{ new Date().getFullYear() }}</span>
		</v-footer>
	</v-app>
</template>

<script>
import { mapGetters } from 'vuex';
export default {
	name: 'DefaultLayout',
	data() {
		return {
			clipped: false,
			drawer: false,
			fixed: false,
			items: [
				{
					icon: 'mdi-home',
					title: 'Home',
					to: '/',
				},
				{
					icon: 'mdi-account-search',
					title: '회원검색',
					to: '/member',
				},
			],
			miniVariant: false,
			right: true,
			rightDrawer: false,
			title: 'CMS',
		};
	},
	computed: {
		...mapGetters({
			getMember: 'auth/getMember',
		}),
	},
	beforeCreate() {
		const BYPASS_LIST = this.$store.state.BYPASS_ROUTE_LIST;
		if (!BYPASS_LIST.includes(this.$route.path)) {
			if (this.$store.state.auth.authStatus !== 'success') {
				this.$router.push('/login');
			}
		}
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
