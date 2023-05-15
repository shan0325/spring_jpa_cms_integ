<template>
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
				<nested-child-menu :menus="childMenus" />
			</v-list>
		</div>
	</v-navigation-drawer>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex';
import lodash from 'lodash';
import NestedChildMenu from '~/components/layout/NestedChildMenu.vue';

export default {
	components: {
		NestedChildMenu,
	},
	data: () => ({
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
		isSubNaviDrawerTempMini: false,
		menuDepthIndex: {
			depth1: 0,
			depth2: 1,
			depth3: 2,
		},
		topMandatory: false,
	}),
	computed: {
		...mapGetters({
			getTopMenuId: 'menu/getTopMenuId',
			getChildMenuId: 'menu/getChildMenuId',
		}),
		topMenuId: {
			get() {
				return this.getTopMenuId;
			},
			set(value) {
				return this.setTopMenuId(value);
			},
		},
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
		...mapMutations({
			setTopMenuId: 'menu/setTopMenuId',
			setChildMenuId: 'menu/setChildMenuId',
			setStoreSelectedNaviMenus: 'menu/setSelectedNaviMenus',
		}),
		async getAdminMenus() {
			let naviMenus = localStorage.getItem('naviMenus');
			if (naviMenus) {
				naviMenus = JSON.parse(naviMenus);
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

			const copyMenus = lodash.cloneDeep(this.menus);
			const firstDepth = 1;
			const paramMenuId = Number(this.$route.query.menuId);
			const result = this.setSelectedNaviMenusRecursive(
				firstDepth,
				copyMenus,
				paramMenuId,
			);
			if (!result) {
				this.setStoreSelectedNaviMenus([]);
				this.setNaviDrawerWidth();
				this.setIsOverlayNaviDrawer();
				return;
			}
			this.menus = copyMenus;

			const selectedNaviMenus =
				this.$store.getters['menu/getSelectedNaviMenus'];
			this.setTopMenuId(selectedNaviMenus[this.menuDepthIndex.depth1].id);
			this.setSubMenuList(
				selectedNaviMenus[this.menuDepthIndex.depth1].id,
			);
			this.setChildMenuId(
				selectedNaviMenus[this.menuDepthIndex.depth3]
					? selectedNaviMenus[this.menuDepthIndex.depth3].id
					: selectedNaviMenus[this.menuDepthIndex.depth2].id,
			);
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
					if (depth > 1) {
						menu.active = true;
					}
					return true;
				}
			}
			return false;
		},
		setSelectedNaviMenus(depth, menu) {
			const selectedNaviMenus =
				this.$store.getters['menu/getSelectedNaviMenus'];
			const newSelectedNaviMenus = [];
			for (let i = 0; i < selectedNaviMenus.length; i++) {
				if (i < depth) {
					newSelectedNaviMenus.push(selectedNaviMenus[i]);
				}
			}

			newSelectedNaviMenus[this.menuDepthIndex[`depth${depth}`]] = {
				id: menu.id,
				name: menu.name,
			};

			this.setStoreSelectedNaviMenus(newSelectedNaviMenus);
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
			this.topMandatory = true;
			this.setNaviDrawerWidth();
			this.setIsOverlayNaviDrawer();

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
		},
		setIsOverlayNaviDrawer(value) {
			let isOverlayNaviDrawer = false;
			if (this.expandOnHover && this.childMenus.length === 0) {
				isOverlayNaviDrawer = true;
			}
			this.$emit('setIsOverlayNaviDrawer', isOverlayNaviDrawer);
		},
		setInitIsSubNaviDrawerTempMini() {
			if (this.isSubNaviDrawerTempMini) {
				this.expandOnHover = true;
				this.subNaviDrawerMiniVariant = false;
				this.isSubNaviDrawerTempMini = false;
			}
		},
	},
};
</script>

<style>
.pl-200px {
	padding-left: 200px !important;
}
</style>
