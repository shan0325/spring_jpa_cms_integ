<template>
	<div>
		<p class="headline mt-5 mb-5">코드 설정</p>
		<v-card>
			<v-row class="pa-4" justify="space-between">
				<v-col cols="12">
					<code-create-dialog
						:parent-code-param="null"
						@createSuccess="createSuccess"
					/>
					<v-divider class="mt-3"></v-divider>
				</v-col>
				<v-col cols="12" sm="5">
					<v-treeview
						:items="codes"
						:item-children="'childCodes'"
						:item-text="'code'"
						:active.sync="active"
						:open.sync="open"
						activatable
						color="warning"
						transition
						@update:active="getCode"
					>
					</v-treeview>
				</v-col>

				<v-divider vertical></v-divider>

				<v-col class="text-center">
					<v-scroll-y-transition mode="out-in">
						<div
							v-if="!editeCode"
							class="text-h6 grey--text text--lighten-1 font-weight-light"
							style="align-self: center"
						>
							Select a Code
						</div>
						<v-card v-else :key="editeCode.id" flat>
							<v-card-text>
								<p class="title">
									{{ editeCode.code }}
								</p>
							</v-card-text>
							<v-divider></v-divider>
							<v-form ref="editeForm">
								<v-row
									class="text-left mt-0 pb-0"
									tag="v-card-text"
								>
									<v-col>
										<v-text-field
											v-model="editeCode.name"
											label="코드명 *"
											:rules="nameRules"
											:maxlength="30"
										></v-text-field>
									</v-col>
								</v-row>
								<v-row
									class="text-left mt-0 pt-0 pb-0"
									tag="v-card-text"
								>
									<v-col>
										<v-text-field
											v-model="editeCode.description"
											label="코드설명"
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
											v-model="editeCode.ord"
											label="순서 *"
											:rules="ordRules"
											:maxlength="3"
										></v-text-field>
									</v-col>
								</v-row>
								<v-row
									class="text-left mt-0 pt-0 pb-0"
									tag="v-card-text"
								>
									<v-col>
										<v-radio-group
											v-model="editeCode.useYn"
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
							<v-row>
								<v-col class="text-left">
									<code-create-dialog
										:parent-code-param="editeCode"
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
			<v-row>
				<confirm-message ref="confirm" />
			</v-row>
		</v-card>
	</div>
</template>

<script>
import CodeCreateDialog from '~/components/code/CodeCreateDialog.vue';
import ConfirmMessage from '~/components/common/ConfirmMessage.vue';
export default {
	components: {
		CodeCreateDialog,
		ConfirmMessage,
	},
	async asyncData({ params, $axios, error }) {
		try {
			const codes = await $axios.$get('/api/codes');
			return { codes };
		} catch (e) {}
	},
	data() {
		return {
			codes: [],
			editeCode: null,
			active: [],
			open: [],
			codeRules: [v => !!v || '코드는 필수 입니다.'],
			nameRules: [v => !!v || '코드명은 필수 입니다.'],
			ordRules: [
				v => !!v || '순서는 필수 입니다.',
				v => !/[^0-9]/.test(v) || '순서는 숫자만 입력 가능합니다.',
				v =>
					(!!v && !(v.length > 3)) ||
					'순서는 3자 이상 입력할 수 없습니다.',
			],
		};
	},
	methods: {
		async getCode() {
			if (!this.active.length) {
				this.editeCode = null;
				return;
			}
			const id = this.active[0];
			this.editeCode = await this.$axios.$get(`/api/codes/${id}`);
		},
		async save() {
			const validate = this.$refs.editeForm.validate();
			if (!validate) return;

			try {
				await this.$axios.$put(
					`/api/codes/${this.editeCode.id}`,
					this.editeCode,
				);

				this.$store.dispatch('alert/updateAlert', {
					type: 'primary',
					text: '수정을 완료했습니다.',
				});

				this.codes = await this.$axios.$get('/api/codes');
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
		async createSuccess() {
			this.codes = await this.$axios.$get('/api/codes');
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
						`/api/codes/${this.editeCode.id}`,
						this.editeCode,
					);

					this.$store.dispatch('alert/updateAlert', {
						type: 'primary',
						text: '삭제를 완료했습니다.',
					});
					this.codes = await this.$axios.$get('/api/codes');
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
