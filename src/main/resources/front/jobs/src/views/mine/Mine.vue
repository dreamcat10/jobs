<template>
	<div class="app">
		<TopBar :index=3></TopBar>
		<div class="mine-main">
			<div class="mine-info">
				<div class="content-info">
					<el-avatar :size= "110" :src= "empInfo.avatar"></el-avatar>
					<div style="margin-left: 50px;">
						<p>姓名：{{empInfo.name}}</p>
						<p>联系方式：{{empInfo.phone}}</p>
						<el-button type="text" @click="dialogVisible = true" style="padding: 0;font-weight: 700; font-size: 20px;">
							所属公司: {{company.name}}
						</el-button>
						<el-dialog title="公司详情" :visible.sync="dialogVisible" width="40%">
						  <div>
							  <p>公司名称: {{company.name}}</p>
							  <p>公司地址: {{company.address}}</p>
							  <p>创建时间: {{company.createTime}}</p>
							  <p>公司简介: {{company.content}}</p>
						  </div>
						</el-dialog>
						<img style="height: 30px; width: 30px; position: absolute;top: 30px; right: 40px;" 
								src="../../assets/img/tuichu.png" @click="isOutLogin = true"/>
						<el-dialog
						  title="提示"
						  :visible.sync="isOutLogin"
						  width="30%"
						  >
						  <span>确认退出登录？</span>
						  <span slot="footer" class="dialog-footer">
							<el-button @click="isOutLogin = false">取 消</el-button>
							<el-button type="primary" @click="outLogin()">确 定</el-button>
						  </span>
						</el-dialog>
						<el-button type="primary" style="position: absolute;right: 20px; top: 100px;"
						@click="editInfo()">编辑个人资料</el-button>
					</div>
				</div> 
			</div>
			<div class="mine-process">
				 <el-row :gutter="20">
				        <el-col :span="6">
				          <div>
				            <el-statistic group-separator="," :value="process[0]" title="已沟通"></el-statistic>
				          </div>
				        </el-col>
				        <el-col :span="6">
				          <div>
				             <el-statistic group-separator="," :value="process[1]" title="收到简历"></el-statistic>
				          </div>
				        </el-col>
						<el-col :span="6">
						  <div>
						     <el-statistic group-separator="," :value="process[2]" title="邀请面试"></el-statistic>
						  </div>
						</el-col>
						<el-col :span="6">
						  <div>
						     <el-statistic group-separator="," :value="process[3]" title="成功录用"></el-statistic>
						  </div>
						</el-col>
				</el-row>
			</div>
			<div class="mine-fun">
				<p>常用功能</p>
				<div class="fun-list">
					<el-row :gutter="20">
						<el-col :span="3" style="text-align: center;">
							<el-button type="primary" @click="sendPost()">发布岗位</el-button>
						</el-col>		
						<el-col :span="3" style="text-align: center;">
						  <el-button type="primary"  @click="postInfo()">管理岗位</el-button>
						</el-col>
						<el-col :span="3" style="text-align: center;">
						  <el-button type="primary" @click="sendNews()">发布贴子</el-button>
						</el-col>
						<el-col :span="3" style="text-align: center;">
						  <el-button type="primary" @click="newsInfo()">管理贴子</el-button>
						</el-col>
					</el-row>
				</div>
			</div>
			<div class="mine-release">
				<div  @click="postInfo()">
					<p>已发布岗位</p>
					<el-card class="post-card">
					  <div v-for="item in post.slice(0,3)">
						  <div class="post-item">
							  <el-descriptions :column="2" >
							      <el-descriptions-item label="岗位名称">{{item.name}}</el-descriptions-item>
							  	  <el-descriptions-item label="薪资">{{item.salary}}</el-descriptions-item>
							  </el-descriptions>
							  <el-descriptions :column="1">
							      <el-descriptions-item label="任职要求">{{item.content}}</el-descriptions-item>
							      <el-descriptions-item label="发布时间">{{item.createTime}}</el-descriptions-item>
							  </el-descriptions>
						  </div>
					  </div>
					  <p style="margin: 0;">......</p>
					</el-card>
				</div>
				 <el-divider></el-divider>
				<div @click="newsInfo()">
					<p>已发布贴子</p>
					<el-card class="news-card">
					  <div v-for="item in news.slice(0,3)">
						  <div class="news-item">
							  <p>{{item.title}}</p>
							  <span class="text-name">{{item.content}}</span>
							  <p style="font-size: 12px;
										font-weight: 500;">{{item.createTime}}</p>
						  </div>
					  </div>
					  <p style="margin: 0;">......</p>
					</el-card>
				</div>
			</div>
		</div>
	</div>
</template>

<script>	
	import TopBar from "@/components/common/TopBar.vue"
	
	import { mapGetters, mapState, mapActions } from 'vuex'
	import { company,process,news,post } from '@/network/mine.js'
	export default{
		name: "Mine",
		components:{
			TopBar
		},
		computed:{
			...mapState(['empInfo']),
		},
		data(){
			return{
				url: '',
				company: {},
				dialogVisible: false,
				process: [],
				post: [],
				news: [],
				isOutLogin: false,
			}
		},
		activated() {
			this.getCompany()
			this.getProcess()
			this.getPost()
			this.getNews()
		},
		methods:{
			outLogin(){
				this.isOutLogin = false
				this.$store.commit('REMOVE_INFO')
				this.$router.replace('/')
			},
			editInfo(){
				this.$router.push('/editInfo')
			},
			getPath(path){
			  getImg(path).then(res => {
				 this.url = res
			  })
			},
			getUrl(path){
			  this.getPath(path)
			  return this.url
			},
			getNews(){
				news(this.empInfo.id).then(res => {
					if(this.handlerCode(res))
					this.news = res.data.data
				})
			},
			getPost(){
				post(this.empInfo.id).then(res => {
					if(this.handlerCode(res))
					this.post = res.data.data
				})
			},
			getCompany(){
				company(this.empInfo.companyId).then(res => {
					if(this.handlerCode(res))
					this.company = res.data.data
				})
			},
			getProcess(){
				process(this.empInfo.id).then(res => {
					if(this.handlerCode(res))
					this.process = res.data.data
				})
			},
			handlerCode(res){
				let recode = res.data
				if(recode.code == 200){
					return true;
				}else{
					this.$message({
					  message: '网络错误',
					  type: 'error'
					});
					return false;
				}
			},
			sendPost(){
				this.$router.push('/sendPost')
			},
			sendNews(){
				this.$router.push('/sendNews')
			},
			postInfo(){
				this.$router.push({
					name: 'postInfo',
					params: {
						id: this.empInfo.id
					}
				})
			},
			newsInfo(){
				this.$router.push({
					name: 'newsInfo',
					params: {
						id: this.empInfo.id
					}
				})
			}
		}
	}
</script>

<style scoped>
	.content-info{
		margin: 0;
		display: flex;
		padding: 40px;
		background-color: #e9eef3;
		position: relative;
	}
	.content-info p{
		font-weight: 700;
		font-size: 20px;
	}
	.mine-process{
		height: 80px;
		background-color:#e9eef3 ;
		margin-top: 15px;
		padding-top: 1px;
	}
	.mine-fun{
		background-color:#e9eef3 ;
		margin-top: 15px;
		padding: 20px;
	}
	.mine-release{
		background-color:#e9eef3 ;
		margin-top: 15px;
		padding: 20px;	
	}
	p{
		font-size: 24px;
		font-weight: 700;
		font-family: "KaiTi"
	}
	.post-item{
		margin-bottom: 15px;
		border: #e7e7e7 solid 1px;
		border-radius: 4px;
	}
	.news-item{
		margin-bottom: 15px;
		border: #e7e7e7 solid 1px;
		border-radius: 4px;
	}
	.text-name{
		  display: inline-block;
		  width: 77%;
		  overflow: hidden;
		  white-space: nowrap;
		  text-overflow: ellipsis;
	}
</style>
