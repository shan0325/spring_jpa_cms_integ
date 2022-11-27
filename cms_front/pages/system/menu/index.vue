<template>
	<div class="mt-7">
		<!-- <p class="text-h6 font-weight-bold">메뉴 설정</p> -->

		<v-tabs v-model="menuGroupTabsIndex" class="mb-5">
			<v-tab
				v-for="mg in menuGroups"
				:key="mg.id"
				@click="getMenus(mg.id)"
			>
				{{ mg.groupName }}
			</v-tab>
		</v-tabs>

		<v-card outlined>
			<v-row class="pa-4" justify="space-between">
				<v-col cols="12" sm="5">
					<v-treeview
						:items="menus"
						:item-children="'childMenus'"
						:item-text="'name'"
						:active.sync="active"
						:open.sync="open"
						activatable
						color="warning"
						transition
						@update:active="getMenu"
					>
					</v-treeview>
				</v-col>

				<v-divider vertical></v-divider>

				<v-col class="text-center">
					<v-scroll-y-transition mode="out-in">
						<div
							v-if="!editeMenu"
							class="text-h6 grey--text text--lighten-1 font-weight-light"
							style="align-self: center"
						>
							Select a Menu
						</div>
						<v-card v-else :key="editeMenu.id" flat>
							<ValidationObserver ref="editorForm">
								<form>
									<v-row
										class="text-left mt-0 pb-0"
										tag="v-card-text"
									>
										<v-col>
											<ValidationProvider
												v-slot="{ errors }"
												name="메뉴명"
												rules="required"
											>
												<v-text-field
													v-model="editeMenu.name"
													label="메뉴명 *"
													:error-messages="errors[0]"
													:maxlength="30"
												></v-text-field>
											</ValidationProvider>
										</v-col>
									</v-row>
									<v-row
										class="text-left mt-0 pt-0 pb-0"
										tag="v-card-text"
									>
										<v-col>
											<v-text-field
												v-model="editeMenu.description"
												label="메뉴설명"
												:maxlength="50"
											></v-text-field>
										</v-col>
									</v-row>
									<v-row
										class="text-left mt-0 pt-0 pb-0"
										tag="v-card-text"
									>
										<v-col>
											<ValidationProvider
												v-slot="{ errors }"
												name="순서"
												rules="required|numeric"
											>
												<v-text-field
													v-model="editeMenu.ord"
													label="순서 *"
													:error-messages="errors[0]"
													:maxlength="3"
												></v-text-field>
											</ValidationProvider>
										</v-col>
									</v-row>
									<v-row
										class="text-left mt-0 pt-0 pb-0"
										tag="v-card-text"
									>
										<v-col>
											<ValidationProvider
												v-slot="{ errors }"
												name="메뉴타입"
												rules="required"
											>
												<v-select
													v-model="editeMenu.menuType"
													label="메뉴타입 *"
													:items="menuTypeCodes"
													item-text="name"
													item-value="code"
													:error-messages="errors[0]"
												>
												</v-select>
											</ValidationProvider>
										</v-col>
									</v-row>
									<v-row
										class="text-left mt-0 pt-0 pb-0"
										tag="v-card-text"
									>
										<v-col>
											<v-radio-group
												v-model="editeMenu.useYn"
												class="mt-0 pb-0"
											>
												<template #label>
													<div>사용여부</div>
												</template>
												<div class="d-flex">
													<v-radio
														class="mb-0 mr-5"
														label="사용"
														value="Y"
													>
													</v-radio>
													<v-radio
														label="중지"
														value="N"
													>
													</v-radio>
												</div>
											</v-radio-group>
										</v-col>
									</v-row>
								</form>
							</ValidationObserver>
							<v-row>
								<v-col class="text-left">
									<menu-create-dialog
										:parent-menu-param="editeMenu"
										:menu-type-codes="menuTypeCodes"
										@createSuccess="createSuccess"
									/>
								</v-col>
								<v-col class="text-right">
									<v-btn
										color="red darken-1"
										text
										@click="deleteCode"
									>
										삭제
									</v-btn>
									<v-btn
										color="blue darken-1"
										text
										@click="save"
									>
										수정
									</v-btn>
								</v-col>
							</v-row>
						</v-card>
					</v-scroll-y-transition>
				</v-col>
			</v-row>
		</v-card>
		<confirm-message ref="confirm" />
	</div>
</template>

<script>
import MenuCreateDialog from '~/components/menu/MenuCreateDialog.vue';
import ConfirmMessage from '~/components/common/ConfirmMessage.vue';
export default {
	components: {
		MenuCreateDialog,
		ConfirmMessage,
	},
	data() {
		return {
			menuGroupTabsIndex: 0,
			menuGroups: [],
			menus: [],
			menuTypeCodes: [],
			editeMenu: null,
			active: [],
			open: [],
		};
	},
	async fetch() {
		await this.getMenuGroups();
		await this.getMenus(1);
		await this.getMenuTypeCodes('SYSTEM', 'MENU_TYPE');
	},
	methods: {
		async getMenuGroups() {
			try {
				this.menuGroups = await this.$axios.$get('/api/menu-groups');
			} catch (e) {}
		},
		async getMenus(menuGroupId) {
			try {
				this.menus = await this.$axios.$get(
					`/api/menus/menu-group/${menuGroupId}`,
				);
			} catch (e) {}
		},
		async getMenuTypeCodes(topCode, parentCode) {
			try {
				this.menuTypeCodes = await this.$axios.$get(
					`/api/codes/top-code/${topCode}/parent-code/${parentCode}`,
				);
			} catch (e) {}
		},
		async getMenu() {
			if (!this.active.length) {
				this.editeMenu = null;
				return;
			}
			const id = this.active[0];
			this.editeMenu = await this.$axios.$get(`/api/menus/${id}`);
		},
		async getCode() {
			if (!this.active.length) {
				this.editeCode = null;
				return;
			}
			const id = this.active[0];
			this.editeCode = await this.$axios.$get(`/api/codes/${id}`);
		},
		save() {
			try {
				this.$refs.editorForm.validate().then(async success => {
					if (!success) return;

					await this.$axios.$put(
						`/api/menus/${this.editeMenu.id}`,
						this.editeMenu,
					);

					this.$store.dispatch('alert/updateAlert', {
						type: 'primary',
						text: '수정을 완료했습니다.',
					});

					this.getMenus(this.menuGroups[this.menuGroupTabsIndex].id);
				});
			} catch (error) {
				let errorMessage =
					'시스템 오류가 발생하였습니다. 잠시후 다시 시도해주세요';

				const errorData = error.response.data;
				if (errorData && errorData.apierror) {
					errorMessage = errorData.apierror.message;
				}

				this.$store.dispatch('alert/updateAlert', {
					type: 'red',
					text: errorMessage,
				});
			}
		},
		createSuccess() {
			this.getMenus(this.menuGroups[this.menuGroupTabsIndex].id);
		},
		async deleteCode() {
			const confirmResult = await this.$refs.confirm.open({
				type: 'red',
				title: '확인',
				text: '정말로 삭제하시겠습니까?',
			});

			if (confirmResult) {
				try {
					await this.$axios.$delete(
						`/api/menus/${this.editeMenu.id}`,
						this.editeMenu,
					);

					this.$store.dispatch('alert/updateAlert', {
						type: 'primary',
						text: '삭제를 완료했습니다.',
					});
					this.getMenus(this.menuGroups[this.menuGroupTabsIndex].id);
					this.editeMenu = null;
				} catch (error) {
					let errorMessage =
						'시스템 오류가 발생하였습니다. 잠시후 다시 시도해주세요';

					const errorData = error.response.data;
					if (errorData && errorData.apierror) {
						errorMessage = errorData.apierror.message;
					}

					this.$store.dispatch('alert/updateAlert', {
						type: 'red',
						text: errorMessage,
					});
				}
			}
		},
	},
};
</script>
