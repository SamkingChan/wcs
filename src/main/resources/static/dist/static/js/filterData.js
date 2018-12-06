export  function filetime(res) {
  return res.getFullYear() + '-' + add(res.getMonth() + 1) + '-' + add(res.getDate()) + ' ' + add(res.getHours()) + ':' + add(res.getMinutes()) + ':' + add(res.getSeconds());
}
export function  add(m) {
  return m < 10 ? "0" + m : m;
}
