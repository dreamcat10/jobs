import { request } from "@/network/request.js"

export function company(id){
	return request({
		url: '/company/get/' + id,
		method: 'get'
	
	});
}
export function process(id){
	return request({
		url: '/process/summary/' + id,
		method: 'get'
	});
}
export function post(id){
	return request({
		url: '/post/list/' + id,
		method: 'get'
	});
}
export function news(id){
	return request({
		url: '/news/list/' + id,
		method: 'get'
	});
}
export function search(name){
	return request({
		url: '/company/search?name=' + name,
		method: 'get'
	});
}
export function edit(empInfo){
	return request({
		url: '/emp/edit',
		method: 'patch',
		data: {
			avatar: empInfo.avatar,
			name: empInfo.name,
			sex: empInfo.sex,
			password: empInfo.password,
			phone: empInfo.phone,
			companyId: empInfo.companyId,
			id: empInfo.id
		}
	});
}
