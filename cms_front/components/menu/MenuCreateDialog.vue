<template>
	<v-dialog v-model="createDialog" max-width="500px">
		<template #activator="{ on, attrs }">
			<v-btn color="blue darken-1" text v-bind="attrs" v-on="on">
				{{ buttonName }}
			</v-btn>
		</template>
		<v-card class="pb-3">
			<v-card-title class="d-block">
				<p v-if="parentMenu.name" class="title text-center mt-2 mb-2">
					{{ parentMenu.name }}
				</p>
				<p v-else class="title text-center mt-2 mb-2">
					최상위 메뉴 추가
				</p>
			</v-card-title>
			<v-divider></v-divider>
			<v-card-text class="pb-0">
				<v-container>
					<v-form ref="createForm">
						<v-row class="text-left mt-0 pb-0" tag="v-card-text">
							<v-col>
								<v-text-field
									v-model="createMenu.name"
									label="메뉴명 *"
									:rules="rules.nameRule"
									:maxlength="30"
								></v-text-field>
							</v-col>
						</v-row>
						<v-row class="text-left mt-0 pb-0" tag="v-card-text">
							<v-col>
								<v-text-field
									v-model="createMenu.description"
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
								<v-text-field
									v-model="createMenu.ord"
									label="순서 *"
									:rules="rules.ordRule"
									:maxlength="3"
								></v-text-field>
							</v-col>
						</v-row>
						<v-row
							class="text-left mt-0 pt-0 pb-0"
							tag="v-card-text"
						>
							<v-col>
								<v-select
									v-model="createMenu.menuType"
									label="메뉴타입 *"
									:items="menuTypeCodes"
									item-text="name"
									item-value="code"
									:rules="rules.menuTypeRule"
								>
								</v-select>
							</v-col>
						</v-row>
						<v-row
							class="text-left mt-0 pt-0 pb-0"
							tag="v-card-text"
						>
							<v-col>
								<v-radio-group
									v-model="createMenu.useYn"
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
										<v-radio label="중지" value="N">
										</v-radio>
									</div>
								</v-radio-group>
							</v-col>
						</v-row>
					</v-form>
				</v-container>
			</v-card-text>
			<v-card-actions>
				<v-spacer></v-spacer>
				<v-btn text @click="close"> 취소 </v-btn>
				<v-btn color="blue darken-1" text @click="create"> 등록 </v-btn>
			</v-card-actions>
		</v-card>
	</v-dialog>
</template>

<script>
export default {
	props: {
		parentMenuParam: {
			type: Object,
			default: null,
		},
		menuTypeCodes: {
			type: Array,
			default: null,
		},
	},
	data() {
		return {
			createDialog: false,
			buttonName: '최상위 메뉴 추가',
			rules: {
				nameRule: [v => !!v || '메뉴명은 필수 입니다.'],
				ordRule: [
					v => !!v || '순서는 필수 입니다.',
					v => !/[^0-9]/.test(v) || '순서는 숫자만 입력 가능합니다.',
					v =>
						(!!v && !(v.length > 3)) ||
						'순서는 3자 이상 입력할 수 없습니다.',
				],
				menuTypeRule: [v => !!v || '메뉴타입은 필수 입니다.'],
			},
			parentMenu: {},
			createMenu: {},
			defaultCreateMenu: {
				parentId: null,
				topId: null,
				menu: '',
				name: '',
				description: '',
				ord: null,
				useYn: 'Y',
			},
		};
	},
	watch: {
		createDialog(visible) {
			if (visible) {
				if (this.$refs.createForm) {
					this.$refs.createForm.reset();
				}

				this.createMenu = Object.assign({}, this.defaultCreateMenu);
				this.createMenu.parentId = this.parentMenu.id;
				this.createMenu.topId =
					this.parentMenu.topId || this.parentMenu.id;
			}
		},
	},
	created() {
		if (this.parentMenuParam) {
			this.parentMenu = this.parentMenuParam;
			this.buttonName = '하위 메뉴 추가';
		} else {
			this.parentMenu = {};
		}
	},
	methods: {
		close() {
			this.createDialog = false;
		},
		async create() {
			const validate = this.$refs.createForm.validate();
			if (!validate) return;

			try {
				await this.$axios.$post('/api/menus', this.createMenu);

				this.$store.dispatch('alert/updateAlert', {
					type: 'primary',
					text: '추가를 완료했습니다.',
				});

				this.createDialog = false;
				this.$emit('createSuccess');
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
	},
};
</script>
