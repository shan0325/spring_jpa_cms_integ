<template>
	<v-app>
		<v-system-bar app>
			<v-btn icon @click="$router.push('/')">{{ title }}</v-btn>
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

		<v-navigation-drawer
			v-model="drawer"
			app
			fixed
			permanent
			:hide-overlay="true"
			:width="naviDrawerWidth"
			:expand-on-hover="expandOnHover && childMenus.length === 0"
		>
			<div
				@mousemove="setInitIsSubNaviDrawerTempMini"
				@mouseleave="setInitIsSubNaviDrawerTempMini"
			>
				<v-navigation-drawer
					v-model="drawer"
					color="grey lighten-3"
					fixed
					permanent
					:width="subNaviDrawerWidth.value"
					:expand-on-hover="expandOnHover"
					:mini-variant.sync="subNaviDrawerMiniVariant"
					@update:mini-variant="updateSubNaviDrawerMiniVariant"
				>
					<v-list>
						<v-list-item class="px-2">
							<v-list-item-avatar>
								<v-img
									src="https://avatars.githubusercontent.com/u/18279501?v=4"
								></v-img>
							</v-list-item-avatar>

							<v-list-item-content class="py-0">
								<v-list-item-title class="text-h6">{{
									$store.state.auth.manager.username
								}}</v-list-item-title>
								<v-list-item-subtitle>{{
									$store.state.auth.manager.name
								}}</v-list-item-subtitle>
							</v-list-item-content>
						</v-list-item>

						<!-- <v-list-item link>
							<v-list-item-content>
								<v-list-item-title class="text-h6">{{
									$store.state.auth.manager.username
								}}</v-list-item-title>
								<v-list-item-subtitle>{{
									$store.state.auth.manager.name
								}}</v-list-item-subtitle>
							</v-list-item-content>
						</v-list-item> -->
					</v-list>

					<v-divider></v-divider>

					<v-list nav dense>
						<v-list-item
							link
							@click.stop="
								setSubNaviDrawerExpandOnHover(!expandOnHover)
							"
						>
							<v-list-item-icon>
								<!-- <v-icon v-if="expandOnHover">mdi-pin</v-icon>
								<v-icon v-else>mdi-pin-off</v-icon> -->
								<v-icon>mdi-menu</v-icon>
							</v-list-item-icon>
							<v-list-item-title></v-list-item-title>
						</v-list-item>

						<v-list-item-group v-model="topMenuId" mandatory>
							<v-list-item
								v-for="item in menus"
								:key="item.id"
								link
								@click.stop="setSubMenuList(item.id)"
							>
								<v-list-item-icon>
									<v-icon v-if="item.materialIcon"
										>mdi-{{ item.materialIcon }}</v-icon
									>
									<!-- <v-icon v-else>mdi-alpha-m-circle</v-icon> -->
									<v-icon v-else>mdi-circle-medium</v-icon>
								</v-list-item-icon>
								<v-list-item-title>{{
									item.name
								}}</v-list-item-title>
							</v-list-item>
						</v-list-item-group>
					</v-list>
				</v-navigation-drawer>
			</div>

			<div :class="getSubMenuListPaddingLeft">
				<v-sheet color="grey lighten-5" height="100" width="100%">
					<p class="pa-4 text-h6 font-weight-bold">
						{{ topMenuName }}
					</p>
				</v-sheet>

				<v-list shaped nav dense>
					<template v-for="(item, i) in childMenus">
						<template
							v-if="item.childMenus && item.childMenus.length > 0"
						>
							<v-list-group :key="i" no-action :value="true">
								<template #activator>
									<v-list-item-content>
										<v-list-item-title
											v-text="item.name"
										></v-list-item-title>
									</v-list-item-content>
								</template>
								<v-list-item
									v-for="(
										childItem, childI
									) in item.childMenus"
									:key="childI"
									link
									class="pl-7"
									@click="moveMenu(childItem)"
								>
									<v-list-item-title
										v-text="childItem.name"
									/>
								</v-list-item>
							</v-list-group>
						</template>
						<template v-else>
							<v-list-item :key="i" @click="moveMenu(item)">
								<v-list-item-content>
									<v-list-item-title v-text="item.name" />
								</v-list-item-content>
							</v-list-item>
						</template>
					</template>
				</v-list>
			</div>
		</v-navigation-drawer>

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
export default {
	data: () => ({
		title: 'CMS',
		drawer: true,
		expandOnHover: true,
		naviDrawerWidth: 220,
		subNaviDrawerWidth: {
			value: 220,
			expand: 220,
			mini: 56,
		},
		subMenuListWidth: 220,
		subNaviDrawerMiniVariant: null,
		menus: [],
		childMenus: [],
		topMenuName: '',
		isOverlayNaviDrawer: true,
		isSubNaviDrawerTempMini: false,
		topMenuId: '',
		menuTypeMovePath: {
			MT_BOARD: '/board',
			MT_CONTENTS: '/contents',
			MT_LINK: '/link',
		},
	}),
	computed: {
		getSubMenuListPaddingLeft() {
			return this.isSubNaviDrawerTempMini || this.expandOnHover
				? 'pl-14'
				: 'pl-' + this.subNaviDrawerWidth.expand + 'px';
		},
	},
	created() {
		this.getAdminMenus();
	},
	methods: {
		async getAdminMenus() {
			const data = await this.$store.dispatch('menu/getMenus', {
				menuGroupId: this.$ADMIN_MENU_GROUP_ID,
				managerId: this.$store.state.auth.manager.id,
			});
			this.menus = data;
		},
		setSubNaviDrawerExpandOnHover(expandOnHover) {
			this.expandOnHover = expandOnHover;

			if (!this.expandOnHover) {
				this.subNaviDrawerMiniVariant = false;
			}

			this.setNaviDrawerWidth();
		},
		setSubMenuList(findMenuId) {
			const findedMenu = this.menus.find(menu => menu.id === findMenuId);
			if (findedMenu.childMenus) {
				this.topMenuName = findedMenu.name;
				this.childMenus = findedMenu.childMenus;
			}

			this.setNaviDrawerWidth();

			if (this.expandOnHover) {
				this.expandOnHover = false;
				this.subNaviDrawerMiniVariant = true;
				this.isSubNaviDrawerTempMini = true;
			}
		},
		setNaviDrawerWidth() {
			if (this.isSubNaviDrawerTempMini) {
				this.naviDrawerWidth =
					this.subNaviDrawerWidth.mini +
					this.subNaviDrawerWidth.expand;
			} else if (this.childMenus.length === 0) {
				this.naviDrawerWidth = this.subNaviDrawerWidth.expand;
			} else {
				const width = this.expandOnHover
					? this.subNaviDrawerWidth.mini
					: this.subNaviDrawerWidth.expand;
				this.naviDrawerWidth = width + this.subMenuListWidth;
			}
			this.isOverlayNaviDrawer = false;
		},
		updateSubNaviDrawerMiniVariant(value) {
			this.isOverlayNaviDrawer = this.childMenus.length === 0;
		},
		setInitIsSubNaviDrawerTempMini() {
			if (this.isSubNaviDrawerTempMini) {
				this.expandOnHover = true;
				this.subNaviDrawerMiniVariant = false;
				this.isSubNaviDrawerTempMini = false;
			}
		},
		doLogout() {
			this.$store.dispatch('auth/logout').then(response => {
				this.$router.push('/login');
			});
		},
		moveMenu(menu) {
			let movePath = '';
			const menuType = menu.menuType;
			if (menuType === 'MT_MENU') {
				movePath = menu.viewPath;
			} else {
				movePath = this.menuTypeMovePath[menuType];
			}

			movePath = `${movePath}?menuId=${menu.id}`;
			this.$router.push(movePath);
		},
	},
};
</script>

<style>
.pl-220px {
	padding-left: 220px !important;
}
.left-56px {
	left: 56px !important;
}
</style>
