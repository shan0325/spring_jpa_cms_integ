<template>
	<div>
		<h3>공통 테스트</h3>
		<div>{{ members }}</div>
	</div>
</template>

<script>
import clock from '@/components/codepen/clock.vue';
export default {
	async asyncData(context) {
		try {
			const { data } = await context.$axios.get('/api/members', {
				params: {
					page: 0,
					size: 3,
					search: '',
				},
			});

			console.log(data);
			return { members: data.content };
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
	data() {
		return {
			members: {},
		};
	},
};
</script>
