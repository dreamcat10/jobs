<template>
	<div class="detail" style="height: 100%;">
		<el-empty :class="{indexActive: isEmpty}" description="网络或服务器错误,请稍后重试!!!"></el-empty>
		<div class="detail-main" style="height: 100%;" :class="{indexActive: !isEmpty}">
			<Header></Header>	
			<div class="user-head">
				<div class="user-head-content">
					<el-avatar :size= "110" :src= "userDetail.avatar"></el-avatar>
					<div class="content-name">
						<p>姓名：{{userDetail.name}}</p>
						<p>意向岗位：{{userDetail.intendedPost}}</p>
					</div>
					<div class="content-info">
						<p>联系方式：{{userDetail.phone}}</p>
						<p>居住地址：{{userDetail.address}}</p>
					</div>
				</div>
			</div>
			<div class="user-main">
				<div class="user-main-content">
					<div class="exp">
						<p>教育经历：</p>
						<el-divider></el-divider>
						<p>{{resume.educationalExp}}</p>
					</div>
					<div class="exp">
						<p>获奖经历：</p>
						<el-divider></el-divider>
					</div>
					<div class="exp">
						<p>工作经历：</p>
						<el-divider></el-divider>
					</div>
					<div class="exp">
						<p>附件简历：</p>
						<el-divider></el-divider>
						<div v-show="resume.resumeAddr != null">{{resume.resumeAddr}} <el-button @click="dialogVisible = true">预览</el-button></div>
						<div v-show="resume.resumeAddr == null">
							<el-empty description="用户暂未上传附件简历"></el-empty>
						</div>
						<el-dialog
						  title="简历浏览"
						  :visible.sync="dialogVisible"
						  width="50%">
						  <div>
							  <iframe :src="path" frameborder="0"
							  style="width: 100%;height: 100%;min-height: 500px;"></iframe>
						  </div>				  
						</el-dialog>
						
					</div>
					<div style="margin-top: 20px; text-align: center; width: 70%; height: 80px;">
						<el-button type="primary" @click=connect()>开始沟通</el-button>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	import {detail,resume} from '@/network/user.js'
	import {addRoom} from '@/network/chat.js'
	
	import Header from '@/components/common/Headr.vue'
	export default{
		name: "Detail",
		data(){
			return{
				id: 0,
				userDetail: '',
				resume: '',
				path: '',
				isEmpty: true,
				dialogVisible: false
			}
		},
		components:{
			Header
		},
		activated() {
			this.id = this.$route.params.id
			this.getDetail(this.id)
		},
		methods:{
			connect(){
				let fromId = this.$store.getters.getUser.id
				addRoom(fromId, this.id).then(res => {
					if(res.data.code == 200){
						this.$router.push("/news")
					}
				})
			},
			getResume(fileName){
				resume(fileName).then(res => {
					this.path = window.URL.createObjectURL(res.data)
				})
			},
			getDetail(id){
				detail(id).then(res => {
					console.log(res.data)
					let record = res.data;
					if(record.code != 200){
						this.$message({
						  type: 'error',
						  message: '网络异常，请稍后重试！！！'
						});
						this.isEmpty = false
					}else{	
						if(record.data == ''){
							this.isEmpty = false
						}else{
							this.isEmpty = true;
							this.userDetail = record.data;
							this.resume = record.data.resume;
							this.getResume(this.resume.resumeAddr)
						}					
					}
				}).catch(err => {
					this.$message({
					  type: 'error',
					  message: '网络异常，请稍后重试！！！'
					});
					this.isEmpty = false
				})
			}
		}
	}
</script>

<style>
	.indexActive{
		display: none;
	}
	.user-head{
		background-color: #e9eef3;
		height: 20%;
	}
	.user-main{
		background-color: #e6f5f8;
	}
	.user-head-content{
		width: 70%;
		height: 100%;
		padding-left: 15%;
		padding-top: 1%;
		display: flex;
	}
	.content-name{
		margin-left: 5%;		
	}
	.content-info{
		margin-left: 35%;
		
	}
	.user-main-content{
		margin-left: 15%;
		border: none;
		padding-top: 20px;
	}
	.exp{
		background-color: #FFFFFF;
		border-radius: 20px;
		width: 70%;
		padding: 20px 30px;
		height: 400px;
		margin-top: 20px;
	}
	.el-divider{
		width: 90%;
		margin-left: 5%;
	}
	p{
		font-size: 24px;
		font-weight: 700;
		font-family: "KaiTi";
	}
</style>
