<template>
	<div class="app">
		<TopBar :index=1></TopBar>
		<div class="main">
			<div class="ul">
				<ul class="infinite-list" v-infinite-scroll="load"
							style="overflow:auto" :infinite-scroll-disabled="!isLoading">
					<div>
						<el-button type="text" :class="{'news-btu':type == 1}" @click=changeType(0)>点赞数</el-button>
						<el-button type="text" :class="{'news-btu':type == 0}" @click=changeType(1)>发布时间</el-button>
					</div>
					<li v-for="news in newsInfo" class="news-item">
						<el-divider></el-divider>
					  <div class="news-item">
						  <div class="item-info">
							  <el-avatar :size= "50" :src= "news.createAvatar"></el-avatar>
							  <p style="margin-left: 20px; line-height: 10px;">{{news.createName}}</p>
						  </div>
						<div class="item-left">
							<p @click="detail(news)">{{news.title}}</p>
							<span class="text-name">{{news.content}}</span>
							<div style="display: flex;">
								 <span @click="like(news)" class="like">
									 <img src="../../assets/img/dianzan-off.png" v-show="!news.isStart"/>
									<img src="../../assets/img/dianzan-on.png" v-show="news.isStart"></img>
								 </span>
								 <span class="like">{{news.starts}}</span>
								<p style="font-size: 14px;font-weight: 500;margin-left: 76%;">{{news.createTime}}</p>
							</div>
						</div>
					  </div>
					</li>
					<p v-if="!isLoading" style="text-align: center;">没有更多了</p>
					<!-- <p v-else><i class="el-icon-loading"></i>加载中</p> -->
				</ul>
			</div>
		</div>
	</div>
</template>

<script>
	import TopBar from "@/components/common/TopBar.vue"
	
	import { mapState} from 'vuex'
	import {news,start} from '@/network/news.js'
	export default{
		name: "Community",
		data(){
			return{
				newsInfo: [],
				size: 3,
				type: 0,
				current: 0,
				total: 0,
				isLoading: true,
				types: ['点赞数','发布时间']
			} 
		},
		computed:{
			...mapState(['empInfo']),
		},
		components:{
			TopBar
		},
		mounted() {
			
		},
		methods:{
			changeType(type){
				this.type = type
				this.current = 0
				this.isLoading = true
				this.newsInfo = []
				this.load()
			},
			like(news){
				news.isStart = !news.isStart
				if(news.isStart){
					news.starts += 1;
				}else{
					news.starts -= 1;
				}
				start(news, this.empInfo.id)
			},
			detail(news){
				this.$router.push({
					name: 'newsDetail',
					params:{
						id: news.id
					}
				})
			},
			load(){
				if(this.isLoading){
					this.isLoading = false
					this.current += 1;
					news(this.empInfo.id,this.current,this.size,this.type).then(res => {
						if(res.data.code == 200){
							console.log(res.data.data)
							this.newsInfo = this.newsInfo.concat(res.data.data.recodes);
							this.total = res.data.data.total;
							this.isLoading = this.size*this.current > this.total ? false : true
						}
					})
				}
			},
		}
	}
</script>

<style>
	.main{
		height: 90%;
		background-color: #e9eef3;
		position: relative;
	}
	.ul{
		position: absolute;
		margin: 20px 10%;
		height: 90%;
		width: 80%;
		padding: 20px 40px;
		border-radius: 30px;
		background-color: #FFFFFF;
	}
	.infinite-list{
		height: 93%;
		list-style-type: none;
	}
	.news-item p{
		font-size: 24px;
		font-weight: 700;
		font-family: "KaiTi"
	}
	.item-info{
		display: flex;
		margin-top: 20px;
		height: 40px;
	}
	.text-name{
		  display: inline-block;
		  width:900px;
		  overflow: hidden;
		  white-space: nowrap;
		  text-overflow: ellipsis;
	}
	.like {
	    cursor: pointer;
	    font-size: 15px;
	    display: inline-block;
		margin-top: 17px;
	  }
	.like img{
		height: 15px;
		width: 15px;
	}
	.news-item{
		border: 0;
	}
	.news-btu{
		color: #000000;
	}
</style>
