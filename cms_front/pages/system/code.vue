<template>
	<v-card>
		<v-card-title class="indigo white--text text-h5">
			코드 설정
		</v-card-title>
		<v-row class="pa-4" justify="space-between">
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
							<h3 class="text-h5">
								{{ editeCode.code }}
							</h3>
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
										:maxlength="10"
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
								<v-dialog
									v-model="createDialog"
									max-width="500px"
								>
									<template #activator="{ on, attrs }">
										<v-btn v-bind="attrs" v-on="on">
											하위코드 추가
										</v-btn>
									</template>
									<v-card>
										<v-card-title>
											<span class="text-h5">
												{{ editeCode.code }}</span
											>
										</v-card-title>
										<v-card-text>
											<v-container>
												<v-form ref="createForm">
													<v-row
														class="text-left mt-0 pb-0"
														tag="v-card-text"
													>
														<v-col>
															<v-text-field
																v-model="
																	createCode.code
																"
																label="코드 *"
																:rules="
																	codeRules
																"
																:maxlength="10"
															></v-text-field>
														</v-col>
													</v-row>
													<v-row
														class="text-left mt-0 pb-0"
														tag="v-card-text"
													>
														<v-col>
															<v-text-field
																v-model="
																	createCode.name
																"
																label="코드명 *"
																:rules="
																	nameRules
																"
																:maxlength="10"
															></v-text-field>
														</v-col>
													</v-row>
													<v-row
														class="text-left mt-0 pt-0 pb-0"
														tag="v-card-text"
													>
														<v-col>
															<v-text-field
																v-model="
																	createCode.description
																"
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
																v-model="
																	createCode.ord
																"
																label="순서 *"
																:rules="
																	ordRules
																"
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
																v-model="
																	createCode.useYn
																"
																class="mt-0 pb-0"
															>
																<template
																	#label
																>
																	<div>
																		사용여부
																	</div>
																</template>
																<div
																	class="d-flex"
																>
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
												</v-form>
											</v-container>
										</v-card-text>
										<v-card-actions>
											<v-spacer></v-spacer>
											<v-btn
												color="blue darken-1"
												text
												@click="close"
											>
												취소
											</v-btn>
											<v-btn
												color="blue darken-1"
												text
												@click="create"
											>
												등록
											</v-btn>
										</v-card-actions>
									</v-card>
								</v-dialog>
							</v-col>
							<v-col class="text-right">
								<v-btn @click="deleteCode"> 삭제 </v-btn>
								<v-btn color="primary" @click="save">
									수정
								</v-btn>
							</v-col>
						</v-row>
					</v-card>
				</v-scroll-y-transition>
			</v-col>
		</v-row>
	</v-card>
</template>

<script>
export default {
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
			defaultCreateCode: {
				parentId: null,
				topId: null,
				code: '',
				name: '',
				description: '',
				ord: null,
				useYn: 'Y',
			},
			createCode: {},
			createDialog: false,
		};
	},
	watch: {
		createDialog(visible) {
			if (visible) {
				console.log('open');
				this.createCode = Object.assign({}, this.defaultCreateCode);
				this.createCode.parentId = this.editeCode.id;
				this.createCode.topId =
					this.editeCode.topId || this.editeCode.id;
			} else {
				console.log('close');
			}
		},
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

				this.$store.dispatch('snackbar/showSnack', {
					text: '수정을 완료했습니다.',
					color: 'success',
				});
			} catch (error) {
				let errorMessage =
					'시스템 오류가 발생하였습니다. 잠시후 다시 시도해주세요';

				const errorData = error.response.data;
				if (errorData && errorData.apierror) {
					errorMessage = errorData.apierror.message;
				}

				this.$store.dispatch('snackbar/showSnack', {
					text: errorMessage,
					color: 'red',
				});
			}
		},
		close() {
			this.createDialog = false;
		},
		async create() {
			const validate = this.$refs.createForm.validate();
			if (!validate) return;

			if (!this.createCode.parentId || !this.createCode.topId) {
				this.$store.dispatch('snackbar/showSnack', {
					text: '부모 아이디는 필수 입니다. 다시 실행해 주세요',
					color: 'red',
				});
				this.createDialog = false;
				this.getCode();
				return;
			}

			try {
				await this.$axios.$post('/api/codes', this.createCode);

				this.$store.dispatch('snackbar/showSnack', {
					text: '추가를 완료했습니다.',
					color: 'success',
				});
			} catch (error) {
				let errorMessage =
					'시스템 오류가 발생하였습니다. 잠시후 다시 시도해주세요';

				const errorData = error.response.data;
				if (errorData && errorData.apierror) {
					errorMessage = errorData.apierror.message;
				}

				this.$store.dispatch('snackbar/showSnack', {
					text: errorMessage,
					color: 'red',
				});
			}
			this.createDialog = false;
			this.codes = await this.$axios.$get('/api/codes');
		},
		async deleteCode() {
			if (confirm('정말로 삭제하시겠습니까?')) {
				try {
					await this.$axios.$delete(
						`/api/codes/${this.editeCode.id}`,
						this.editeCode,
					);

					this.$store.dispatch('snackbar/showSnack', {
						text: '삭제를 완료했습니다.',
						color: 'success',
					});
					this.codes = await this.$axios.$get('/api/codes');
				} catch (error) {
					let errorMessage =
						'시스템 오류가 발생하였습니다. 잠시후 다시 시도해주세요';

					const errorData = error.response.data;
					if (errorData && errorData.apierror) {
						errorMessage = errorData.apierror.message;
					}

					this.$store.dispatch('snackbar/showSnack', {
						text: errorMessage,
						color: 'red',
					});
				}
			}
		},
	},
};
</script>
