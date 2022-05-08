<template>
	<v-row>
		<v-col>
			<v-card>
				<v-card-title>
					회원검색
					<v-spacer></v-spacer>
					<v-text-field
						v-model="search"
						append-icon="mdi-magnify"
						label="Search"
						single-line
						hide-details
					></v-text-field>
				</v-card-title>
				<v-data-table
					:headers="headers"
					:items="listData"
					:search="search"
				></v-data-table>
			</v-card>
		</v-col>
	</v-row>
</template>

<script>
export default {
	data() {
		return {
			search: '',
			headers: [
				{
					text: '순서',
					align: 'start',
					filterable: false,
					value: '',
				},
				{ text: '이름', value: 'name' },
				{ text: '이메일', value: 'email' },
				{ text: '휴대폰', value: 'hp' },
				{ text: '생성일', value: 'createdDate' },
			],
			listData: [],
		};
	},
	computed: {},
	async mounted() {
		console.log(
			'Authorization : ' +
				this.$axios.defaults.headers.common.Authorization,
		);

		try {
			const response = await this.$axios.get('/api/members');
			const data = response.data;
			if (data) {
				data.forEach(d => {
					d.createdDate = this.$moment(d.createdDate).format(
						'YYYY-MM-DD HH:mm:ss',
					);
				});
			}
			this.listData = data;
		} catch (error) {
			const errorData = error.response.data;
			if (errorData && errorData.apierror) {
				throw new Error(errorData.apierror.message);
			}
			throw new Error(
				'시스템 오류가 발생하였습니다. 잠시후 다시 시도해주세요',
			);
		}
	},
	methods: {},
};
</script>
