<template>
</template>

<script>
	export default{
		name: "Terms",
		data(){
			return{
				terms:{
					degrees: [{
					  value: '高中及以下',
					  label: '高中及以下'
					}, {
					  value: '专科',
					  label: '专科'
					}, {
					  value: '本科',
					  label: '本科'
					}, {
					  value: '硕士',
					  label: '硕士'
					}, {
					  value: '博士',
					  label: '博士'
					}],
				posts:  [{
					  value: 'IT',
					  label: 'IT',
					  children: [{
						value: 'Java',
						label: 'Java'
					},{
						value: '架构师',
						label: '架构师'
					}]
					},{
					  value: '金融',
					  label: '金融',
					  children: [{
						value: '会计',
						label: '会计'
					},{
						value: '理财师',
						label: '理财师'
					}]
					}],
				times: [{
						value: false,
						label: '不限'
					},{
						value: true,
						label: '最近活跃'
					}
				],
				addrs: [{
					  label: '热门城市',
					  options: [{
						value: '上海',
						label: '上海'
					  }, {
						value: '北京',
						label: '北京'
					  }]
					}, {
					  label: '城市名',
					  options: [{
						value: '成都',
						label: '成都'
					  }, {
						value: '深圳',
						label: '深圳'
					  }, {
						value: '广州',
						label: '广州'
					  }, {
						value: '大连',
						label: '大连'
					  }]
					}]
			}}
			},
		created() {
			this.$emit("set-terms", this.terms)
		}
	}
</script>

<style>
</style>
