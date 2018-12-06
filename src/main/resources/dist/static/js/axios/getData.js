import axios from './axios'
/*  入库  */
export const inbound = () => axios('/data/getInOrder/A15103796');
/*  入库查询  */
export const incheck = (data) => axios('/data/getInOrder/'+data);
/*  出库 */
export const outbound = () => axios('/data/product/out/AH60100015');
/*  出库查询  */
export const outCheck = (data) => axios('/data/product/out/A15103796',data,'POST');
/*  登录  */
export const login = (data) => axios('/user/login/',data,'POST');
/*  打印  */
export const printAll = (data) => axios('/printer/printAll',data,'POST');
/*  保存入库打印信息  */
export const saveInbound =(data) => axios('/data/saveData/10/CAD7716412D69132E8E072317A98B0EA',data,'POST');
/*  校验查询  */
export const checkVerrify = (data) => axios('/data/getValidateInOrder/'+data);
/*  校验新增  */
export const addList = (data) => axios('/data/getInOrderByEpc/'+data);
/*  开始校验  */
export const openCheck  = ()  => axios('/reader/validate');
/*  停止校验  */
export const closeCheck = () => axios('/reader/close');
/*  提交校验结果  */
export const postList = () => axios('/data/saveValidate/CAD7716412D69132E8E072317A98B0EA');
/*  打印历史记录  */
export const printItem = (data)=> axios('/data/findInPrintHistory/CAD7716412D69132E8E072317A98B0EA',data,'POST');
/*  校验历史记录  */
export const verifyItem = (data) => axios('/data/findInValidateHistory/CAD7716412D69132E8E072317A98B0EA',data,'POST');
