import Vue from "vue"
import Vuex from "vuex"

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    empInfo: JSON.parse(sessionStorage.getItem("empInfo")),
	chatRooms: JSON.parse(sessionStorage.getItem("chatRooms")),
	chatLogs: JSON.parse(sessionStorage.getItem("chatLogs")),
  },
  mutations: {
    
    SET_USERINFO: (state, empInfo) => {
      state.empInfo = empInfo
      sessionStorage.setItem("empInfo", JSON.stringify(empInfo))
    },
    REMOVE_INFO: (state) => {
      state.empInfo = {}
      sessionStorage.setItem("empInfo", JSON.stringify(''))
    },
	SET_CHATROOMS: (state, chatRooms) => {
		state.chatRooms = chatRooms
		sessionStorage.setItem("chatRooms", JSON.stringify(chatRooms))
	},
	SET_CHATLOGS: (state, chatLogs) => {
		state.chatLogs = chatLogs
		sessionStorage.setItem("chatLogs", JSON.stringify(chatLogs))
	},
	SET_CHATLOG: (state, log, id) => {
		state.chatLogs.forEach(item => {
			if(item.roomId == id){
				item.logs.push(log)
			}
		})
		sessionStorage.setItem("chatLogs", JSON.stringify(state.chatLogs))
	},
	REMOVE_LOGS: (state, id) => {
		state.chatLogs.forEach(item => {
			if(item.roomId == id){
				item.logs = []
			}
		})
		sessionStorage.setItem("chatLogs", JSON.stringify(state.chatLogs))
	}

  },
  getters: {
    // get
    getUser: state => {
      return state.empInfo
    },
	getRooms: state => {
		return state.chatRooms
	},
	getLogs: state => {
		return state.chatLogs
	}

  },
  actions: {
  },
  modules: {
  }
})
