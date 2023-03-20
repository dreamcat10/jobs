<template>
	<div class="chat-room">
		<div class="chat-list" >
			<ul class="ul-scroll" ref="chatLogList" @scroll="scrollEvent">
				<li v-show="page <= MaxPage" style="height: 80px;">
					<el-button type="text" icon="el-icon-time" :loading="isLoading" @click="getHistory()">点击查看更多消息</el-button>
				</li>
				<li v-show="isShow" style="height: 80px;">
					<el-button type="text" @click="scrollToBottom()">点击返回底部</el-button>
				</li>
				<li class="ul-scroll-item" v-for="item in concatList">
					<div class="log-item-left" v-if="item.fromId == room.otherId">
						<el-avatar :size="50" :src="room.oAvatar"></el-avatar>
						<div class="log-detail-left" v-html="item.context.replace(/\\n/g, '<br/>')">
						</div>
					</div>
					<div class="log-item-right" v-else ="item.fromId != room.otherId">
						<div style="display: flex; margin-left: auto;">
							<div class="log-detail-right">
								<span v-html="item.context.replace(/\\n/g, '<br/>')"></span>
							</div>
							<el-avatar :size="50" :src="empInfo.avatar"></el-avatar>
						</div>
					</div>
				</li>
				<li style="height: 20px;"></li>
			</ul>
			<el-backtop target="ul.ul-scroll" :bottom="162" :right="165" style="width: 20px; height: 20px;"></el-backtop>
		</div>
		<div class="chat-text">
			<textarea @keyup.enter="send(context)" v-model="context"></textarea>
			<el-button type="primary" class="text-btu" @click="send(context)">发送</el-button>
		</div>
	</div>
</template>

<script>
	import { mapGetters, mapState, mapActions } from 'vuex'
	import {chatLogs} from'@/network/chat.js'
	import {sendSock} from '@/api/websocket.js'
	
	export default{
		name:'ChatRoom',
		data(){
			return{
				context: '',
				page: 1,
				total: 1,
				size: 1,
				dbLogs: [], //离线前接收的消息
				list: [],
				isLoading: false,
				scrollFlag: true,
				lastScrollHeight: ''
			}
		},
		props:{
			room: {
				type: Object
			},
		},
		computed:{
			...mapState(['chatLogs','empInfo']),		
			logList(){ //离线后接收的消息
				let logs = []
				if(this.chatLogs != null){
					this.chatLogs.forEach(item =>{
						if(item.roomId == this.room.id){
							logs = item.logs;
							this.removeLog()
						}
					})
					this.scrollFlag = true
				}
				return logs
			},
			MaxPage(){
				return Math.ceil(this.total / this.size); 
			},
			isShow(){
				return this.page >this.MaxPage && this.$refs.chatLogList.scrollHeight > 600
			},
			concatList: {
				get(){					
					let logs = this.list
					this.dbLogs.forEach(item => {
						logs.unshift(item)
					})
					this.dbLogs = []
					logs = logs.concat(this.logList)
					this.list = logs
					return logs
				}
			},
		
		},
		watch:{	
			room(val){
				this.getChatLogs()				
			},
		},
		activated() {
			this.$refs.chatLogList.scrollTop = this.$refs.chatLogList.scrollHeight 
		},
		mounted() {		
			if(this.room.id != null){
				this.getChatLogs()
			}
			if(this.scrollFlag){
				console.log('bottom')
				this.$refs.chatLogList.scrollTop = this.$refs.chatLogList.scrollHeight
			}
		},
		updated() {
			if(this.scrollFlag){
				this.scrollToBottom()
			}else{
				this.scrollUnchange()
			}		
		},
		
		methods:{
			//发送消息
			send(val){
				this.context = ''
				let context = JSON.stringify(val)
				let msg = '{"toId":"'+ this.room.otherId +'","contentText":'+ context +',"roomId":"'+ this.room.id +'"}'
				sendSock(msg)
				console.log(msg)
			},
			scrollEvent(e){
				if(e.srcElement.scrollTop == 0 && this.page <= this.MaxPage){
					this.lastScrollHeight = e.srcElement.scrollHeight
					setTimeout(() =>{
						this.getHistory()
					},500)
				}
			},
			removeLog(){
				this.$store.commit('REMOVE_LOGS',this.room.id)
			},
			//滚动条保持最底部方法
			scrollToBottom () {
			  this.$nextTick(() => {
				  //回到底部需要滚动的长度
					let top = this.$refs.chatLogList.scrollHeight - this.$refs.chatLogList.scrollTop
			  
					// 实现滚动效果 			  
					const timeTop = setInterval(() => {			  
					  this.$refs.chatLogList.scrollTop += 80;  
					  top -= 50;
					  if (top <= 0) {		  
						clearInterval(timeTop); //清除定时器
					  }
					}, 10);
			  })
			},
			//页面保持原位置
			scrollUnchange () {
			  this.$nextTick(() => {
				  this.$refs.chatLogList.scrollTop = this.$refs.chatLogList.scrollHeight - this.lastScrollHeight + 80
			  })
			},
			getHistory(){
				this.isLoading = true
				this.scrollFlag = false
				this.getChatLogs()
			},
			getChatLogs(){
				chatLogs(this.room.id, this.page).then(res => {
					console.log(res.data)
					let data = res.data
					if(data.code == 200){
						this.dbLogs = data.data.recodes
						this.total = data.data.total
						this.size = data.data.size
						this.page += 1
						setTimeout(() => {
							this.isLoading = false
						},500)
						console.log('page,max',this.page,this.MaxPage)
					}
				}).catch(err => {
					this.$message({
					  type: 'error',
					  message: '网络异常，请稍后重试！！！'
					});
				})
			},
		}
	}
</script>

<style>
	 .text-btu{
		position: absolute;
		bottom: 5px;
		right: 100px;
	}
	  textarea {
	    box-sizing: border-box;
	    padding: 10px 30px 0 30px ;
	    height: 110px;
	    width: 100%;
	    border: none;
	    outline: none;
	    font-family: 'Micrsofot Yahei';
	    resize: none;
		vertical-align: top; 
		background-color: #F9FAFD;
	  }

	.chat-room{
		height: 100%;
	}
	.chat-list{
		box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)
	}
	.ul-scroll{
		list-style: none;
		margin: 0px;
		padding: 0;
		overflow: auto;
		display: inline-block;
		white-space: nowrap;
		height: 100%; 
		width: 100%;
		background: #F9FAFD;
		border-radius: 5px;
	}
	.ul-scroll-item{
		width: 100%;
	}
	.log-item-left{
		display: flex;
		width: 100%;
	}
	.log-item-right{
		display: flex;
		width: 100%;
	}
	.log-detail-left {
		background-color: #e5e5e5;
		border-radius: 4px;
		position: relative;
		margin-top: 54px;
		margin-left: 10px;
		padding: 10px;
		text-align: left;
	}
	.log-detail-left::after{
		content: '';
		border-top: 30px solid #e5e5e5;
		border-right: 15px solid transparent;
		transform: rotate(113deg);
		position: absolute;
		top: 0px;
		left: -16px;
	}
	.log-detail-right {
		background-color: #12b7f5;
		border-radius: 4px;
		position: relative;
		margin-top: 54px;
		margin-left: 10px;
		padding: 10px;
		color: #FFF;
		text-align: left;
	}
	.log-detail-right::after{
		content: '';
		border-top: 30px solid #12b7f5;
		border-right: 15px solid transparent;
		transform: rotate(216deg);
		position: absolute;
		top: 0px;
		right: -10px;
	}
	.chat-list{
		height: 80%;
		overflow: hidden;
		line-height: 20px;
		text-align: center;
	}
	.chat-text{
		height: 20%;
		background-color: #F9FAFD;
		border-top: 1px solid #dedede;
		position: relative;
	}
  .ul-scroll::-webkit-scrollbar {
	width: 10px;
	background-color: #f9fafd;
  }
  .ul-scroll::-webkit-scrollbar-thumb {
	border-radius: 5px;
	background-color: #e9eef3;
  }
/* .ul-scroll::-webkit-scrollbar-button {
	width: 10px;
	background-color: #d3dce6;
	
  } */
  .ul-scroll::-webkit-scrollbar-track {
	border-radius: 5px;
  }

</style>
