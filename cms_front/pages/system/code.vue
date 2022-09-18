<template>
	<v-card>
		<v-card-title class="indigo white--text text-h5">
			코드 설정
		</v-card-title>
		<v-row class="pa-4" justify="space-between">
			<v-col cols="5">
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
						v-if="!code"
						class="text-h6 grey--text text--lighten-1 font-weight-light"
						style="align-self: center"
					>
						Select a Code
					</div>
					<v-card v-else :key="code.id" flat>
						<v-card-text>
							<h3 class="text-h5">
								{{ code.code }}
							</h3>
						</v-card-text>
						<v-divider></v-divider>
						<v-row class="text-left mt-0 pb-0" tag="v-card-text">
							<v-col>
								<v-text-field
									v-model="code.name"
									label="코드명"
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
									v-model="code.description"
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
									v-model="code.ord"
									label="순서"
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
									v-model="code.useYn"
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
						<v-row>
							<v-col class="text-left">
								<v-btn> 하위코드 추가 </v-btn>
							</v-col>
							<v-col class="text-right">
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
			code: null,
			active: [],
			open: [],
			nameRules: [v => !!v || '코드명은 필수 입니다.'],
			ordRules: [
				v => !!v || '순서는 필수 입니다.',
				v => !/[^0-9]/.test(v) || '순서는 숫자만 입력 가능합니다.',
				v => !(v.length > 3) || '순서는 3자 이상 입력할 수 없습니다.',
			],
		};
	},
	methods: {
		async getCode() {
			if (!this.active.length) {
				this.code = null;
				return;
			}
			const id = this.active[0];
			this.code = await this.$axios.$get(`/api/codes/${id}`);
		},
		async save() {
			const updateCode = {
				name: this.code.name,
				description: this.code.description,
				ord: this.code.ord,
				useYn: this.code.useYn,
			};
			await this.$axios.$put(`/api/codes/${this.code.id}`, updateCode);
			alert('수정을 완료했습니다.');
		},
	},
};
</script>
