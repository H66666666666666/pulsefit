import axios from "axios";


//创建axios实例对象
const request =axios.create({
  baseURL:'/api',
  timeout:600000
})

//axios的请求拦截器：自动携带token
request.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('pulsefit_token')
    if (token) {
      config.headers['token'] = token
    }
    return config
  },
  (error) => Promise.reject(error)
)

//axios的响应 response 拦截器
request.interceptors.response.use(
  (response) => { //成功回调
    return response.data
  },
  (error) => { //失败回调
    return Promise.reject(error)
  }
)

export default request