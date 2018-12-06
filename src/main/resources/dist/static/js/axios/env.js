let host = ''; // 域名地址

// 开发环境
if (process.env.NODE_ENV == 'development') {
  host = "http://192.168.1.123:8080";
// 线上环境
}else if(process.env.NODE_ENV == 'production'){
  host = "http://192.168.1.123:8080";
}

export {
  host
}
