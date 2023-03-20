import {request} from "@/network/request.js"
export function login(form){
	return request({
		url: '/emp/logging',
		method: 'post',
		data: {
			'phone': form.phone,
			'code': form.code,
			'password': form.password
		}
	})
} 
export function getCode(phone){
	return request({
		url: '/emp/sendMsg/' + phone
	})
}
export function init(emp){
	return request({
		url: '/emp/init',
		method: 'post',
		data:{
			name: emp.name,
			sex: emp.sex,
			companyId: emp.companyId,
			phone: emp.phone,
			password: emp.password,
			code: emp.code
		}
	})
}