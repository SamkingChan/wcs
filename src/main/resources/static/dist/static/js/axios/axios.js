import Vue from 'vue';
import axios from 'axios';
import { host } from "./env.js";
// 请求前拦截
axios.interceptors.request.use(
  config => {
    let token = localStorage.getItem("token");
    if (token) {  // 判断是否存在token，如果存在的话，则每个http header都加上token
      config.headers["XX-Token"] = `${token}`;
      config.headers["XX-Device-Type"] = "wxapp";
    }
    if (config.url.indexOf(host) === -1) {
      config.url = host + config.url;/*拼接完整请求路径*/
    }
    return config;
  },
  err => {
    return Promise.reject(err);
  })

export default async(url ='', data = {}, method = 'GET') => {
  method = method.toUpperCase();
  var obj = {};

  if (method == 'GET') {
    obj.params = data;
  } else if (method == 'POST') {
    obj.data = data;
  }
  obj.method = method;
  obj.url = url;

  return new Promise((resolve, reject) => {
    axios(obj).then(response => {
      if (response.status == 200) {
        resolve(response.data);
        /*if (response.result == 1) {
          if(response.data != '') {
            resolve(response.data);
          }
        } else {
          resolve(response.data);
        }*/
      } else {
        reject(response);
      }
    }).catch(err => {
      if(err.response.status == 401) {
        resolve(err.response.data);
      }else if(err.response.status == 402){
        resolve(err.response.data);
      } else {
        reject(err);
      }
    });
  })

}
