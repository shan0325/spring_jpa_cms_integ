<template>
	<div>
		<div v-for="item in menus" :key="item.id">
			<v-list-group
				v-if="item.childMenus && item.childMenus.length > 0"
				v-model="item.active"
				no-action
			>
				<template #activator>
					<v-list-item-title v-text="item.name" />
				</template>
				<div v-for="childItem in item.childMenus" :key="childItem.id">
					<v-list-item
						class="pl-7"
						:class="{
							highlighted: childItem.id === getChildMenuId,
						}"
						@click.stop="moveMenu(childItem)"
					>
						<v-list-item-title v-text="childItem.name" />
					</v-list-item>
				</div>
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
	props: {
		menus: {
			type: Array,
			required: true,
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
