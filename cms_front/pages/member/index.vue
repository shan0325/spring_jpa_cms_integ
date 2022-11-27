<template>
	<div class="mt-7">
		<!-- <p class="text-h6 font-weight-bold">회원 검색</p> -->
		<v-card outlined>
			<v-data-table
				:headers="headers"
				:items="items"
				:loading="loading"
				disable-sort
				hide-default-footer
				class="elevation-1"
			>
				<template #top>
					<v-toolbar flat>
						<v-text-field
							v-model="search"
							append-icon="mdi-magnify"
							label="Search"
							single-line
							hide-details
							@keyup.enter="searchMembers"
						></v-text-field>
						<v-spacer></v-spacer>
						<v-dialog v-model="dialog" max-width="500px">
							<template #activator="{ on, attrs }">
								<v-btn
									color="primary"
									dark
									text
									v-bind="attrs"
									v-on="on"
								>
									회원 등록
								</v-btn>
							</template>
							<v-card>
								<v-card-title>
									<span class="text-h5">{{ formTitle }}</span>
								</v-card-title>
								<v-card-text>
									<v-container>
										<v-row>
											<v-col cols="12" sm="6" md="6">
												<v-text-field
													v-model="editedItem.name"
													label="이름"
												></v-text-field>
											</v-col>
											<v-col cols="12" sm="6" md="6">
												<v-text-field
													v-model="
														editedItem.password
													"
													label="비밀번호"
												></v-text-field>
											</v-col>
											<v-col cols="12" sm="6" md="6">
												<v-text-field
													v-model="editedItem.email"
													label="이메일"
												></v-text-field>
											</v-col>
											<v-col cols="12" sm="6" md="6">
												<v-text-field
													v-model="editedItem.hp"
													label="휴대폰"
												></v-text-field>
											</v-col>
											<v-col cols="12">
												회원 상태
												<v-radio-group
													v-model="editedItem.status"
													row
												>
													<v-radio
														label="정상"
														value="ACTIVITY"
													>
													</v-radio>
													<v-radio
														label="정지"
														value="SUSPENTION"
													>
													</v-radio>
													<v-radio
														label="탈퇴"
														value="WITHDRAWAL"
													>
													</v-radio>
												</v-radio-group>
											</v-col>
											<v-col cols="12">
												<div>권한</div>
												<v-checkbox
													v-for="a in authorities"
													:key="a.id"
													v-model="
														editedItem.authorities
													"
													class="d-inline-block mr-5"
													:label="a.authorityName"
													:value="a.id"
												>
												</v-checkbox>
											</v-col>
										</v-row>
									</v-container>
								</v-card-text>

								<v-card-actions>
									<v-spacer></v-spacer>
									<v-btn text @click="close"> 취소 </v-btn>
									<v-btn
										color="blue darken-1"
										text
										@click="save"
									>
										등록
									</v-btn>
								</v-card-actions>
							</v-card>
						</v-dialog>
						<v-dialog v-model="dialogDelete" max-width="500px">
							<v-card>
								<v-card-title class="text-h5"
									>Are you sure you want to delete this
									item?</v-card-title
								>
								<v-card-actions>
									<v-spacer></v-spacer>
									<v-btn
										color="blue darken-1"
										text
										@click="closeDelete"
										>Cancel</v-btn
									>
									<v-btn
										color="blue darken-1"
										text
										@click="deleteItemConfirm"
										>OK</v-btn
									>
									<v-spacer></v-spacer>
								</v-card-actions>
							</v-card>
						</v-dialog>
					</v-toolbar>
					<v-divider></v-divider>
				</template>
				<template #[`item.actions`]="{ item }">
					<v-icon small class="mr-2" @click="editItem(item)">
						mdi-pencil
					</v-icon>
					<v-icon small @click="deleteItem(item)">
						mdi-delete
					</v-icon>
				</template>
				<template #no-data>
					<v-btn color="primary" @click="searchMembers">
						Reset
					</v-btn>
				</template>
			</v-data-table>
		</v-card>
		<div class="text-center mt-4">
			<common-pagination :paging-param="pagingData" @goPage="goPage" />
		</div>
	</div>
</template>

<script>
import CommonPagination from '~/components/common/CommonPagination.vue';
export default {
	components: { CommonPagination },
	data() {
		return {
			search: '',
			page: 1,
			loading: true,
			dialog: false,
			dialogDelete: false,
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
				{ text: '변경', value: 'actions' },
			],
			items: [],
			editedIndex: -1,
			editedItem: {
				id: -1,
				name: '',
				password: '',
				email: '',
				hp: '',
				status: '',
				authorities: [],
			},
			defaultItem: {
				name: '',
				email: '',
				password: '',
				hp: '',
				status: '',
				authorities: [],
			},
			pagingData: {},
			authorities: [],
		};
	},
	async fetch() {
		await this.searchMembers();
		await this.getAuthorities();
	},
	computed: {
		formTitle() {
			return this.editedIndex === -1 ? '회원 추가' : '회원 수정';
		},
	},
	watch: {
		dialog(val) {
			val || this.close();
		},
		dialogDelete(val) {
			val || this.closeDelete();
		},
	},
	methods: {
		async getAuthorities() {
			const { data } = await this.$axios.get('/api/authorities', {
				params: {
					authorityType: 'MEMBER',
				},
			});
			this.authorities = data;
		},
		async goPage(page) {
			this.page = page;
			await this.getMembers();
		},
		async searchMembers() {
			this.page = 1;
			await this.getMembers();
		},
		async getMembers() {
			try {
				const { data } = await this.$axios.get('/api/members', {
					params: {
						page: this.page - 1,
						size: 3,
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

						const authorities = [];
						d.authorities.forEach(a => {
							authorities.push(a.authorityId);
						});
						d.authorities = authorities;

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
		editItem(item) {
			this.editedIndex = this.items.indexOf(item);
			this.editedItem = Object.assign({}, item);
			this.dialog = true;
		},
		deleteItem(item) {
			this.editedIndex = this.desserts.indexOf(item);
			this.editedItem = Object.assign({}, item);
			this.dialogDelete = true;
		},

		deleteItemConfirm() {
			this.desserts.splice(this.editedIndex, 1);
			this.closeDelete();
		},

		close() {
			this.dialog = false;
			this.$nextTick(() => {
				this.editedItem = Object.assign({}, this.defaultItem);
				this.editedIndex = -1;
			});
		},

		closeDelete() {
			this.dialogDelete = false;
			this.$nextTick(() => {
				this.editedItem = Object.assign({}, this.defaultItem);
				this.editedIndex = -1;
			});
		},

		save() {
			if (this.editedIndex > -1) {
				// 수정
				this.updateMember();
			} else {
				// 등록
			}
			this.close();
		},
		updateMember() {
			console.log(this.editedItem);
		},
		createMember() {},
	},
};
</script>
