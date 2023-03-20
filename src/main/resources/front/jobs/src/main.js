import Vue from 'vue'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import App from './App.vue'
import router from './router'
import axios from 'axios'
import store from './store'
import VueQuillEditor from 'vue-quill-editor'
 
// 引入样式
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'
 
Vue.use(VueQuillEditor)
axios.defaults.withCredentials=true;//让ajax携带cookie
Vue.use(ElementUI);
Vue.config.productionTip = false
//添加事件总线对象
Vue.prototype.$bus = new Vue()
Vue.prototype.$axios = axios

router.beforeEach(function(to, from, next) {
  if (to.meta.needLogin) {
    //页面是否登录
    if (store.getters.getUser != null && store.getters.getUser.id != null) {
      //本地存储中是否有token(uid)数据
      next(); //表示已经登录
    } else {
      //next可以传递一个路由对象作为参数 表示需要跳转到的页面
      next({
        name: "login"
      });
	  setTimeout('console.clear()', 300)
    }
  } else {
    //表示不需要登录
    next(); //继续往后走
  }
});

new Vue({
	router,
	axios,
	store,
  render: h => h(App),
}).$mount('#app')
