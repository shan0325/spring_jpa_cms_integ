<template>
	<div>
		<div>{{ $store.state.auth.member.name }}</div>
		<div>{{ $store.state.auth.member.email }}</div>
	</div>
</template>

<script>
export default {
	name: 'IndexPage',
	created() {},
};
</script>
