<template>
	<div class="app">
	  <TopBar></TopBar>
	  <div class="main">
		  <el-container>
		      <el-aside width="200px">
				<el-button style="margin-top: 100px;" type="text" 
				@click=changeOrder(0) icon="el-icon-thumb" :class="{checked: order == 0}">推荐牛人</el-button>
				<el-button type="text" 
				@click=changeOrder(1) icon="el-icon-search" :class="{checked: order == 1}">搜索牛人</el-button>
				<el-button type="text" 
				@click=changeOrder(2) icon="el-icon-star-off" :class="{checked: order == 2}">感兴趣</el-button>
				<el-button type="text" 
				@click=changeOrder(3) icon="el-icon-user" :class="{checked: order == 3}">牛人进程</el-button>
			  </el-aside>
		      <el-container>
		        <el-main>
					<router-view></router-view>
				</el-main>
		      </el-container>
		  </el-container>
	  </div>
	</div>
</template>

<script>
import TopBar from '@/components/common/TopBar'

import {page} from '@/network/user.js'


export default {
  name: 'Index',
  components: {
    TopBar,
  },
  data(){
	  return{
		  order: 0,
		  isLogin: false
	  }
  },
  activated() {
	if(this.$store.getters.getUser != null && this.$store.getters.getUser.id != null ){
		this.isLogin = true
	}else{
		this.isLogin = false
	}
	this.changeOrder(this.order)
  },
  methods:{
	  changeOrder(order){
		  this.order = order;
		  switch(order){
			  case 0: this.$router.push('/index/recommend')
			  break;
			  case 1: this.$router.push('/index/search')
			  break;
			  case 2: this.$router.push('/index/interested')
			  break;
			  case 3: this.$router.push('/index/process')
			  break;
			  default: this.$message({
					  type: 'error',
					  message: '页面不存在！！！'
					});
		  }
	  },
	
  }
}
</script>
<style >	
	.checked{
		font-weight: 700;
	}
	.main{
		height: 90%;
	}
	.el-aside .el-button{
		width: 100%;
		margin: 0;
		color: #000000;
		font-size: 20px;
	}
  .el-header, .el-footer {
    background-color: #B3C0D1;
    color: #333;
    text-align: center;
    line-height: 100px;
  }
  
  .el-aside {
    background-color: #D3DCE6;
    color: #333;
    text-align: center;
    line-height: 150px;
  }
  
  .el-main {
    background-color: #E9EEF3;
    color: #333;
    text-align: center;
    line-height: 160px;
  }
  
  body > .el-container {
    margin-bottom: 40px;
  }
  .el-container {
    height: 100%;
  }
</style>

