<template>
	<div>
		<ul>
			<li v-for="(room,index) in rooms">
				<div :class="{click: index == indexNum}" class="room" @click=toChat(index)>
					<el-avatar :size= "50" :src= "room.oAvatar"></el-avatar>
					<div class="room-name">
						<p>{{room.oName}}</p>
					</div>
				</div>				
			</li>
		</ul>
	</div>
</template>

<script>
	export default{
		name: "UserList",
		props:{
			rooms: {
				type: Array
			}
		},
		data(){
			return{
				indexNum: 0
			}
		},
		methods:{
			toChat(index){
				this.indexNum = index
				this.$emit('toChat', this.indexNum)
			}
		}
	}
</script>

<style>
	.click{
		box-shadow: 0 2px 12px 0 rgba(118, 118, 118, 0.5);
		border: 5px solid #ffffff;
		
	}
	ul{
		list-style-type: none;
		margin: 0;
		padding: 0 10px;
	}
	.room{
		background-color: white;
		border-radius: 8px;
		display: flex;
		margin-top: 10px;
	}
	.el-avatar{
		margin: 10px;
	}
	.room-name{
		line-height: 40px;
		margin-left: 20px;
		font-family: "Microsoft YaHei";
		font-weight: 600;
	}
</style>
