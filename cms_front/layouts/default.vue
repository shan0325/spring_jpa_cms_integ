<template>
	<v-app>
		<v-system-bar app>
			<v-btn icon href="/">{{ title }}</v-btn>
			<v-spacer></v-spacer>
			<v-btn icon @click="doLogout">
				<v-icon>mdi-exit-to-app</v-icon>
			</v-btn>
		</v-system-bar>

		<v-app-bar
			app
			clipped-right
			flat
			height="72"
			:class="{ 'left-56px': isOverlayNaviDrawer }"
		>
			<!-- <v-app-bar-nav-icon @click.stop="drawer = !drawer" /> -->
			<v-spacer></v-spacer>
			<v-responsive max-width="156">
				<v-text-field
					dense
					flat
					hide-details
					rounded
					solo-inverted
				></v-text-field>
			</v-responsive>
		</v-app-bar>

		<menu-navigation-drawer
			@setIsOverlayNaviDrawer="setIsOverlayNaviDrawer"
		/>

		<v-main :class="{ 'pl-14': isOverlayNaviDrawer }">
			<v-container>
				<Nuxt />
			</v-container>
		</v-main>

		<v-footer
			app
			color="transparent"
			height="72"
			inset
			:class="{ 'left-56px': isOverlayNaviDrawer }"
		>
			<v-text-field
				background-color="grey lighten-1"
				dense
				flat
				hide-details
				rounded
				solo
			></v-text-field>
		</v-footer>
	</v-app>
</template>

<script>
import MenuNavigationDrawer from '~/components/layout/MenuNavigationDrawer.vue';

export default {
	components: {
		MenuNavigationDrawer,
	},
	data: () => ({
		title: 'CMS',
		isOverlayNaviDrawer: true,
	}),
	methods: {
		setIsOverlayNaviDrawer(isOverlayNaviDrawer) {
			this.isOverlayNaviDrawer = isOverlayNaviDrawer;
		},
		doLogout() {
			this.$store.dispatch('auth/logout').then(response => {
				this.$router.push('/login');
			});
		},
	},
};
</script>

<style>
.left-56px {
	left: 56px !important;
}
</style>
