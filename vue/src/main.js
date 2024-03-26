import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import axios from "axios";


Vue.use(Loading.directive);

Vue.prototype.$http = axios
Vue.use(ElementUI);


new Vue({
  router,
  el: '#app',
  render: h => h(App)
}).$mount('#app')
