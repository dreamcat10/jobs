import { request } from "@/network/request.js"

export function chatRooms(id){
	return request({
		url: '/chat/rooms/' + id,
		method: 'get'
	
	});
}

export function chatLogs(roomId, current){
	return request({
		url: '/chat/logs/' + roomId +'/'+current,
		method: 'get'
	
	});
}
export function addRoom(roomId, toId){
	return request({
		url: '/chat/room/' + roomId +'/'+toId,
		method: 'post'
	
	});
}