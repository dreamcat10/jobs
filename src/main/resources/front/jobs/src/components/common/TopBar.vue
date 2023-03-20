<template>
	<div class="bar-head">
	 <div class="top-left">Jobs 招聘</div>
   	  <div class="top-right">
		  <el-row :gutter="40">
			<el-col :span="6"><div class="grid-content bg-purple"
								:class="{active: index==0}" @click="toOtherPath(0)">招聘</div></el-col>
			<el-col :span="6"><div class="grid-content bg-purple"
								:class="{active: index==1}" @click="toOtherPath(1)">社区</div></el-col>
			<el-col :span="6"><div class="grid-content bg-purple"
								:class="{active: index==2}" @click="toOtherPath(2)">消息</div></el-col>
			<el-col :span="6"><div class="grid-content bg-purple"
								:class="{active: index==3,logging: !isLogin}" @click="toOtherPath(3)">我的</div></el-col>
			<el-col :span="6"><div class="grid-content bg-purple"
								:class="{active: index==3,logging: isLogin}" @click="toLogin()">去登录</div></el-col>						
		  </el-row>
	 </div>	  
	</div>
</template>

<script>
	export default{
		name: "TopBar",
		components:{},
		data(){
			return{
				path: ['/index', '/community', '/news', '/mine'],
				isLogin: false
			}
		},
		props:{
			index: {
				type: Number,
				default:0
			}	
		},
		created() {
		
		},
		activated() {
			if(this.$store.getters.getUser != null && this.$store.getters.getUser.id != null){
				console.log('0',this.$store.getters.getUser)
				this.isLogin = true
			}else{
				console.log(1)
				this.isLogin = false
			}
		},
		methods:{
			toLogin(){
				this.$router.push({
					path: '/login',
					name: 'login'
				});
			},
			toOtherPath(toIndex){
				if(this.index != toIndex){
					this.$router.push(this.path[toIndex]);
				} 
			}
		}
	}
</script>

<style>
	.logging{
		display: none;
	}
	.active{
		font-weight: 700;
	}
	.bar-head{
		height: 10%;
		padding: 0;
		background-color: #B3C0D1;
		display: flex;
	}
	.top-left{
		height: 100%;
		width: 50%;
		line-height: 70px;
		text-align: center;
		color: white;
		font-size: 60px;
		font-weight: 700;
		font-family: "华文行楷";
	}
	.top-right{
		width: 50%;
		height: 100%;
		margin-right: 10px;
		padding-left: 40%;
	}
	
	.el-row {
	   margin-top: 20px;
	   &:last-child {
	     margin-bottom: 0;
	   }
	 }
	 .el-col {
	   border-radius: 4px;
	 }
	 .bg-purple-dark {
	   background: #99a9bf;
	 }
	 .bg-purple {
			background: #6272A4;
	 }
	 .bg-purple-light {
	   background: #e5e9f2;
	 }
	 .grid-content {
		color: white;
	    border-radius: 4px ;
		line-height: 32px;
	    min-height: 36px;
		text-align: center;
	 }
	 .row-bg {
	   padding: 10px 0;
	   background-color: #f9fafc;
	 }
</style>
