<template>
	<div>
		<v-row justify="center">
			<v-col>
				<v-card>
					<v-card-title>
						회원 목록
						<v-spacer></v-spacer>
						<v-text-field
							v-model="search"
							append-icon="mdi-magnify"
							label="Search"
							single-line
							hide-details
							@keyup.enter="getMembers"
						></v-text-field>
					</v-card-title>
					<v-data-table
						:headers="headers"
						:items="items"
						:loading="loading"
						hide-default-footer
						class="elevation-1"
					></v-data-table>
				</v-card>
				<div class="text-center mt-4">
					<common-pagination
						:paging-param="pagingData"
						@goPage="goPage"
					/>
				</div>
			</v-col>
		</v-row>
	</div>
</template>

<script>
import commonPagination from '~/components/commonPagination.vue';
export default {
	components: { commonPagination },
	data() {
		return {
			search: '',
			page: 1,
			loading: true,
			headers: [
				{
					text: '순서',
					align: 'start',
					filterable: false,
					value: 'number',
				},
				{ text: '이름', value: 'name' },
				{ text: '이메일', value: 'email' },
				{ text: '휴대폰', value: 'hp' },
				{ text: '생성일', value: 'createdDate' },
			],
			items: [],
			pagingData: {},
		};
	},
	fetch() {
		this.getMembers();
	},
	fetchOnServer: false,
	computed: {},
	methods: {
		goPage(page) {
			this.page = page;
			this.getMembers();
		},
		async getMembers() {
			try {
				const { data } = await this.$axios.get('/api/members', {
					params: {
						page: this.page - 1,
						size: 10,
						search: this.search,
					},
				});
				const { content, totalElements, pageable, totalPages } = data;
				if (content) {
					let index = 0;
					content.forEach(d => {
						d.number =
							totalElements -
							(this.page - 1) * pageable.pageSize -
							index++;

						d.createdDate = this.$moment(d.createdDate).format(
							'YYYY-MM-DD HH:mm:ss',
						);
					});
				}
				this.loading = false;
				this.items = content;
				this.pagingData = {
					pageable,
					totalPages,
				};
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
	},
};
</script>
