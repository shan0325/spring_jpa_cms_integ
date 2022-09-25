<!-- 
// 사용법
this.$store.dispatch('snackbar/showSnack', {
    text: '내용',
    color: 'success',
    timeout: 3500,
});
-->
<template>
	<v-snackbar v-model="show" :color="color" :timeout="timeout" :top="'top'">
		{{ text }}

		<template #action="{ attrs }">
			<v-btn dark text v-bind="attrs" @click="show = false">
				Close
			</v-btn>
		</template>
	</v-snackbar>
</template>

<script>
export default {
	data() {
		return {
			show: false,
			color: '',
			text: '',
			timeout: 0,
		};
	},
	created() {
		this.$store.subscribe((mutation, state) => {
			if (mutation.type === 'snackbar/SHOW_MESSAGE') {
				this.text = state.snackbar.text;
				this.color = state.snackbar.color;
				this.timeout = state.snackbar.timeout;
				this.show = true;
			}
		});
	},
};
</script>
