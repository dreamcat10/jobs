<template>
	<div style="height: 81%;">
		<el-empty :class="{indexActive: isEmpty}" description="暂无数据" ></el-empty>
		<div class="data" :class="{indexActive: !isEmpty}">
			<ul class="infinite-list" style="overflow: auto; height: 100%;" ref="ul"> 
			  <li v-for="user in userInfo" class="infinite-list-item">
				<el-descriptions class="margin-top" :column="3">
				  <template slot="title" >
					  <el-avatar :size= "70" 
					  :src= "user.avatar"></el-avatar>
					  <span style="margin-left: 30px;">{{user.name}}</span>
			      </template>
				  <template slot="extra" >
					<el-button style="margin-top: 150px;" type="primary" size="small" @click="detail(user.id)">了解详情</el-button>
				  </template>
				  <el-descriptions-item label="年龄" >{{user.age}}</el-descriptions-item>
				  <el-descriptions-item label="手机号">{{user.phone}}</el-descriptions-item>
				  <el-descriptions-item label="居住地">{{user.address}}</el-descriptions-item>
				  <el-descriptions-item label="最高学历">{{user.degree}}</el-descriptions-item>
				  <el-descriptions-item label="意向岗位">{{user.intendedPost}}</el-descriptions-item>
				</el-descriptions>
			  </li>
			  <el-pagination @current-change="changeCurrent"
			    background
			    layout="prev, pager, next"
				:current-page.sync="currentIndex"
			    :total="total" :page-size="pageSize">
			  </el-pagination>
			</ul>
		</div>
	</div>
</template>

<script>
	export default{
		name: "Data",
		props:{
			userInfo: {
				type: Array,
				default: function(){
					return []
				}
			},
			currentPage: {
				type: Number,
				default: 1
			},
			isEmpty: {
				type: Boolean,
				default: true
			},
			total: 0,
			pageSize: 0
		},
		computed:{
			currentIndex: {
				get(){
					return this.currentPage;
				},
				set(v){
					this.$emit("on-change-currentPage", v);
				}
			}
		},
		methods:{
			changeCurrent(current){
				this.$refs.ul.scrollTop = 0
				this.$emit('changeCurrent', current)
			},
			detail(id){
				this.$router.push({
					name: 'detail',
					params: {
						id
					}
				})
			}
		}
	}
</script>

<style>
	.data{
		overflow: hidden;
		height: 100%;
	}
	.el-descriptions__header{
		height: 80px;
	}
	.el-descriptions__title{
		line-height: 20px;
	}
	.el-descriptions{
		margin-top: 20px;
	}
	.el-pagination{
		margin-top: 35px;
		height: 30px;
	}
</style>
