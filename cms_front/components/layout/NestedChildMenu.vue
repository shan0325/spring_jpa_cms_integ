<template>
	<div :style="{ 'padding-left': depth * 13 + 'px' }">
		<div v-for="item in menus" :key="item.id">
			<v-list-group
				v-if="item.childMenus && item.childMenus.length > 0"
				v-model="item.active"
				no-action
				:sub-group="depth > 0"
			>
				<template #activator>
					<v-list-item-title v-text="item.name" />
				</template>
				<nested-child-menu
					:menus="item.childMenus"
					:depth="depth + 1"
				/>
			</v-list-group>
			<v-list-item
				v-else
				:class="{
					highlighted: item.id === getChildMenuId,
				}"
				@click.stop="moveMenu(item)"
			>
				<v-list-item-title v-text="item.name" />
			</v-list-item>
		</div>
	</div>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex';

export default {
	name: 'NestedChildMenu',
	props: {
		menus: {
			type: Array,
			required: true,
		},
		depth: {
			type: Number,
			default: 0,
		},
	},
	data: () => ({
		menuTypeMovePath: {
			MT_BOARD: '/board',
			MT_CONTENTS: '/contents',
			MT_LINK: '/link',
		},
	}),
	computed: {
		...mapGetters({
			getChildMenuId: 'menu/getChildMenuId',
		}),
	},
	created() {
		console.log(this.depth);
	},
	methods: {
		...mapMutations({
			setChildMenuId: 'menu/setChildMenuId',
		}),
		moveMenu(menu) {
			let movePath = '';
			const menuType = menu.menuType;
			if (menuType === 'MT_MENU') {
				movePath = menu.viewPath;
			} else {
				movePath = this.menuTypeMovePath[menuType];
			}
			this.$router.push(`${movePath}?menuId=${menu.id}`);

			this.setChildMenuId(menu.id);
		},
	},
};
</script>

<style>
.highlighted {
	color: red !important;
}
</style>
