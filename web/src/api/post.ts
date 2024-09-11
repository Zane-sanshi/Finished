// 权限问题后期增加
import { get, post } from '/@/utils/http/axios';
import { UserState } from '/@/store/modules/user/types';
// import axios from 'axios';
enum URL {
    listUserPostUrl = '/api/post/listUserPostApi',
    listCompanyPostUrl = '/api/post/listCompanyPostApi',
    create = '/api/post/create',
    delete = '/api/post/delete',
}

const listUserPostApi = async (params: any) => get<any>({ url: URL.listUserPostUrl, params: params, data: {}, headers: {} });
const listCompanyPostApi = async (params: any) => get<any>({ url: URL.listCompanyPostUrl, params: params, data: {}, headers: {} });
const createApi = async (data: any) =>
    post<any>({ url: URL.create, params: {}, data: data, headers: { 'Content-Type': 'multipart/form-data;charset=utf-8' } });
const deleteApi = async (params: any) => post<any>({ url: URL.delete, params: params, headers:{} });

export { listUserPostApi, listCompanyPostApi, createApi, deleteApi };
