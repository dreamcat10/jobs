import Vue from 'vue';
import VueRouter from 'vue-router';
Vue.use(VueRouter);

const Index = () => import('@/views/index/Index')
// import Index from '@/views/index/Index'
const routes = [
	{
		path: '',
		redirect: '/index'
	},
	{
		path: '/index',
		name: 'index',
		component: Index,
		children: [
			{
				path: 'recommend',
				component:  () => import('@/views/index/child/recommend.vue')
			},
			{
				path: 'search',
				component:  () => import('@/views/index/child/search.vue')
			},
			{
				path: 'interested',
				component:  () => import('@/views/index/child/interested.vue'),
				meta: {
					needLogin: true //需要加校检判断的路由
				}
			},
			{
				path: 'process',
				component:  () => import('@/views/index/child/process.vue'),
				meta: {
					needLogin: true //需要加校检判断的路由
				}
			}
		]
		
	},
	{
		path: '/community',
		component: () => import('@/views/community/Community'),
		meta: {
			needLogin: true //需要加校检判断的路由
		}
	},
	{
		path: '/news',
		component: () => import('@/views/news/News'),
		meta: {
			needLogin: true //需要加校检判断的路由
		}
	},
	{
		path: '/mine',
		component: () => import('@/views/mine/Mine'),
		meta: {
			index: 3
		}
	},
	{
		path: '/login',
		name: 'login',
		component: () => import('@/views/login/Login'),	
	},
	{
		path: '/init',
		name: 'init',
		component: () => import('@/views/login/children/Init.vue')
	},
	{
		path: '/detail/:id',
		name: 'detail',
		component: () => import('@/views/index/child/Detail'),
		meta: {
		    needLogin: true //需要加校检判断的路由
		},
	},
	{
		path: '/postInfo/:id',
		name: 'postInfo',
		component: () => import('@/views/common/post/PostInfo.vue'),
		
	},
	{
		path: '/sendPost',
		name: 'sendPost',
		component: () => import('@/views/common/post/SendPost.vue'),
		
	},
	{
		path: '/newsInfo/:id',
		name: 'newsInfo',
		component: () => import('@/views/common/news/NewsInfo.vue'),
		
	},
	{
		path: '/sendNews',
		name: 'sendNews',
		component: () => import('@/views/common/news/SendNews.vue'),
		
	},
	{
		path: '/newsDetail/:id',
		name: 'newsDetail',
		component: () => import('@/views/community/child/NewsDetail.vue'),			
	},
	{
		path: '/postDetail/:id',
		name: 'postDetail',
		component: () => import('@/views/common/post/PostDetail.vue'),			
	},
	{
		path: '/editInfo',
		name: 'editInfo',
		component: () => import('@/views/mine/EditInfo.vue'),			
	},
	{
		path: '/editProcess',
		name: 'editProcess',
		component:  () => import('@/views/index/child/child/EditProcess.vue'),
	}
	
]

const router = new VueRouter({
	routes,
	mode:'history'
})

export default router