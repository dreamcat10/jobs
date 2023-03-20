import { request } from "@/network/request.js"

export function page(current, term){
	return request({
		url: '/user/page/' + current,
		method: 'post',
		data: {
			'degree': term.degree,
			'addr': term.addr,
			'time': term.time,
			'post': term.post[1]
		}
	});
}
export function search(current,name, term){
	return request({
		url: '/user/page/' + current + '/' + name,
		method: 'post',
		data: {
			'degree': term.degree,
			'addr': term.addr,
			'time': term.time,
			'post': term.post[1]
		}
	});
}
export function inter(current, term){
	return request({
		url: '/user/page/inter/' + current ,
		method: 'post',
		data: {
			'degree': term.degree,
			'addr': term.addr,
			'time': term.time,
			'post': term.post[1]
		}
	});
}

export function detail(id){
	return request({
		url: '/user/detail/' + id ,
		method: 'get'
	});
}

export function resume(fileName){
	return request({
		url: '/common/down/resume/' + fileName ,
		method: 'get',
		responseType: 'blob'
	});
}
export function getAvatar(path){
	return request({
		url: '/common/down/img/' + path,
		method: 'get',
		responseType: 'blob'
	});
}
export function process(id){
	return request({
		url: '/process/list/' + id,
		method: 'get'
	});
}

export function putProcess(process){
	return request({
		url: '/process/change',
		method: 'put',
		data: {
			id: process.id,
			connect: process.connect,
			resume: process.resume,
			interview: process.interview,
			employment: process.employment
		}
	});
}