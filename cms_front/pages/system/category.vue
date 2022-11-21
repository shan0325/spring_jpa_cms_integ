<template>
	<div class="mt-7">
		<p class="text-h5">카테고리 설정</p>

		<v-container>
			<v-row class="text-left mt-0 pb-0" tag="v-card-text">
				<v-col>
					<ValidationProvider
						v-slot="{ errors }"
						name="카테고리명"
						rules="required"
						tag="div"
						class="input-text"
						:class="value ? 'value-inputed active' : ''"
					>
						<v-text-field
							v-model="value"
							label="카테고리명 *"
						></v-text-field>
						<p v-if="errors[0]" style="color: red">
							{{ errors[0] }}
						</p>
					</ValidationProvider>
				</v-col>
			</v-row>
		</v-container>
	</div>
</template>

<script>
export default {
	data() {
		return { value: '' };
	},
	fetch() {},
	methods: {},
};
</script>
