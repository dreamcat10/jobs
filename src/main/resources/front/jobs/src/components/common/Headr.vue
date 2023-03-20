<template>
	<div>
		<div class="login-head" :class="{fixed : isFixed}">
			<div class="left" style="margin-top: 25px;">
				<el-page-header @back="goBack" title="返回">
				</el-page-header>
			</div>
			<div class="right" style="width: 83%;">Jobs招聘</div>
		</div>
		<div v-if="isFixed" style="height: 75px; width: 100%;"></div>
	</div>
</template>

<script>
	export default{
		name: "Headr",
		data(){
			return {
				isFixed: false,
			}
		},
		mounted() {
		　　// 滚动条的获取
		　　window.addEventListener('scroll', this.handleScrollx, true)
	    },
		methods:{
			goBack(){
				this.$router.go(-1)
			},
			handleScrollx() {
			  if(window.pageYOffset > 1){
				  this.isFixed = true
			  }
			  if(window.pageYOffset == 0){
				  this.isFixed = false
			  }
			}
		}
	}
</script>

<style>
	.login-head{
		display: flex;
		height: 10%;
		width: 100%;
		background-color: #B3C0D1;
		line-height: 70px;
		text-align: center;
		color: white;
		font-size: 60px;
		font-weight: 700;
		font-family: "华文行楷";
	}
	.fixed{
		position: fixed;
		top: 0;
		left: 0;
		z-index: 200;
	}
</style>
