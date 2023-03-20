import axios from 'axios'
export function request(config){
    const instance = new axios.create({
      // baseURL: 'http://120.77.203.139:80/api',
      baseURL: 'http://localhost:8081/api',
      timeout: 5000
    })
  instance.interceptors.request.use(config => {
    return config
  },
    err => {

    })
  instance.interceptors.response.use(res =>{
    return res
  },
    err => {

    })
  return instance(config)
}
