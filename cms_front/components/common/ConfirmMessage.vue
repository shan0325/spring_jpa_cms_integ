<!-- 
// 예제    
const confirmResult = await this.$refs.confirm.open({
    type: 'red',
    title: '확인',
    text: '정말로 삭제하시겠습니까?',
}); 
-->
<template>
	<v-overlay :value="is_show" z-index="100005" style="min-width: 100%">
		<v-alert
			:color="type"
			border="top"
			light
			colored-border
			elevation="3"
			style="min-width: 320px"
		>
			<v-row>
				<v-col cols="12">
					{{ title }}
				</v-col>
				<v-col cols="12" v-html="change(text)"> </v-col>
			</v-row>
			<v-row>
				<v-col cols="12" align="right">
					<v-btn :color="type" text @click="ok"> 예 </v-btn>
					<v-btn text @click="cancel"> 아니요 </v-btn>
				</v-col>
			</v-row>
		</v-alert>
	</v-overlay>
</template>

<script>
export default {
	data() {
		return {
			is_show: false,
			type: 'info', // success, info, warning, error 이렇게 4가지 사용 가능
			title: '',
			text: '',
			result: undefined,
		};
	},
	mounted() {
		document.addEventListener('keydown', this.onKeyEvent);
	},
	methods: {
		onKeyEvent(e) {
			if (this.is_show) {
				const code = e.keyCode;
				if (code === 27) {
					// esc
					this.cancel();
				} else if (code === 13) {
					// enter
					this.ok();
				} else {
					e.stopPropagation();
					e.preventDefault();
				}
			}
		},
		change(v) {
			return String(v).replace(/(?:\r\n|\r|\n)/g, '</br>');
		},
		open(options) {
			this.is_show = true;
			this.type = options.type === (null || '') ? 'info' : options.type;
			this.title = options.title;
			this.text = options.text;
			return new Promise((resolve, reject) => {
				this.result = resolve;
			});
		},
		ok() {
			this.is_show = false;
			this.result(true);
		},
		cancel() {
			this.is_show = false;
			this.result(false);
		},
	},
};
</script>

<style></style>
