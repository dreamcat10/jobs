<template>
  <keep-alive>
        <router-view />
 </keep-alive>
</template>

<script>
import {chatRooms} from "@/network/chat.js"
import {createWebSocket} from "@/api/websocket.js"

export default {
  name: 'app',
  created() {
	 let emp = sessionStorage.getItem("empInfo")
  	if(emp != null){
	    emp = JSON.parse(emp);
		createWebSocket(this.callback, emp.id)
		console.log(1)
		this.chat(emp.id)
	}
  },

  data(){
	  return{
		  logMap: [],
	  }
  },
  methods: {
	  callback(msg){
		this.logMap = this.$store.getters.getLogs
		this.logMap.map(item => {
			if(msg.chatRoomId == item.roomId){
				item.logs.push(msg)
			}
		})
		this.$store.commit('SET_CHATLOGS',this.logMap);
	  },
	  chat(id){
		chatRooms(id).then(res => {
			let data = res.data
			if(data.code == 200){
				let rooms = data.data
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
	@import "./assets/css/font.css";
	@import "./assets/css/global.css";
	body{
		height: 100vh;
		margin: 0;
	}
	
</style>

