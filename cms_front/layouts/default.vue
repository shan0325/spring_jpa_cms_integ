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
					@update:mini-variant="setIsOverlayNaviDrawer"
				>
					<v-list>
						<v-list-item class="px-2 py-1">
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

						<v-list-item-group
							v-model="topMenuId"
							:mandatory="topMandatory"
						>
							<v-list-item
								v-for="item in menus"
								:key="item.id"
								:value="item.id"
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
					<div v-for="item in childMenus" :key="item.id">
						<v-list-group
							v-if="item.childMenus && item.childMenus.length > 0"
							v-model="item.active"
							no-action
						>
							<template #activator>
								<v-list-item-title v-text="item.name" />
							</template>
							<div
								v-for="childItem in item.childMenus"
								:key="childItem.id"
							>
								<v-list-item
									:id="`childMenu_${childItem.id}`"
									class="pl-7"
									:class="{
										highlighted:
											childItem.id === childMenuId,
									}"
									@click.stop="
										moveMenuSetDepthMenu(childItem)
									"
								>
									<v-list-item-title
										v-text="childItem.name"
									/>
								</v-list-item>
							</div>
						</v-list-group>
						<v-list-item
							v-else
							:id="`childMenu_${item.id}`"
							:class="{
								highlighted: item.id === childMenuId,
							}"
							@click.stop="moveMenuSetDepthMenu(item)"
						>
							<v-list-item-title v-text="item.name" />
						</v-list-item>
					</div>
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
		naviDrawerWidth: 200,
		subNaviDrawerWidth: {
			value: 200,
			expand: 200,
			mini: 56,
		},
		subMenuListWidth: 220,
		subNaviDrawerMiniVariant: null,
		menus: [],
		childMenus: [],
		topMenuName: '',
		isOverlayNaviDrawer: true,
		isSubNaviDrawerTempMini: false,
		topMenuId: null,
		childMenuId: null,
		childGroupExpands: {},
		childGroupExpands2: {},
		childGroupExpand: null,
		selectedNaviMenus: {},
		menuTypeMovePath: {
			MT_BOARD: '/board',
			MT_CONTENTS: '/contents',
			MT_LINK: '/link',
		},
		topMandatory: false,
	}),
	computed: {
		getSubMenuListPaddingLeft() {
			return this.isSubNaviDrawerTempMini || this.expandOnHover
				? 'pl-14'
				: 'pl-' + this.subNaviDrawerWidth.expand + 'px';
		},
	},
	created() {},
	async mounted() {
		await this.setExpandOnHoverByCookie();
		await this.getAdminMenus();
		await this.setSelectNaviDrawer();
	},
	methods: {
		async getAdminMenus() {
			let naviMenus = localStorage.getItem('naviMenus');
			if (naviMenus) {
				naviMenus = JSON.parse(naviMenus);

				// TODO 버전 체크해서 다시 가져올지 체크
			} else {
				naviMenus = await this.$store.dispatch('menu/getMenus', {
					menuGroupId: this.$ADMIN_MENU_GROUP_ID,
					managerId: this.$store.state.auth.manager.id,
				});
				localStorage.setItem('naviMenus', JSON.stringify(naviMenus));
			}
			this.menus = naviMenus;
		},
		setSelectNaviDrawer() {
			if (!this.menus) return;

			const firstDepth = 1;
			const paramMenuId = Number(this.$route.query.menuId);
			const result = this.setSelectedNaviMenusRecursive(
				firstDepth,
				this.menus,
				paramMenuId,
			);
			if (!result) {
				this.selectedNaviMenus = {};
				this.setNaviDrawerWidth();
				this.setIsOverlayNaviDrawer();
				return;
			}

			this.topMenuId = this.selectedNaviMenus.depth1.id;
			this.setSubMenuList(this.selectedNaviMenus.depth1.id);

			this.childMenuId = this.selectedNaviMenus.depth3
				? this.selectedNaviMenus.depth3.id
				: this.selectedNaviMenus.depth2.id;
		},
		setExpandOnHoverByCookie() {
			const expandOnHoverCookie = this.$cookies.get('expandOnHover');
			if (expandOnHoverCookie) {
				this.expandOnHover = JSON.parse(expandOnHoverCookie);
			}
		},
		setSelectedNaviMenusRecursive(depth, menus, findMenuId) {
			if (!menus) return false;

			for (let i = 0; i < menus.length; i++) {
				const menu = menus[i];
				this.setSelectedNaviMenus(depth, menu);

				if (menu.id === findMenuId) return true;

				const isEnd = this.setSelectedNaviMenusRecursive(
					depth + 1,
					menu.childMenus,
					findMenuId,
				);
				if (isEnd) {
					menu.active = true;
					return true;
				}
			}
			return false;
		},
		setSelectedNaviMenus(depth, menu) {
			if (depth === 1) {
				this.selectedNaviMenus = {};
			} else if (depth === 2) {
				this.selectedNaviMenus.depth3 = null;
			}

			if (!menu) {
				this.selectedNaviMenus[`depth${depth}`] = null;
				return;
			}

			this.selectedNaviMenus[`depth${depth}`] = {
				id: menu.id,
				name: menu.name,
			};
		},
		setSubNaviDrawerExpandOnHover(expandOnHover) {
			this.expandOnHover = expandOnHover;

			// 쿠키에 expandOnHover 값 설정
			this.$cookies.set('expandOnHover', expandOnHover, '365d');

			if (!this.expandOnHover) {
				this.subNaviDrawerMiniVariant = false;
			}

			this.setNaviDrawerWidth();
			this.setIsOverlayNaviDrawer();
		},
		setSubMenuList(findMenuId) {
			const findedMenu = this.menus.find(menu => menu.id === findMenuId);
			if (findedMenu && findedMenu.childMenus) {
				this.topMenuName = findedMenu.name;
				this.childMenus = findedMenu.childMenus;
			}
			this.setNaviDrawerMandatory(findMenuId);
			this.setNaviDrawerWidth();
			this.setIsOverlayNaviDrawer();

			if (this.expandOnHover) {
				this.expandOnHover = false;
				this.subNaviDrawerMiniVariant = true;
				this.isSubNaviDrawerTempMini = true;
			}
		},
		setNaviDrawerMandatory(topMenuId) {
			this.topMandatory = true;

			if (
				this.selectedNaviMenus &&
				Object.keys(this.selectedNaviMenus).length > 0 &&
				this.selectedNaviMenus.depth1.id === topMenuId
			) {
				this.childMenuId = Number(this.$route.query.menuId);
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
		},
		setIsOverlayNaviDrawer(value) {
			if (this.expandOnHover && this.childMenus.length === 0) {
				this.isOverlayNaviDrawer = true;
			} else {
				this.isOverlayNaviDrawer = false;
			}
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
		moveMenuSetDepthMenu(menu) {
			this.moveMenu(menu);

			this.childMenuId = menu.id;

			// const depth1Menu = this.menus.find(
			// 	menu => menu.id === this.topMenuId,
			// );
			// this.setSelectedNaviMenus(1, depth1Menu);

			// const depth = 2;
			// this.setSelectedNaviMenusRecursive(
			// 	depth,
			// 	depth1Menu.childMenus,
			// 	menu.id,
			// );
		},
		moveMenu(menu) {
			let movePath = '';
			const menuType = menu.menuType;
			if (menuType === 'MT_MENU') {
				movePath = menu.viewPath;
			} else {
				movePath = this.menuTypeMovePath[menuType];
			}
			this.$router.push(`${movePath}?menuId=${menu.id}`);
		},
		getLink(menu) {
			let movePath = '';
			const menuType = menu.menuType;
			if (menuType === 'MT_MENU') {
				movePath = menu.viewPath;
			} else {
				movePath = this.menuTypeMovePath[menuType];
			}
			return {
				to: `${movePath}/${menu.id}`,
			};
		},
	},
};
</script>

<style>
.pl-200px {
	padding-left: 200px !important;
}
.left-56px {
	left: 56px !important;
}
.highlighted {
	color: red !important;
}
</style>
