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
			<v-app-bar-title class="text-h6 font-weight-bold">{{
				subMenuTitle
			}}</v-app-bar-title>
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
		>
			<v-navigation-drawer
				v-model="drawer"
				color="grey lighten-3"
				fixed
				permanent
				:width="subNaviDrawerWidth.default"
				:expand-on-hover="expandOnHover"
				:mini-variant.sync="isSubNaviDrawerMini"
				@update:mini-variant="updateSubNaviDrawerMiniVariant"
			>
				<v-list>
					<v-list-item class="px-2">
						<v-list-item-avatar>
							<v-img
								src="https://avatars.githubusercontent.com/u/18279501?v=4"
							></v-img>
						</v-list-item-avatar>
					</v-list-item>

					<v-list-item link>
						<v-list-item-content>
							<v-list-item-title class="text-h6">{{
								$store.state.auth.manager.username
							}}</v-list-item-title>
							<v-list-item-subtitle>{{
								$store.state.auth.manager.name
							}}</v-list-item-subtitle>
						</v-list-item-content>
					</v-list-item>
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
							<v-icon v-if="expandOnHover">mdi-pin</v-icon>
							<v-icon v-else>mdi-pin-off</v-icon>
						</v-list-item-icon>
						<v-list-item-title></v-list-item-title>
					</v-list-item>

					<v-list-item
						v-for="item in menus"
						:key="item.seq"
						link
						@click="setSubMenuList(item.seq)"
					>
						<v-list-item-icon>
							<v-icon>{{ item.icon }}</v-icon>
						</v-list-item-icon>
						<v-list-item-title>{{ item.title }}</v-list-item-title>
					</v-list-item>
				</v-list>
			</v-navigation-drawer>

			<div :class="getSubMenuListPaddingLeft">
				<v-sheet color="grey lighten-5" height="100" width="100%">
					<p class="pa-4 text-h6 font-weight-bold">
						{{ topMenuTitle }}
					</p>
				</v-sheet>

				<v-list shaped nav dense>
					<template v-for="(item, i) in subMenus">
						<template v-if="item.children">
							<v-list-group :key="i" no-action :value="true">
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
									link
									class="pl-7"
									@click="goMenu(childItem)"
								>
									<v-list-item-title
										v-text="childItem.title"
									/>
								</v-list-item>
							</v-list-group>
						</template>
						<template v-else>
							<v-list-item :key="i" @click="goMenu(item)">
								<v-list-item-content>
									<v-list-item-title v-text="item.title" />
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
		naviDrawerWidth: 0,
		subNaviDrawerWidth: {
			default: 220,
			mini: 56,
		},
		subMenuListWidth: 220,
		isSubNaviDrawerMini: null,
		menus: [
			{
				seq: 1,
				icon: 'mdi-account-search',
				title: 'Member',
				children: [
					{
						title: '회원 검색',
						to: '/member',
					},
				],
			},
			{
				seq: 2,
				icon: 'mdi-laptop',
				title: 'System',
				children: [
					{
						title: '메뉴 설정',
						to: '/system/menu',
					},
					{
						title: '코드 설정',
						to: '/system/code',
					},
				],
			},
		],
		subMenus: [],
		topMenuTitle: '',
		subMenuTitle: '',
		isOverlayNaviDrawer: true,
	}),
	computed: {
		getSubMenuListPaddingLeft() {
			return this.expandOnHover
				? 'pl-14'
				: 'pl-' + this.subNaviDrawerWidth.default + 'px';
		},
	},
	created() {
		this.getAdminMenus();
		this.setNaviDrawerWidth();
	},
	methods: {
		async getAdminMenus() {
			const data = await this.$store.dispatch('menu/getMenus', {
				menuGroupId: this.$ADMIN_MENU_GROUP_ID,
			});

			console.log(data);
		},
		setSubNaviDrawerExpandOnHover(expandOnHover) {
			this.expandOnHover = expandOnHover;
			this.isOverlayNaviDrawer = false;
			this.setNaviDrawerWidth();
		},
		setNaviDrawerWidth() {
			if (this.subMenus.length === 0) {
				this.naviDrawerWidth = !this.expandOnHover
					? this.subNaviDrawerWidth.default
					: this.subNaviDrawerWidth.mini;
			} else if (!this.expandOnHover) {
				this.naviDrawerWidth =
					this.subNaviDrawerWidth.default + this.subMenuListWidth;
			} else {
				this.naviDrawerWidth =
					this.subNaviDrawerWidth.mini + this.subMenuListWidth;
			}
		},
		setSubMenuList(findMenuSeq) {
			if (this.menus && this.menus.length > 0) {
				const findedMenu = this.menus.find(
					menu => menu.seq === findMenuSeq,
				);

				if (findedMenu.children) {
					this.topMenuTitle = findedMenu.title;
					this.subMenus = findedMenu.children;
				}
			}
			this.setNaviDrawerWidth();
		},
		doLogout() {
			this.$store.dispatch('auth/logout').then(response => {
				this.$router.push('/login');
			});
		},
		updateSubNaviDrawerMiniVariant(value) {
			console.log(value);

			this.isOverlayNaviDrawer =
				this.expandOnHover && this.subMenus.length === 0;

			console.log(this.isOverlayNaviDrawer);

			if (!value) {
				this.naviDrawerWidth =
					this.subMenus.length === 0
						? this.subNaviDrawerWidth.default
						: this.subNaviDrawerWidth.mini + this.subMenuListWidth;
			} else {
				this.naviDrawerWidth =
					this.subMenus.length === 0
						? this.subNaviDrawerWidth.mini
						: this.subNaviDrawerWidth.mini + this.subMenuListWidth;
			}
		},
		goMenu(menu) {
			this.subMenuTitle = menu.title;
			this.$router.push(menu.to);
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
