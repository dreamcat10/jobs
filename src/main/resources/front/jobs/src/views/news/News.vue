<template>
	<div class="app">
		<TopBar :index=2></TopBar>
		<el-container style="height: 90%;">
		  <el-aside width="200px">
			  <UserList :rooms="rooms" @toChat="toChat"></UserList>
		  </el-aside>
		  <el-main>
			  <ChatRoom :room=room :key="index"></ChatRoom>
		  </el-main>
		</el-container>
	</div>
</template>

<script>
	import TopBar from "@/components/common/TopBar.vue"
	import UserList from "@/components/content/news/UserList.vue"
	import ChatRoom from "@/components/content/news/ChatRoom.vue"
	import {chatRooms} from "@/network/chat.js"
	export default{
		name: "News",
		data(){
			return{
				rooms: [],
				room: {},
				index: 0,
				emp: {},
				logMap: [],
			}
		},
		activated() {			
			this.chat(this.$store.getters.getUser.id)
			this.toChat(this.index)
		},
		components:{
			TopBar,
			UserList,
			ChatRoom
		},
		methods:{
			toChat(index){
				this.index = index
				this.room = this.rooms[index]
			},
			 async chat(id){
				await chatRooms(id).then(res => {
					let data = res.data
					if(data.code == 200){
						let rooms = data.data
						this.rooms = rooms
						this.$store.commit('SET_CHATROOMS',rooms);
						rooms.map(item => {
							let log = {
								roomId: '',
								logs: []
							};
							log.roomId = item.id,
							log.logs = []
							this.logMap.push(log)
						})
						this.$store.commit('SET_CHATLOGS',this.logMap);
					}else{
						console.log("获取聊天室信息失败")
					}
				})
			},
		}
	}
</script>

<style> 
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
	padding-right:0;
  }
  .el-container {
    height: 100%;
  }
</style>
