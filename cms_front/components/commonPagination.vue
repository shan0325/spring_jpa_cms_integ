<template>
	<div>
		<v-btn small min-width="30" class="px-0" @click="goPage(1)"
			><v-icon>mdi-chevron-double-left</v-icon></v-btn
		>
		<v-btn small min-width="30" class="px-0" @click="goPage(prevBlockPage)"
			><v-icon>mdi-chevron-left</v-icon></v-btn
		>
		<template v-for="(n, index) in pages">
			<v-btn
				:key="n"
				small
				min-width="30"
				class="px-0"
				:class="{
					'v-btn--active': n === curPage,
					'ml-1': index > 0,
				}"
				@click="goPage(n)"
			>
				{{ n }}
			</v-btn>
		</template>
		<v-btn small min-width="30" class="px-0" @click="goPage(nextBlockPage)"
			><v-icon>mdi-chevron-right</v-icon></v-btn
		>
		<v-btn small min-width="30" class="px-0" @click="goPage(totalPages)"
			><v-icon>mdi-chevron-double-right</v-icon></v-btn
		>
	</div>
</template>

<script>
export default {
	props: {
		pagingParam: {
			type: Object,
			default: null,
		},
	},
	data() {
		return {
			totalPages: 1,
			curPage: 1,
			pagesPerBlock: 10,
			curBlock: 1,
			prevBlockPage: 1,
			nextBlockPage: 1,
			thisBlockStartPage: 1,
			thisBlockLastPage: 1,
			pages: [],
			toggle_exclusive: [2],
		};
	},
	watch: {
		pagingParam(data) {
			const { pageable, totalPages } = data;
			const { pageNumber } = pageable;

			const curPage = pageNumber + 1;
			const pagesPerBlock = this.pagesPerBlock;
			const curBlock =
				curPage % pagesPerBlock !== 0
					? parseInt(curPage / pagesPerBlock) + 1
					: curPage / pagesPerBlock;
			const prevBlockPage =
				curBlock === 1 ? 1 : (curBlock - 1) * pagesPerBlock;
			const nextBlockPage =
				curBlock * pagesPerBlock + 1 > totalPages
					? totalPages
					: curBlock * pagesPerBlock + 1;
			const thisBlockStartPage =
				curBlock === 1 ? 1 : (curBlock - 1) * pagesPerBlock + 1;
			const thisBlockLastPage =
				curBlock * pagesPerBlock > totalPages
					? totalPages
					: curBlock * pagesPerBlock;

			console.log(
				`curPage: ${curPage} / curBlock: ${curBlock} / prevBlockPage: ${prevBlockPage} / nextBlockPage: ${nextBlockPage} 
				/ thisBlockStartPage: ${thisBlockStartPage} / thisBlockLastPage: ${thisBlockLastPage}`,
			);

			this.totalPages = totalPages;
			this.curPage = curPage;
			this.pagesPerBlock = pagesPerBlock;
			this.curBlock = curBlock;
			this.prevBlockPage = prevBlockPage;
			this.nextBlockPage = nextBlockPage;
			this.thisBlockStartPage = thisBlockStartPage;
			this.thisBlockLastPage = thisBlockLastPage;

			this.pages.splice(0, this.pages.length); // 배열 비우기
			for (let i = thisBlockStartPage; i <= thisBlockLastPage; i++) {
				this.pages.push(i);
			}
		},
	},
	methods: {
		goPage(page) {
			this.$emit('goPage', page);
		},
	},
};
</script>
