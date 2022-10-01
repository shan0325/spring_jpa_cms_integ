<template>
	<v-dialog v-model="createDialog" max-width="500px">
		<template #activator="{ on, attrs }">
			<v-btn color="blue darken-1" text v-bind="attrs" v-on="on">
				{{ buttonName }}
			</v-btn>
		</template>
		<v-card class="pb-3">
			<v-card-title class="d-block mt-2 text-center">
				<p v-if="parentCode.code" class="title">
					{{ parentCode.code }}
				</p>
				<p v-else class="title">최상위 코드</p>
			</v-card-title>
			<v-card-text class="pb-0">
				<v-divider></v-divider>
				<v-container>
					<v-form ref="createForm">
						<v-row class="text-left mt-0 pb-0" tag="v-card-text">
							<v-col>
								<v-text-field
									v-model="createCode.code"
									label="코드 *"
									:rules="codeRules"
									:maxlength="20"
								></v-text-field>
							</v-col>
						</v-row>
						<v-row class="text-left mt-0 pb-0" tag="v-card-text">
							<v-col>
								<v-text-field
									v-model="createCode.name"
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
									v-model="createCode.description"
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
									v-model="createCode.ord"
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
									v-model="createCode.useYn"
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
		parentCodeParam: {
			type: Object,
			default: null,
		},
	},
	data() {
		return {
			createDialog: false,
			buttonName: '최상위 코드 추가',
			codeRules: [v => !!v || '코드는 필수 입니다.'],
			nameRules: [v => !!v || '코드명은 필수 입니다.'],
			ordRules: [
				v => !!v || '순서는 필수 입니다.',
				v => !/[^0-9]/.test(v) || '순서는 숫자만 입력 가능합니다.',
				v =>
					(!!v && !(v.length > 3)) ||
					'순서는 3자 이상 입력할 수 없습니다.',
			],
			parentCode: {},
			createCode: {},
			defaultCreateCode: {
				parentId: null,
				topId: null,
				code: '',
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
				this.createCode = Object.assign({}, this.defaultCreateCode);
				this.createCode.parentId = this.parentCode.id;
				this.createCode.topId =
					this.parentCode.topId || this.parentCode.id;
			}
		},
	},
	created() {
		if (this.parentCodeParam) {
			this.parentCode = this.parentCodeParam;
			this.buttonName = '하위 코드 추가';
		} else {
			this.parentCode = {};
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
				await this.$axios.$post('/api/codes', this.createCode);

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
