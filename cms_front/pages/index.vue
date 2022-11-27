<template>
	<v-row class="clock-row text-center d-flex align-center">
		<v-col cols="12">
			<clock />

			<!-- <v-card max-width="375" class="mx-auto mt-10">
				<v-img src="https://picsum.photos/700/700" height="300px" dark>
					<v-row class="fill-height">
						<v-card-title class="white--text pl-12 pt-12">
							<div class="text-h4 pl-12 pt-12">Contact Info</div>
						</v-card-title>
					</v-row>
				</v-img>

				<v-list two-line>
					<v-list-item>
						<v-list-item-icon>
							<v-icon color="indigo"> mdi-phone </v-icon>
						</v-list-item-icon>

						<v-list-item-content>
							<v-list-item-title>(02) 555-1234</v-list-item-title>
						</v-list-item-content>
					</v-list-item>

					<v-divider inset></v-divider>

					<v-list-item>
						<v-list-item-icon>
							<v-icon color="indigo"> mdi-email </v-icon>
						</v-list-item-icon>

						<v-list-item-content>
							<v-list-item-title
								>shan0325@naver.com</v-list-item-title
							>
						</v-list-item-content>
					</v-list-item>
				</v-list>
			</v-card> -->
		</v-col>
	</v-row>
</template>

<script>
import clock from '@/components/codepen/clock.vue';
export default {
	components: {
		clock,
	},
	fetch() {},
};
</script>

<style>
.clock-row {
	height: 80vh;
}
</style>
