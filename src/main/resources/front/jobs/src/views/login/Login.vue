<template>
	<div>
		<div class="login">
			<div class="login-head">
				<div class="left" style="margin-top: 25px;">
					<el-page-header @back="goBack" title="返回首页">
					</el-page-header>
				</div>
				<div class="right" style="width: 83%;">Jobs招聘</div>
				</div>
			<div class="login-main">
				<div class="login-box">
					<div class="box-head">
						<el-tooltip :class="{active: isChange}" effect="light" content="使用密码登录" placement="right-start">
						      <el-button icon="el-icon-key" round @click=change()></el-button>
						</el-tooltip>
						<el-tooltip style="margin: 0;" :class="{active: !isChange}" effect="light" content="使用验证码登录" placement="right-start">
						      <el-button icon="el-icon-mobile-phone" round @click=change()></el-button>
						</el-tooltip>
						<div class="text">登录</div>
						<div class="ma-context" :class="{active: isChange}">
							<el-form ref="form1" :rules="rules" :model="form1" label-width="80px">
								<el-form-item prop="phone">
									<el-input v-model="form1.phone" placeholder="手机号"></el-input>
								</el-form-item>
								<el-form-item prop="code">
									<el-input v-model="form1.code"  placeholder="短信验证码">
										<el-button slot="append" type="primary" 
										:disabled="isDisable" @click="sendCode('form1',form1.phone)">{{buttonName}}</el-button>									
									</el-input>
								</el-form-item>
								 <el-form-item style="text-align: center;">
									<el-button type="primary" @click="submitCode('form1')" :loading="isLoading">登录</el-button>
								</el-form-item>
								<el-form-item prop="checked">
									 <el-checkbox v-model="form1.checked">已阅读并同意《用户服务协议》和《隐私政策》</el-checkbox>
								</el-form-item>
								<el-form-item>
									<el-link href='/init' type="primary" :underline="false">没有账号？ 立即注册</el-link>
								</el-form-item>
							</el-form>
						</div>
						<div class="mi-context" :class="{active: !isChange}">
							<el-form ref="form2" :rules="rules" :model="form2" label-width="80px">
								<el-form-item prop="phone">
									<el-input v-model="form2.phone" placeholder="手机号"></el-input>
								</el-form-item>
								<el-form-item prop="password">
									<el-input :show-password="true" v-model="form2.password"  placeholder="密码">								
									</el-input>
								</el-form-item>
								 <el-form-item style="text-align: center;">
									<el-button type="primary" @click="submitPass('form2',form2.phone)" :loading=isLoading>登录</el-button>
								</el-form-item>
								<el-form-item prop="checked">
									 <el-checkbox v-model="form2.checked">已阅读并同意《用户服务协议》和《隐私政策》</el-checkbox>
								</el-form-item>
								<el-form-item>
									<el-link href='/init' type="primary" :underline="false">没有账号？ 立即注册</el-link>
									<el-link style="margin-left: 40px;" href='/' type="primary" :underline="false">忘记密码？</el-link>
								</el-form-item>
							</el-form>
						</div>
					</div>
				</div>
			</div>
		</div>
		<router-view></router-view>
	</div>
</template>

<script>
	import {login,getCode} from "@/network/login.js"
	import {chatRooms} from "@/network/chat.js"
	import {createWebSocket} from "@/api/websocket.js"
	
	export default{
		name: "Login",
		data(){
			return{
				form1: {
					phone: '',
					code: '',
					checked: [],
				},
				form2: {
					phone: '',
					password: '',
					checked: [],
				},
				rules: {
				  phone: [
					{ required: true, message: '请输入手机号', trigger: 'change'},
					{ len: 11, message: '请填写正确的手机号' , trigger: 'change'}
				  ],
				  code: [
					{ required: true, message: '请输入验证码', trigger: 'change'}
				  ],
				  checked: [
					{required: true , message: '请阅读并勾选上方协议',trigger: 'change'}
				   ],
				   password: [
					   {required: true , message: '密码不能为空',trigger: 'change'}
				   ]
				 },
				 isLoading: false,
				 isChange: false,
				 isDisable: false,
				 buttonName: '获取验证码',
				 time: 3,
				 timer: null,
				 logMap: []
			}
		},
		methods:{
			goBack(){
				this.$router.replace("/")
			},
			change(){
				this.isChange = !this.isChange;
			},
			sendCode(formName,phone){			
			    this.$refs[formName].validateField('phone',(msg) => {
					if(msg == ''){
						this.isDisable = true;
						getCode(phone).then((res) => {
							console.log(res);
							});
						this.countDown();
						this.time = 3
					}					
				})			
							
			},			
		    countDown() {
				if (!this.timer) {
				this.timer = setInterval(() => {
				  if (this.time > 0 && this.time <= 60) {
					this.buttonName = this.time + '秒后重新获取';
					this.time--;
				  } else {
					this.isDisable = false;
					this.buttonName = '获取验证码';
					clearInterval(this.timer);
					this.timer = null;
				  }
				}, 1000);
			  }
		    },
			submitCode(formName) {
				let num = 0;
				this.$refs[formName].validateField(['phone','checked','code'],(msg) => {
					if(msg == ''){
						num++;
					}					
				})
				if(num == 3){
					this.isLoading = true;
					this.getEmp(this.form1)
				}
			},
			submitPass(formName) {
				let num = 0;
				this.$refs[formName].validateField(['phone','checked','password'],(msg) => {
					if(msg == ''){
						num ++;
					}
				})	
				if(num == 3){
					this.isLoading = true;
					this.getEmp(this.form2)
				}
			},
			getEmp(form){
				login(form).then((res) => {
					this.isLoading = false;
					let data = res.data;
					if(data.code == 200){							
						let empInfo = data.data;
						console.log(data)
						this.$store.commit('SET_USERINFO',empInfo);
						let id = empInfo.id;
						createWebSocket(this.callback,id);
						this.chat(id);
						this.$router.replace('/');
					}else{
						this.$message.error(data.msg);
					}
				});
			},
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
					console.log(res)
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

<style scoped>
	.active{
		display: none;
	}
	.login{
		height: 100vh;
	}
	.login-head{
		display: flex;
		height: 10%;
		width: 100%;
		background-color: #B3C0D1;
		line-height: 70px;
		text-align: center;
		color: white;
		font-size: 60px;
		font-weight: 700;
		font-family: "华文行楷";
	}
	.login-main{
		height: 90%;
		position: relative;
	}
	.item{
		margin: 0;
	}
	.login-box{
		height:80%;
		width:30%;
		border-radius: 20px;
		box-shadow: 2px 2px 2px #E5E5E5, -2px -2px 2px #E5E5E5; 
		background-color:#ffffff;
		position:absolute;
		top:15%;
		left:35%;
	}
	.box-head{
		width: 100%;
		
	}
	.text{
		margin-top: 40px;
		text-align: center;
		line-height: 60px;
		font-size: 20px;
	}
	.ma-context{
		margin-top: 50px;
		margin-left: 20px;
		width: 70%;
	}
	.mi-context{
		margin-top: 50px;
		margin-left: 20px;
		width: 70%;
	}
	
</style>
